package com.grocex_api.reportService.serviceImpl;

import com.grocex_api.reportService.dto.ReportData;
import com.grocex_api.reportService.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public byte[] getReport(){
       try {
           // Create dummy data
           ReportData reportData = new ReportData();
           reportData.setApplicationNumber("APP-00123");
           reportData.setAddressOfPremises("123 Main Street, Springfield");
           reportData.setNameOfLandLord("John Doe");
           reportData.setNameOfTenant("Jane Smith");
           reportData.setCurrentRent(1200.00);
           reportData.setRentForFixturesAndFitting(150.00);
           reportData.setFutureRentAssessed(1350.00);
           reportData.setRates("Water, Electricity, Waste Collection");
           reportData.setPortionOfPremises("Ground Floor - Shop 1, Ground Floor - Shop 2, Ground Floor - Shop 3 ");
           reportData.setRatesForEntirePremises(3000.00);
           reportData.setRentForTimeBeingInForce(1250.00);
           reportData.setApportionmentOfRent(new BigDecimal("675.00"));
           reportData.setOtherMatters("Includes 5% increase as per recent agreement.");
           reportData.setCreatedAt(ZonedDateTime.now());
           reportData.setUnitName("Sunshine Plaza");

           // Wrap in a collection
           List<ReportData> dataList = Collections.singletonList(reportData);
           JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);

           // Parameters
           Map<String, Object> parameters = new HashMap<>();
           parameters.put("test", "test");

           // Load file
           InputStream templateStream = getClass().getResourceAsStream("/templates/report.jrxml");

           // Compile
           JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);
           log.info("compiled file->>>>{}", jasperReport);
           // Fill
           JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters, beanCollectionDataSource);

           // Export
           ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

           return outputStream.toByteArray();
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

}
