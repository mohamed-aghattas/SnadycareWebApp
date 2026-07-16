package com.orca.sndycareV99.service;

import com.orca.sndycareV99.dto.charge.ChargeRequest;
import com.orca.sndycareV99.dto.charge.ChargeResponse;
import com.orca.sndycareV99.entity.Charge;
import com.orca.sndycareV99.entity.Residence;
import com.orca.sndycareV99.repository.ChargeRepository;
import com.orca.sndycareV99.repository.PaymentRepository;
import com.orca.sndycareV99.repository.ResidenceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChargeService {

    private final ChargeRepository chargeRepository;
    private final ResidanceContextService residanceContextService;
    private final ResidenceRepository residenceRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ChargeResponse createCharge(ChargeRequest chargeRequest) {
        String chargeName = chargeRequest.getName().trim();

        boolean isExists = chargeRepository.existsByNameIgnoreCaseAndResidenceId(chargeName, currentResidenceId());

        if (isExists) {
            throw new IllegalArgumentException("A charge with the name '" + chargeName + "' already exists in this residence.");
        }

        Residence residence = getResidence(currentResidenceId());

        Charge charge = Charge.builder()
                .amount(chargeRequest.getAmount())
                .description(chargeRequest.getDescription() != null ? chargeRequest.getDescription().trim() : null)
                .frequency(chargeRequest.getFrequency())
                .name(chargeName)
                .isActive(true)
                .residence(residence)
                .build();

        Charge savedCharge = chargeRepository.save(charge);
        return toResponse(savedCharge);
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> getAllChargeByResidance() {
        return chargeRepository.findByResidenceId(currentResidenceId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ChargeResponse updateCharge(Long chargeId, ChargeRequest chargeRequest) {
        String chargeName = chargeRequest.getName().trim();
        String description = chargeRequest.getDescription() != null ? chargeRequest.getDescription().trim() : null;

        Charge charge = chargeRepository.findByIdAndResidenceId(chargeId, currentResidenceId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Charge id = " + chargeId + " not found in residence id = " + currentResidenceId()));

        if (!charge.getName().equalsIgnoreCase(chargeName)) {
            boolean isExist = chargeRepository.existsByNameIgnoreCaseAndResidenceId(chargeName, currentResidenceId());

            if (isExist) {
                throw new IllegalArgumentException("Charge name '" + chargeName + "' already exists in this residence.");
            }
            charge.setName(chargeName);
        }

        charge.setAmount(chargeRequest.getAmount());
        charge.setFrequency(chargeRequest.getFrequency());
        charge.setDescription(description);

        Charge updatedCharge = chargeRepository.save(charge);
        return toResponse(updatedCharge);
    }

    @Transactional
    public void deleteChargeSafely(Long chargeId) {
        Charge charge = chargeRepository.findByIdAndResidenceId(chargeId, currentResidenceId())
                .orElseThrow(() -> new EntityNotFoundException("Charge not found"));

        boolean hasPayments = paymentRepository.existsByChargeId(chargeId);
        if (hasPayments) {
            throw new IllegalStateException(
                    "This charge cannot be deleted because there are existing payment records associated with it. You can modify its value instead of deleting it."
            );
        }

        chargeRepository.delete(charge);
    }

    @Transactional
    public ChargeResponse toggleChargeStatus(Long chargeId) {
        Charge charge = chargeRepository.findByIdAndResidenceId(chargeId, currentResidenceId())
                .orElseThrow(() -> new EntityNotFoundException("Charge not found"));

        charge.setActive(!charge.isActive());

        Charge updatedCharge = chargeRepository.save(charge);
        return toResponse(updatedCharge);
    }

    @Transactional(readOnly = true)
    public Page<ChargeResponse> getAllChargesPaginated(int page, int size, String sortBy) {
        List<String> allowedSortFields = List.of("id", "name", "amount", "createdAt");

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "id";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return chargeRepository.findByResidenceId(currentResidenceId(), pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Map<Charge.Frequency, Long> getChargesCountSummaryByFrequency() {
        List<Charge> charges = chargeRepository.findByResidenceId(currentResidenceId());

        return charges.stream()
                .collect(Collectors.groupingBy(
                        Charge::getFrequency,
                        Collectors.counting()
                ));
    }

    @Transactional(readOnly = true)
    public boolean isChargeDeletable(Long chargeId) {
        return chargeRepository.findByIdAndResidenceId(chargeId, currentResidenceId())
                .map(charge -> !paymentRepository.existsByChargeId(chargeId))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> getChargesByFrequncy(Charge.Frequency frequency) {
        return chargeRepository.findByResidenceIdAndFrequency(currentResidenceId(), frequency).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> searchChargesByName(String query) {
        return chargeRepository.findByResidenceIdAndNameContainingIgnoreCase(currentResidenceId(), query.trim()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalChargesAmountByResidence() {
        return chargeRepository.findByResidenceId(currentResidenceId()).stream()
                .map(Charge::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional(readOnly = true)
    public ChargeResponse getOneCharge(Long chargeId){

        Charge charge = chargeRepository.findByIdAndResidenceId(chargeId,currentResidenceId())
                .orElseThrow(()-> new EntityNotFoundException("This Charge not found"));

        return  toResponse(charge);
    }

    @Transactional(readOnly = true)
    private Residence getResidence(Long residenceId) {
        return residenceRepository.findById(residenceId)
                .orElseThrow(() -> new EntityNotFoundException("Residence not found"));
    }

    private Long currentResidenceId() {

        Long residenceId = residanceContextService.getResidanceId();
        if (residenceId == null) {
            throw new IllegalStateException(
                    "Access Denied: No active residence context found. Please make sure the 'Residence-ID' header is provided."
            );
        }
        return residenceId;
    }

    private ChargeResponse toResponse(Charge charge) {
        return ChargeResponse.builder()
                .id(charge.getId())
                .name(charge.getName())
                .description(charge.getDescription())
                .amount(charge.getAmount())
                .isActive(charge.isActive())
                .frequency(charge.getFrequency())
                .createdAt(charge.getCreatedAt())
                .updatedAt(charge.getUpdatedAt())
                .build();
    }
}