package com.grocex_api.reportService.rest;

import com.grocex_api.reportService.serviceImpl.ReportServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/v1/report")
public class ReportRest {

    private final ReportServiceImpl reportService;

    @Autowired
    public ReportRest(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<byte[]> getReport() {
        byte[] pdfData = reportService.getReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline")
                .filename("rent-assessment-report.pdf")
                .build());

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }
}
