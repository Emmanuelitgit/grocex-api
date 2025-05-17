package com.grocex_api.reportService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {
    private String applicationNumber;
    private String addressOfPremises;
    private String nameOfLandLord;
    private String nameOfTenant;
    private Double currentRent;
    private Double rentForFixturesAndFitting;
    private Double futureRentAssessed;
    private String rates;
    private String portionOfPremises;
    private Double ratesForEntirePremises;
    private Double rentForTimeBeingInForce;
    private BigDecimal apportionmentOfRent;
    private String otherMatters;
    private ZonedDateTime createdAt;
    private String unitName;
}