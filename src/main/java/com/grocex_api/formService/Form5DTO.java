//package com.rentcontrol.formservice.dto;
//
//import com.rentcontrol.formservice.models.RcClientForm1;
//import com.rentcontrol.formservice.models.RcClientForm5;
//import com.rentcontrol.formservice.utility.AppUtils;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.time.ZonedDateTime;
//import java.util.*;
//
//import static com.rentcontrol.formservice.utility.AppUtils.isNotNullOrEmpty;
//
//@Slf4j
//@Data
//public class Form5DTO {
//    private String applicationNumber;
//
//    private UUID assessmentId;
//
//    private String addressOfPremises;
//
//    private String nameOfLandLord;
//    private String nameOfTenant;
//
//    private String unitName;
//
//    private Date publishDate;
//
//    private UUID formTypeId;
//
//    private List<RateDTO> rates = new ArrayList<>();
//
//    private Double currentRent;
//    private Double rentForFixturesAndFitting;
//    private Double futureRentAssessed;
//
//
//    // changed attribute data type from string
//    private List<String> portionOfPremises;
//    private Double ratesForEntirePremises;
//    private Double rentForTimeBeingInForce;
//    // change attribute data type from string
//    private Float apportionmentOfRent;
//    private String otherMatters;
//
//    private UUID createdBy;
//    private ZonedDateTime createdAt;
//    private UUID updatedBy;
//    private ZonedDateTime updatedAt;
//
//
//    public Form5DTO convertToFormDTO(RcClientForm5 form5){
//
//        if(!isNotNullOrEmpty(form5)) return null;
//
//        Form5DTO form5DTO = new Form5DTO();
//
//        form5DTO.setPublishDate(form5.getPublishDate());
//
//        form5DTO.setApplicationNumber(form5.getApplicationNumber());
//
//        form5DTO.setAssessmentId(form5.getAssessmentId());
//
//        form5DTO.setAddressOfPremises(form5.getAddressOfPremises());
//
//        form5DTO.setNameOfLandLord(form5.getNameOfLandLord());
//
//        form5DTO.setNameOfTenant(form5.getNameOfTenant());
//
//        form5DTO.setCurrentRent(form5.getCurrentRent());
//
//        form5DTO.setRentForFixturesAndFitting(form5DTO.getRentForFixturesAndFitting());
//
//        form5DTO.setFutureRentAssessed(form5.getFutureRentAssessed());
//
//        form5DTO.setFormTypeId(form5.getFormTypeId());
//
//        form5DTO.setUnitName(form5.getUnitName());
//
//        form5DTO.setRentForTimeBeingInForce(form5.getRentForTimeBeingInForce());
//
//        if(isNotNullOrEmpty(form5.getRates())){
//            String[] reasonsArray = (form5.getRates()).split(" - ");
//            List<RateDTO> rates = new ArrayList<>();
//
//            for(String item : reasonsArray){
//
//                String[] rateObjects = item.split(":");
//
//                RateDTO rateDTO = new RateDTO(rateObjects[0], Double.parseDouble(rateObjects[1].trim()));
//
//                rates.add(rateDTO);
//            }
//
//            form5DTO.setRates(rates);
//        }
//
//        // converting portion of premises to an array
//        String[] data = form5.getPortionOfPremises().split(",\\s*");
//        List<String> portionOfPremisesArray = new ArrayList<>(Arrays.asList(data));
//        form5DTO.setPortionOfPremises(portionOfPremisesArray);
//
//        form5DTO.setOtherMatters(form5.getOtherMatters());
//        form5DTO.setRatesForEntirePremises(form5.getRatesForEntirePremises());
//        form5DTO.setApportionmentOfRent(form5.getApportionmentOfRent());
//
//        form5DTO.setCreatedAt(form5.getCreatedAt());
//        form5DTO.setUpdatedAt(form5.getUpdatedAt());
//        form5DTO.setCreatedBy(form5.getCreatedBy());
//        form5DTO.setUpdatedBy(form5.getUpdatedBy());
//
//        return form5DTO;
//    }
//
//
//    public RcClientForm5 convertToForm(Form5DTO form5DTO){
//        RcClientForm5 form5 = new RcClientForm5();
//
//        if(!isNotNullOrEmpty(form5DTO)){
//            return form5;
//        }
//        if(isNotNullOrEmpty(form5DTO.getRates())){
//            String rates = "";
//            for (RateDTO rate : form5DTO.getRates()){
//                rates += rate.rateName() +":" + rate.rateAmount() + " - ";
//            }
//            form5.setRates(rates);
//        }
//
//        form5.setApplicationNumber(form5DTO.getApplicationNumber());
//
//        form5.setAssessmentId(form5DTO.getAssessmentId());
//
//        form5.setPublishDate(form5DTO.getPublishDate());
//
//        form5.setAddressOfPremises(form5DTO.getAddressOfPremises());
//
//        form5.setNameOfLandLord(form5DTO.getNameOfLandLord());
//
//        form5.setNameOfTenant(form5DTO.getNameOfTenant());
//
//        form5.setCurrentRent(form5DTO.getCurrentRent());
//
//        form5.setRentForFixturesAndFitting(form5DTO.getRentForFixturesAndFitting());
//
//        form5.setFutureRentAssessed(form5DTO.getFutureRentAssessed());
//
//        form5.setFormTypeId(form5DTO.getFormTypeId());
//
//        form5.setUnitName(form5DTO.getUnitName());
//
//        form5.setRentForTimeBeingInForce(form5DTO.getRentForTimeBeingInForce());
//
//        // converting the array of portion of premises received from the client to a string seperated comma.
//        var concatenatedResult="";
//        for (String value: form5DTO.getPortionOfPremises()){
//            concatenatedResult = concatenatedResult + "," + value;
//        }
//        form5.setPortionOfPremises(concatenatedResult.split(",", 2)[1]);
//
//        form5.setOtherMatters(form5DTO.getOtherMatters());
//        form5.setRatesForEntirePremises(form5DTO.getRatesForEntirePremises());
//        form5.setApportionmentOfRent(form5DTO.getApportionmentOfRent());
//
//        form5.setCreatedAt(form5DTO.getCreatedAt());
//        form5.setUpdatedAt(form5DTO.getUpdatedAt());
//        form5.setCreatedBy(form5DTO.getCreatedBy());
//        form5.setUpdatedBy(form5DTO.getUpdatedBy());
//
//        return form5;
//    }
//}