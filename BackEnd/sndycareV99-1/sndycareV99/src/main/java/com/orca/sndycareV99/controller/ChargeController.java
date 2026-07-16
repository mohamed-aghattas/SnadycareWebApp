package com.orca.sndycareV99.controller;

import com.orca.sndycareV99.dto.charge.ChargeRequest;
import com.orca.sndycareV99.dto.charge.ChargeResponse;
import com.orca.sndycareV99.entity.Charge;
import com.orca.sndycareV99.service.ChargeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/residence/charges")
@RequiredArgsConstructor
public class ChargeController {

    private final ChargeService chargeService;


    @PostMapping
    public ResponseEntity<ChargeResponse> createCharge(@Valid @RequestBody ChargeRequest chargeRequest) {
        ChargeResponse createdCharge = chargeService.createCharge(chargeRequest);
        return new ResponseEntity<>(createdCharge, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ChargeResponse>> getAllChargesByResidence() {
        List<ChargeResponse> charges = chargeService.getAllChargeByResidance();
        return ResponseEntity.ok(charges);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargeResponse> getCharge(@PathVariable Long id) {
        ChargeResponse charge = chargeService.getOneCharge(id);
        return ResponseEntity.ok(charge);
    }


    @GetMapping("/page")
    public ResponseEntity<Page<ChargeResponse>> getAllChargesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Page<ChargeResponse> paginatedCharges = chargeService.getAllChargesPaginated(page, size, sortBy);
        return ResponseEntity.ok(paginatedCharges);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ChargeResponse> updateCharge(
            @PathVariable Long id,
            @Valid @RequestBody ChargeRequest chargeRequest
    ) {
        ChargeResponse updatedCharge = chargeService.updateCharge(id, chargeRequest);
        return ResponseEntity.ok(updatedCharge);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteChargeSafely(@PathVariable Long id) {
        chargeService.deleteChargeSafely(id);
        return ResponseEntity.ok(Map.of("message", "Charge deleted successfully."));
    }


    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ChargeResponse> toggleChargeStatus(@PathVariable Long id) {
        ChargeResponse updatedCharge = chargeService.toggleChargeStatus(id);
        return ResponseEntity.ok(updatedCharge);
    }


    @GetMapping("/{id}/deletable")
    public ResponseEntity<Map<String, Boolean>> isChargeDeletable(@PathVariable Long id) {
        boolean isDeletable = chargeService.isChargeDeletable(id);
        return ResponseEntity.ok(Map.of("deletable", isDeletable));
    }

    @GetMapping("/frequency/{frequency}")
    public ResponseEntity<List<ChargeResponse>> getChargesByFrequency(@PathVariable Charge.Frequency frequency) {
        List<ChargeResponse> charges = chargeService.getChargesByFrequncy(frequency);
        return ResponseEntity.ok(charges);
    }


    @GetMapping("/search")
    public ResponseEntity<List<ChargeResponse>> searchChargesByName(@RequestParam String query) {
        List<ChargeResponse> charges = chargeService.searchChargesByName(query);
        return ResponseEntity.ok(charges);
    }


    @GetMapping("/total-amount")
    public ResponseEntity<Map<String, BigDecimal>> getTotalChargesAmountByResidence() {
        BigDecimal totalAmount = chargeService.getTotalChargesAmountByResidence();
        return ResponseEntity.ok(Map.of("totalAmount", totalAmount));
    }

    @GetMapping("/summary/frequency")
    public ResponseEntity<Map<Charge.Frequency, Long>> getChargesCountSummaryByFrequency() {
        Map<Charge.Frequency, Long> summary = chargeService.getChargesCountSummaryByFrequency();
        return ResponseEntity.ok(summary);
    }
}