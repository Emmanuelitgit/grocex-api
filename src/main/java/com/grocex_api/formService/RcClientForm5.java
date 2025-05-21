//package com.rentcontrol.formservice.models;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.UUID;
//
//@Table(name = "rc_client_form_5")
//@Entity
//@Data
//public class RcClientForm5 {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    private String applicationNumber;
//
//    private UUID assessmentId;
//
//    private Date publishDate;
//
//    private String addressOfPremises;
//
//    @Column(name = "land_lord")
//    private String nameOfLandLord;
//
//    @Column(name = "tenant")
//    private String nameOfTenant;
//
//    private String rates;
//    @Column(name = "portion_of_premises")
//    private String portionOfPremises;
//
//    private Double currentRent;
//
//    @Column(name = "fixtures_fitting")
//    private Double rentForFixturesAndFitting;
//
//    @Column(name = "rent_assessed")
//    private Double futureRentAssessed;
//
//    private Double ratesForEntirePremises;
//
//    private Double rentForTimeBeingInForce;
//
//    // change attribute data type from string
//    private Float apportionmentOfRent;
//
//    private String otherMatters;
//
//    private UUID formTypeId;
//
//    private UUID createdBy;
//    private ZonedDateTime createdAt;
//
//    private UUID updatedBy;
//    private ZonedDateTime updatedAt;
//    private String unitName;
//
//    @PrePersist
//    private void setCreationDate() {
//        createdAt = ZonedDateTime.now();
//        setUpdateDate();
//    }
//
//    @PreUpdate
//    public void setUpdateDate() {
//        updatedAt = ZonedDateTime.now();
//
//    }
//
//}
