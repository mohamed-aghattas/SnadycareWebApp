package com.orca.sndycareV99.service;

import com.orca.sndycareV99.dto.building.BuildingLayoutResponse;
import com.orca.sndycareV99.dto.building.BuildingStats;
import com.orca.sndycareV99.dto.property.FloorDTO;
import com.orca.sndycareV99.dto.property.UnitDTO;
import com.orca.sndycareV99.entity.Payment;
import com.orca.sndycareV99.entity.Property;
import com.orca.sndycareV99.repository.PaymentRepository;
import com.orca.sndycareV99.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildingLayoutService {

    private final PropertyRepository propertyRepository;
    private final PaymentRepository paymentRepository;

    public BuildingLayoutResponse getBuildingLayout(Long buildingId, Long currentChargeId) {

        List<Property> properties = propertyRepository.findByBuildingIdOrderByFloorNumberDescPropertyCodeAsc(buildingId);

        List<Payment> payments = paymentRepository.findPaymentsByBuildingAndCharge(buildingId, currentChargeId);

        Map<Long, Payment.PaymentStatus> propertyPaymentStatusMap = payments.stream()
                .collect(Collectors.toMap(
                        p -> p.getProperty().getId(),
                        Payment::getStatus,
                        (existing, replacement) -> existing
                ));

        long totalPaid = 0;
        long totalUnpaid = 0;
        long totalEmpty = 0;

        List<FlatUnitInfo> flatUnits = new ArrayList<>();

        for (Property prop : properties) {
            String uiStatus;

            if (prop.getStatus() == Property.PropertyStatus.AVAILABLE) {
                uiStatus = "EMPTY";
                totalEmpty++;
            } else {
                Payment.PaymentStatus paymentStatus = propertyPaymentStatusMap.get(prop.getId());

                if (paymentStatus == Payment.PaymentStatus.PAID) {
                    uiStatus = "PAID";
                    totalPaid++;
                } else {
                    uiStatus = "UNPAID";
                    totalUnpaid++;
                }
            }

            flatUnits.add(new FlatUnitInfo(
                    prop.getFloorNumber() != null ? prop.getFloorNumber() : 0,
                    new UnitDTO(
                            prop.getId(),
                            prop.getPropertyCode(),
                            prop.getPropertyType().name(),
                            uiStatus
                    )
            ));
        }

        Map<Integer, List<UnitDTO>> groupedByFloor = flatUnits.stream()
                .collect(Collectors.groupingBy(
                        FlatUnitInfo::floorNumber,
                        LinkedHashMap::new,
                        Collectors.mapping(FlatUnitInfo::unit, Collectors.toList())
                ));

        List<FloorDTO> floors = new ArrayList<>();
        groupedByFloor.forEach((floorNum, units) -> {

            String floorName = floorNum == 0 ? "Ground Floor" : "Floor " + floorNum;
            floors.add(new FloorDTO(floorNum, floorName, units));
        });

        return new BuildingLayoutResponse(
                buildingId,
                properties.isEmpty() ? "Unknown Building" : properties.get(0).getBuilding().getName(),
                floors.size(),
                new BuildingStats(totalPaid, totalUnpaid, totalEmpty),
                floors
        );
    }

    private record FlatUnitInfo(Integer floorNumber, UnitDTO unit) {}
}