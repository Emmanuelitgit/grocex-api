///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.rentcontrol.formservice.serviceImp;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rentcontrol.formservice.dto.*;
//import com.rentcontrol.formservice.models.*;
//import com.rentcontrol.formservice.repositories.*;
//import com.rentcontrol.formservice.service.FormService;
//import com.rentcontrol.formservice.util.JdbcService;
//import com.rentcontrol.formservice.utility.pdf.PDFService;
//import com.rentcontrol.formservice.utility.storage.StorageService;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.modelmapper.ModelMapper;
//import org.modelmapper.convention.MatchingStrategies;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalTime;
//import java.time.ZonedDateTime;
//import java.util.*;
//
//import static com.rentcontrol.formservice.utility.AppUtils.*;
//
///**
// * @author KwakuAf
// */
//@Slf4j
//@Service
//@AllArgsConstructor
//public class FormServiceImpl implements FormService {
//
//    ModelMapper modelMapper;
//
//    ObjectMapper objectMapper;
//
//    private final FormRepository formRepo;
//    private final RcClientForm7Repository form7Repo;
//    private final RcClientForm33Repository form33Repo;
//    private final RcClientAttachmentRepository attachmentRepo;
//    private final RcSetupFormTypeRepo rcSetupFormTypeRepo;
//    private final RcClientWitnessRepository witnessRepo;
//    private final JdbcService jdbcService;
//    private final StorageService storageService;
//    private final PDFService pdfService;
//    private final RcClientForm8Repository form8Repo;
//    private final RcClientForm9Repository form9Repo;
//    private final RcClientForm3Repository form3Repo;
//    private final RcClientForm10Repository form10Repo;
//    private final RcClientForm11Repository form11Repo;
//    private final RcClientForm12Repository form12Repo;
//
//    private final RcClientForm1Repository form1Repository;
//
//    private final RcClientForm5Repository form5Repository;
//
//    /**
//     * This method returns all related forms for a complaint with its related
//     * attachments It returns a Response Object with results, message, http
//     * status code and ZonedDateTime
//     *
//     * @param complaintId - Complaint ID
//     * @return ResponseDTO- Custom Response Object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO getFormByComplaintId(String complaintId) {
//
//        if (complaintId == null || complaintId.isEmpty()) {
//
//            return ResponseDTO.builder()
//                    .message("ComplaintId cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//
//            List<FormLstDTO> formList = jdbcService.getFormByComplaintId(UUID.fromString(complaintId));
//
//            Form rcForm = formRepo.findAllByComplaintIdWithFormType(UUID.fromString(complaintId));
//
//            if (rcForm != null) {
//                if (rcForm.getFormTypeId() != null) {
//                    FormTypeDTO rcFormType = new FormTypeDTO();
//                    rcFormType = jdbcService.getFormType(rcForm.getFormTypeId());
//                    String type = "FORM_7";
//                    if (rcFormType != null) {
//                        rcForm.setFormType(rcFormType);
//
//                        if (rcForm.getFormType().getFormNumber() == 7) {
//                            RcClientForm7DTO form7 = new RcClientForm7DTO();
//                            form7 = jdbcService.getForm7(rcForm.getId());
//                            if (form7 != null) {
//                                rcForm.setFormData(form7);
//                            }
//                        } else if (rcForm.getFormType().getFormNumber() == 33) {
//                            type = "FORM_33";
//                            Form33DTO form33 = new Form33DTO();
//                            form33 = jdbcService.getForm33(rcForm.getId());
//                            if (form33 != null) {
//                                rcForm.setFormData(form33);
//                            }
//
//                        }
//
//                    }
//
//                    List attachements = jdbcService.getAttachments(rcForm.getId(), type);
//                    if (attachements != null) {
//                        rcForm.setFormDataAttachments(attachements);
//                    }
//
//                    List affidavits = jdbcService.getAffidavit(rcForm.getId());
//
//                    if (affidavits != null) {
//
//                        rcForm.setAffidavit(affidavits);
//                    }
//                }
//
//            }
//            FormListDTO forms = new FormListDTO();
//
//            forms.setFormList(formList);
//            forms.setForm(rcForm);
//            forms.setWitnessForms(witnessRepo.findWitnessByComplaintId(UUID.fromString(complaintId)));
//
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(forms)
//                    .date(ZonedDateTime.now())
//                    .build();
//        } catch (ClassCastException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (NullPointerException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//    }
//
//    /**
//     * This method returns all related forms for an assessment with its related
//     * attachments It returns a Response Object with results, message, http
//     * status code and ZonedDateTime
//     *
//     * @param assessmentId - Assessment ID
//     * @return ResponseDTO- Custom Response Object
//     * @Author : Ebenezer N.
//     * @createdAt 15th Oct 2023
//     */
//    @Override
//    public ResponseDTO getFormByAssessmentId(UUID assessmentId) {
//
//        if (!isNotNullOrEmpty(assessmentId)) {
//
//            return ResponseDTO.builder()
//                    .message("AssessmentId cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//
//            List<FormAssLstDTO> formList = jdbcService.getFormByAssessmentId(assessmentId);
//            /*Form1DTO rcForm = new Form1DTO();
//            try{
//                rcForm = rcForm.convertToForm1DTO(form1Repository.findByAssessmentIdIs(assessmentId));
//            }catch (Exception e){
//                log.error(e.getMessage());
//            }
//
//            if (rcForm != null) {
//                if (rcForm.getFormTypeId() != null) {
//                    FormTypeDTO rcFormType = jdbcService.getFormType(rcForm.getFormTypeId());
////                    String type = "FORM_1";
//                    if (rcFormType != null) {
//                        rcForm.setFormType(rcFormType);
//                    }
//
//                    *//*List attachements = jdbcService.getAttachments(rcForm.getId(), type);
//                    if (attachements != null) {
//                        rcForm.setFormDataAttachments(attachements);
//                    }*//*
//                }
//
//            }*/
//            FormAssListDTO forms = new FormAssListDTO();
//
//            forms.setFormList(formList);
////            forms.setForm(rcForm);
//
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(forms)
//                    .date(ZonedDateTime.now())
//                    .build();
//        } catch (ClassCastException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (NullPointerException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//    }
//
//    /**
//     * This method returns current form 33 for a complaint It returns a Response
//     * Object with results, message, http status code and ZonedDateTime
//     *
//     * @param complaintId - Complaint ID
//     * @return ResponseDTO- Custom Response Object
//     * @Author : Ebenezer N.
//     */
//    @Override
//    public ResponseDTO getForm33ByComplaintId(String complaintId) {
//
//        if (complaintId == null || complaintId.isEmpty()) {
//            return ResponseDTO.builder()
//                    .message("ComplaintId cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//
//            List<Form33DTO> form33DTOList = jdbcService.getForm33ByComplaintId(UUID.fromString(complaintId));
//
//            if (form33DTOList == null || form33DTOList.size() == 0) {
//                return ResponseDTO.builder()
//                        .message("Complaint does not have any current form 33")
//                        .statusCode(404)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//            }
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(form33DTOList.get(0))
//                    .date(ZonedDateTime.now())
//                    .build();
//        } catch (ClassCastException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (NullPointerException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//    }
//
//    /**
//     * This method returns all form 7 for a complaint It returns a Response
//     * Object with results, message, http status code and ZonedDateTime
//     *
//     * @param complaintId - Complaint ID
//     * @return ResponseDTO- Custom Response Object
//     * @Author : Ebenezer N.
//     */
//    @Override
//    public ResponseDTO getForm7ByComplaintId(String complaintId) {
//
//        if (complaintId == null || complaintId.isEmpty()) {
//            return ResponseDTO.builder()
//                    .message("ComplaintId cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//
//        try {
//
//            List<RcClientForm7DTO> form7DTOList = jdbcService.getForm7ByComplaintId(UUID.fromString(complaintId));
//
//            if (form7DTOList == null || form7DTOList.size() == 0) {
//                return ResponseDTO.builder()
//                        .message("Complaint does not have any form 7")
//                        .statusCode(404)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//            }
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(form7DTOList)
//                    .date(ZonedDateTime.now())
//                    .build();
//        } catch (ClassCastException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (NullPointerException e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//    }
//
//    /**
//     * This method returns the latest form for a complaint It returns a Response
//     * Object with results, message, http status code and ZonedDateTime
//     *
//     * @param complaintId - Complaint ID
//     * @param formNumber - Form Number
//     * @return ResponseDTO - Custom Response Object
//     * @Author Ebenezer N.
//     * @createdAt 18th August 2023
//     */
//    @Override
//    public ResponseDTO getFormByComplaintId(String complaintId, String formNumber) {
//
//        if (complaintId == null || complaintId.isEmpty()) {
//            return getResponseDto("ComplaintId cannot be null", HttpStatus.NOT_FOUND, "error");
//        }
//
//        try {
//
//            if (formNumber.trim().equalsIgnoreCase("7")) {
//                return getForm7ByComplaintId(complaintId);
//            } else if (formNumber.trim().equalsIgnoreCase("33")) {
//                return getForm33ByComplaintId(complaintId);
//            }
//
//            List response = jdbcService.getFormByComplaintId(UUID.fromString(complaintId), formNumber);
//
//            if (response == null || response.isEmpty()) {
//                return getResponseDto("Complaint does not have any form " + formNumber, HttpStatus.NOT_FOUND, "error");
//            }
//            return getResponseDto("success", HttpStatus.OK, response);
//
//        } catch (ClassCastException e) {
//
//            return getResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST, "error");
//
//        } catch (NullPointerException e) {
//
//            return getResponseDto(e.getMessage(), HttpStatus.NOT_FOUND, "error");
//
//        } catch (Exception e) {
//
//            return getResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST, "error");
//
//        }
//    }
//
//    /**
//     * This method is used to store either Form1 or Form5 which is a
//     * prerequisite form assessment
//     *
//     * @param formObject
//     * @return responseEntity
//     * @author Samuel O.
//     * @createdAt 31st August 2023
//     * @modified
//     * @modifiedAt
//     * @modifiedBy
//     */
//    @Override
//    public ResponseDTO storeAssessmentForm(Object formObject, Integer formNumber) {
//        {
//            if (!isNotNullOrEmpty(formObject)) {
//                return getResponseDto("Form" + formNumber + "DTO cannot be empty", HttpStatus.BAD_REQUEST);
//            }
//
//            try {
//                log.info("Trying to save assessment form -->> FormServiceImpl::storeAssessmentForm");
//                ModelMapper modelMapper1 = new ModelMapper();
//                modelMapper1.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Optional, for strict property name matching.
//
//                /**
//                 * Saving form1
//                 */
//                if (formNumber == 1) {
//                    var form = objectMapper.convertValue(formObject, Form1DTO.class);
//                    /*var form = modelMapper1.typeMap(Map.class, Form1DTO.class)
//                            .addMappings(mapper -> mapper.skip(Form1DTO::setId))
//                            .map(formObject);*/
////                            modelMapper.map(formObject, Form1DTO.class);
//
//
//                    log.info("Trying to save Form1");
//                    return saveForm1(form);
//
//                    /**
//                     * Saving form5
//                     */
//                } else if (formNumber == 5) {
//                    var form5 = objectMapper.convertValue(formObject, Form5DTO.class);
//                    return saveForm5(form5, formNumber);
//
//                    /**
//                     * Throws error when instance is neither form1 nor form5
//                     */
//                } else {
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form " + formNumber + " instance not properly handled");
//                }
//            } catch (Exception e) {
//                log.info("Error saving new form" + e.getMessage() + " --> FormServiceImpl::storeAssessmentForm");
//                return getResponseDto("Error saving Form" + formNumber, HttpStatus.BAD_REQUEST);
//            }
//        }
//    }
//
//    @Override
//    public ResponseDTO fetchAssessmentForm(int formNumber, UUID assessmentId) {
//        log.info("Inside FormServiceImpl::fetchAssessmentForm");
//        ResponseDTO responseDTO;
//
//        try{
//
//            if(formNumber ==  1){
//                log.info("Trying to fetch form1...");
//
//
//                Optional<RcClientForm1> optional = form1Repository.findByAssessmentIdIs(assessmentId);
//
//                if(!optional.isPresent())
//                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Form 1 not found by Assessment ID");
//
//                RcClientForm1 form1 = optional.get();
//
//                Form1DTO f = new Form1DTO();
//
//                responseDTO = getResponseDto("success", HttpStatus.OK, f.convertToForm1DTO(form1));
//
//            }else{
//
//                responseDTO = getResponseDto("success", HttpStatus.NOT_FOUND, null);
//
//            }
//
//            return responseDTO;
//
//        }catch(Exception e){
//            e.printStackTrace();
//            responseDTO = getResponseDto("Error", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return responseDTO;
//    }
//
//    private ResponseDTO getForm5ByAssessmentNumber(String assessmentNumber) {
//        ResponseDTO responseDTO;
//        RcClientForm5 form5 = form5Repository.findByApplicationNumberIs(assessmentNumber);
//
//        Form5DTO f = new Form5DTO();
//
//        responseDTO = getResponseDto("success", HttpStatus.OK, f.convertToFormDTO(form5));
//        return responseDTO;
//    }
//
//    /**
//     * This method focuses on saving Form1 for assessments requests
//     *
//     * @param formObject
//     * @return ResponseDTO
//     * @author Samuel O.
//     * @createdAt 31st August 2023
//     * @modified
//     * @modifiedAt
//     * @modifiedBy
//     */
//    private ResponseDTO saveForm1(Form1DTO formObject) {
//        log.info("Inside FormServiceImpl::saveForm1...");
//        var formDTO = formObject;
//
//        Optional<RcClientForm1> currentForm1 = form1Repository.findByAssessmentIdIs(formObject.getAssessmentId());
//        if(currentForm1.isPresent()){
//            var current = currentForm1.get();
//            current.setIsCurrent(false);
//            form1Repository.save(current);
//        }
//
//        RcSetupFormType formTypeDT = rcSetupFormTypeRepo.findRcSetupFormTypeByformNumber(formDTO.getFormNumber());
//
//        /**
//         * Checking if formTypeDT and formTyDT.getId() both contain data
//         */
//        if (isNotNullOrEmpty(formTypeDT) && isNotNullOrEmpty(formTypeDT.getId())) {
//            formDTO.setFormTypeId(formTypeDT.getId());
//        }
//
//        RcClientForm1 form1 = formDTO.convertToForm(formDTO);
//        form1.setIsCurrent(true);
//
//        RcClientForm1 newForm = form1Repository.save(form1);
//
//        /**
//         * Checking if newForm and newForm.getId() both contain data
//         */
//        if (isNotNullOrEmpty(newForm) && isNotNullOrEmpty(newForm.getId())) {
//            return getResponseDto("success", HttpStatus.CREATED, newForm);
//
//        } else {
//            return getResponseDto("Error saving Form1", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    /**
//     * This method focuses on saving Form5 for assessments requests
//     *
//     * @param formObject
//     * @return ResponseDTO
//     * @author Samuel O.
//     * @createdAt 31st August 2023
//     * @modified
//     * @modifiedAt
//     * @modifiedBy
//     */
//    private ResponseDTO saveForm5(Form5DTO formObject, Integer formNumber) {
//        log.info("Inside form service:->>>{}", formObject);
//        var formDTO = formObject;
//        RcSetupFormType formTypeDT = rcSetupFormTypeRepo.findRcSetupFormTypeByformNumber(formNumber);
//
//        if (formTypeDT != null && formTypeDT.getId() != null) {
//            formDTO.setFormTypeId(formTypeDT.getId());
//        }
//
//        RcClientForm5 newForm = form5Repository.save(formDTO.convertToForm(formDTO));
//
//        if (newForm != null && newForm.getId() != null) {
//            return getResponseDto("success", HttpStatus.CREATED, formDTO.convertToFormDTO(newForm));
//
//        } else {
//            return getResponseDto("Error saving Form5", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    /**
//     * This method saves all related forms for a complaint with its related
//     * attachments It returns a Response Object with results, message, http
//     * status code and ZonedDateTime
//     *
//     * @param formDTO - custom form Object
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO storeNewForm(FormDTO formDTO) {
//        if (formDTO == null) {
//
//            return ResponseDTO.builder()
//                    .message("FormDTO cannot empty")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//
//            if (formDTO.getFormNumber() == null) {
//                return ResponseDTO.builder()
//                        .message("Form Number cannot null")
//                        .statusCode(400)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//            if (formDTO.getFormNumber() == 7) {
//                RcSetupFormType formTypeDT = rcSetupFormTypeRepo.findRcSetupFormTypeByformNumber(formDTO.getFormNumber());
//
//                if (formTypeDT.getId() != null) {
//                    formDTO.setFormTypeId(formTypeDT.getId());
//                }
//                Boolean startcomplaints = true;
//
//                formDTO.setStartsComplaint(startcomplaints);
//            }
//
//            Form newForm = formRepo.save(formDTO.convertDTOToForm(formDTO));
//
//            if (newForm != null && newForm.getId() != null) {
//
//                if (formDTO.getFormNumber() == 7) {
//                    formDTO.setId(newForm.getId());
//                    form7Repo.save(formDTO.convertDTOToForm7(formDTO));
//
//                }
//
//                if (formDTO.getFormNumber() == 33) {
//                    formDTO.setId(newForm.getId());
//                    //check for any form33 in client form and set iscurrent to false in form33 table
//                    // form7Repo.save(formDTO.convertDTOToForm7(formDTO));
//
//                }
//
//                if (formDTO.getAttachments() != null && !formDTO.getAttachments().isEmpty()) {
//                    List<RcClientAttachment> listAttachements = new ArrayList<>();
//
//                    formDTO.getAttachments().forEach(value -> {
//
//                        RcClientAttachment newAttachment = new RcClientAttachment();
//                        newAttachment.setClientFormId(newForm.getId());
//                        newAttachment.setComplaintId(formDTO.getComplaintId());
//                        newAttachment.setCreatedAt(formDTO.getCreatedAt());
//                        newAttachment.setCreatedBy(formDTO.getCreatedBy());
//                        newAttachment.setCreatedByClient(formDTO.getCreatedByClient());
//                        newAttachment.setFileName(value.getFileName());
//                        newAttachment.setType("FORM_7");
//                        listAttachements.add(newAttachment);
//
//                    });
//
//                    if (!listAttachements.isEmpty()) {
//                        attachmentRepo.saveAll(listAttachements);
//                    }
//
//                }
//
//                if (formDTO.getAffidavit() != null) {
//                    List<RcClientAttachment> listAffidavit = new ArrayList<>();
//                    log.info("All Affidavit {}" + formDTO.getAffidavit());
//
//                    var value = formDTO.getAffidavit().getFileName();
//                    RcClientAttachment newAttachment = new RcClientAttachment();
//                    newAttachment.setClientFormId(newForm.getId());
//                    newAttachment.setComplaintId(formDTO.getComplaintId());
//                    newAttachment.setCreatedAt(formDTO.getCreatedAt());
//                    newAttachment.setCreatedBy(formDTO.getCreatedBy());
//                    newAttachment.setCreatedByClient(formDTO.getCreatedByClient());
//                    newAttachment.setFileName(value);
//                    newAttachment.setType("AFFIDAVIT");
//                    listAffidavit.add(newAttachment);
//
//                    if (!listAffidavit.isEmpty()) {
//                        attachmentRepo.saveAll(listAffidavit);
//                    }
//                }
//
//                return ResponseDTO.builder()
//                        .message("true")
//                        .statusCode(201)
//                        .data(newForm)
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else {
//
//                return ResponseDTO.builder()
//                        .message("Error saving new form")
//                        .statusCode(400)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//    }
//
//    @Override
//    public ResponseDTO getFormById(String formId) {
//        if (formId == null || formId.isEmpty()) {
//
//            return ResponseDTO.builder()
//                    .message("Form Id cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//        try {
//
//            Form rcForm = formRepo.findFormById(UUID.fromString(formId));
//
//            if (rcForm == null) {
//
//                return ResponseDTO.builder()
//                        .message("No data found for submitted form Id: " + formId)
//                        .statusCode(404)
//                        .data("No data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//            if (rcForm.getFormTypeId() != null) {
//                FormTypeDTO rcFormType = new FormTypeDTO();
//                rcFormType = jdbcService.getFormType(rcForm.getFormTypeId());
//                String type = "FORM_7";
//
//                if (rcFormType != null) {
//                    rcForm.setFormType(rcFormType);
//
//                    if (null != rcForm.getFormType().getFormNumber()) {
//                        switch (rcForm.getFormType().getFormNumber()) {
//                            case 7 -> {
//                                RcClientForm7DTO form7 = jdbcService.getForm7(rcForm.getId());
//                                if (form7 != null) {
//                                    rcForm.setFormData(Arrays.asList(form7));
//                                }
//                            }
//                            case 33 -> {
//                                type = "FORM_33";
//                                Form33DTO form33 = jdbcService.getForm33(rcForm.getId());
//                                if (form33 != null) {
//                                    rcForm.setFormData(form33);
//                                }
//                            }
//                            case 8 -> {
//                                OtherFormDTO form8 = jdbcService.getForm8(rcForm.getId());
//                                if (form8 != null) {
//                                    rcForm.setFormData(form8);
//                                }
//                            }
//                            case 9 -> {
//                                OtherFormDTO form9 = jdbcService.getForm9(rcForm.getId());
//                                if (form9 != null) {
//                                    rcForm.setFormData(form9);
//                                }
//                            }
//                            case 3 -> {
//                                OtherFormDTO form3 = jdbcService.getForm3(rcForm.getId());
//                                if (form3 != null) {
//                                    rcForm.setFormData(form3);
//                                }
//                            }
//                            case 10 -> {
//                                //form10Report
//                                Form10DTO form10 = jdbcService.getForm10(rcForm.getId());
//                                if (form10 != null) {
//                                    rcForm.setFormData(form10);
//                                }
//                            }
//                            case 11 -> {
//
//                                //form11Report
//                                String formnumber = "11";
//                                OtherFormDTO form11 = jdbcService.getOtherForm(rcForm.getId(), formnumber);
//                                if (form11 != null) {
//                                    rcForm.setFormData(form11);
//                                }
//                            }
//                            case 12 -> {
//                                //form12Report
//                                String formnumber = "12";
//                                OtherFormDTO form12 = jdbcService.getOtherForm(rcForm.getId(), formnumber);
//                                if (form12 != null) {
//                                    rcForm.setFormData(form12);
//                                }
//                            }
//
//                            default -> {
//                                log.warn("Invalid Formnumber Provided");
//
//                                return ResponseDTO.builder()
//                                        .message("Invalid form number provided")
//                                        .statusCode(404)
//                                        .data("error")
//                                        .date(ZonedDateTime.now())
//                                        .build();
//
//                            }
//                        }
//                    }
//                }
//
//                List attachements = jdbcService.getAttachments(rcForm.getId(), type);
//                if (attachements != null) {
//                    rcForm.setFormDataAttachments(attachements);
//                }
//
//                List affidavits = jdbcService.getAffidavit(rcForm.getId());
//
//                if (affidavits != null) {
//
//                    rcForm.setAffidavit(affidavits);
//                }
//            }
//
////            FormListDTO forms = new FormListDTO();
////            forms.setFormList(formList);
////            forms.setForm(rcForm);
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(rcForm)
//                    .date(ZonedDateTime.now())
//                    .build();
//        } catch (ClassCastException e) {
//            log.error(e.getMessage());
//
//            return ResponseDTO.builder()
//                    .message("Class cast exception")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (NullPointerException e) {
//
//            log.error(e.getMessage());
//            return ResponseDTO.builder()
//                    .message("Null pointer error")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//
//            return ResponseDTO.builder()
//                    .message("Exception  printed to logs")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//    }
//
//    /**
//     * This method saves all related forms for a complaint with its related
//     * attachments It returns a Response Object with results, message, http
//     * status code and ZonedDateTime
//     *
//     * @param formDTO - custom form 33 Object
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO storeNewForm33(Form33DTO formDTO) {
//
//        if (formDTO == null) {
//
//            return ResponseDTO.builder()
//                    .message("FormDTO cannot empty")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//
//            RcSetupFormType formTypeDT = rcSetupFormTypeRepo.findRcSetupFormTypeByformNumber(formDTO.getFormNumber());
//
//            if (formTypeDT != null && formTypeDT.getId() != null) {
//                formDTO.setFormTypeId(formTypeDT.getId());
//            }
//
//            Boolean startcomplaints = false;
//
//            formDTO.setStartsComplaint(startcomplaints);
//
//            Form newForm = formRepo.save(formDTO.convertDTOToForm(formDTO));
//
//            if (newForm != null && newForm.getId() != null) {
//
//                if (formDTO.getFormNumber() == 33) {
//                    formDTO.setId(newForm.getId());
//                    //check for any form33 in client form and set iscurrent to false in form33 table
//                    List<RcClientForm33> allForm33 = form33Repo.findAllByComplaintId(newForm.getComplaintId());
//                    if (allForm33 != null && !allForm33.isEmpty()) {
//                        List<RcClientForm33> listForm33 = new ArrayList<>();
//                        allForm33.forEach(value -> {
//                            value.setCurrent(false);
//                            listForm33.add(value);
//
//                        });
//
//                        if (!listForm33.isEmpty()) {
//                            form33Repo.saveAllAndFlush(listForm33);
//                        }
//                    }
//
//                    formDTO.setCurrent(true);
//
//                    form33Repo.save(formDTO.convertDTOToForm33(formDTO));
//
//                }
//
//                if (formDTO.getAttachments() != null && !formDTO.getAttachments().isEmpty()) {
//                    List<RcClientAttachment> listAttachements = new ArrayList<>();
//
//                    formDTO.getAttachments().forEach(value -> {
//
//                        RcClientAttachment newAttachment = new RcClientAttachment();
//                        newAttachment.setClientFormId(newForm.getId());
//                        newAttachment.setComplaintId(formDTO.getComplaintId());
//                        newAttachment.setCreatedAt(formDTO.getCreatedAt());
//                        newAttachment.setCreatedBy(formDTO.getCreatedBy());
//                        newAttachment.setCreatedByClient(formDTO.getCreatedByClient());
//                        newAttachment.setFileName(value.getFileName());
//                        newAttachment.setType("FORM_33");
//                        listAttachements.add(newAttachment);
//
//                    });
//
//                    if (!listAttachements.isEmpty()) {
//                        attachmentRepo.saveAll(listAttachements);
//                    }
//
//                }
//
//                return ResponseDTO.builder()
//                        .message("true")
//                        .statusCode(201)
//                        .data(newForm)
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else {
//
//                return ResponseDTO.builder()
//                        .message("Error saving new form")
//                        .statusCode(400)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//    }
//
//    /**
//     * This method generates a pdf object for a submited form ID. It returns a
//     * Response Object with byte data, message, http status code and
//     * ZonedDateTime
//     *
//     * @param id - String Client Form ID
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public byte[] getFormReportById(String id) {
//        Map<String, Object> jasperParameters = new HashMap<>();
//        jasperParameters.put("rentLogo", storageService.loadImageFile("rentcontrollogo_png"));
//
//        try {
//            UUID formId = UUID.fromString(id);
//            if (formRepo.existsById(formId)) {
//                Form rcForm = formRepo.findFormById(formId);
//
//                if (rcForm == null || rcForm.getFormTypeId() == null) {
//
//                    return null;
//
//                }
//
//                FormTypeDTO rcFormType = jdbcService.getFormType(rcForm.getFormTypeId());
//                if (rcFormType == null) {
//                    return null;
//                }
//
//                rcForm.setFormType(rcFormType);
//
//
//
//                if (null != rcForm.getFormType().getFormNumber()) {
//
//                    var jasperFileName = storageService.getJasperTemplate(rcForm.getFormType().getFormNumber());
//
//                    switch (rcForm.getFormType().getFormNumber()) {
//                        case 7 -> {
//                            log.info("Form 7 Jasper Report Generated");
//                            String formNumber = "7";
//                            Form7JasperDTO form7 = jdbcService.getForm7(rcForm.getId()).convertDTOToJasper7(rcForm.getComplaintNumber(), rcForm.getCreatedAt());
//
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form7));
//                            return report;
//                        }
//                        case 33 -> {
//                            log.info("Form 33 Jasper Report Generated");
//                            Form33DTOJasper form33 = jdbcService.getForm33(formId).convertDTOForJasper(rcForm.getComplaintNumber());
//
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form33));
//                            return report;
//                        }
//                        case 3 -> {
//                            log.info("Form 3 Jasper Report Generated");
//                            OtherFormJasperDTO form3 = jdbcService.getForm3(rcForm.getId()).convertDTOToJasper3(rcForm.getComplaintNumber());
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form3));
//                            return report;
//                        }
//                        case 8 -> {
//                            log.info("Form 8 Jasper Report Generated");
//                            OtherFormJasperDTO form8 = jdbcService.getForm8(rcForm.getId()).convertDTOToJasper8(rcForm.getComplaintNumber());
//
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form8));
//                            return report;
//
//                        }
//                        case 9 -> {
//                            log.info("Form 9 Jasper Report Generated");
//                            OtherFormJasperDTO form9 = jdbcService.getForm9(rcForm.getId()).convertDTOToJasper9(rcForm.getComplaintNumber());
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form9));
//                            return report;
//                        }
//                        case 11 -> {
//                            log.info("Form 11 Jasper Report Generated");
//                            String formNumber = "11";
//                            OtherFormJasperDTO form11 = jdbcService.getOtherForm(rcForm.getId(), formNumber).convertDTOToJasper11(rcForm.getComplaintNumber());
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form11));
//                            return report;
//                        }
//                        case 12 -> {
//                            log.info("Form 12 Jasper Report Generated");
//                            String formNumber = "12";
//                            OtherFormJasperDTO form12 = jdbcService.getOtherForm(rcForm.getId(), formNumber).convertDTOToJasper12(rcForm.getComplaintNumber());
//
//                            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(form12));
//                            return report;
//                        }
//                        default -> {
//                        }
//                    }
//                }
//
//            }
//
//            return null;
//        }  catch (Exception e) {
//            log.error("Error generating jasper report " + e.getMessage());
//            return null;
//        }
//    }
//
//
//    /**
//     * This method generates a pdf sample for Form 10
//     *
//     * @param complaintId - Complaint Id
//     * @param magistrateId
//     * @return ResponseDTO - custom response object
//     * @author Ebenezer N.
//     * @createdAt 21st Dec 2023
//     */
//    @Override
//    public ResponseDTO generateForm10Sample(UUID complaintId, UUID magistrateId) {
//        ResponseDTO responseDTO;
//
//        try {
//            if(!isNotNullOrEmpty(complaintId)){
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complaint id cannot be null");
//            }
//            var form7 = form7Repo.findRcClientForm7ByComplaintId(complaintId);
//
//            if(form7.isEmpty()){
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complaint form does not exist");
//            }
//
//            var magistrate = jdbcService.getMagistrateById(magistrateId);
//
//            var unit = jdbcService.getUnitById(form7.get().getUnitId());
//
//            if(unit.isEmpty() || Boolean.FALSE.equals(unit.get().getIsActive())){
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit does not exist or is not operational");
//            }
//
//            if (magistrate.isEmpty()) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Magistrate does not exist");
//            }
//
//            Map<String, Object> jasperParameters = new HashMap<>();
//            jasperParameters.put("rentLogo", storageService.loadImageFile("rentcontrollogo_png"));
//
//            var jasperFileName = storageService.getJasperTemplate(10);
//
//            log.info("Form 10 Jasper Generating");
//            OtherFormJasperDTO dto = form7.get().toOtherFormJasperDTO();
//
//            dto.setUnitName(unit.get().getName());
//            dto.setUnitAddress(unit.get().getAddress());
//            dto.setUnitLocation(unit.get().getLocation());
//            dto.setMagistrateAddress(magistrate.get().getAddress());
//            dto.setMagistrateLocation(magistrate.get().getLocation());
//
//            byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(dto));
//
//            responseDTO = getResponseDto("success", HttpStatus.OK, report);
//
//        }  catch (ResponseStatusException e) {
//            log.error(e.getReason());
//            responseDTO = getResponseDto(e.getReason(), HttpStatus.valueOf(e.getStatusCode().value()));
//        }  catch (Exception e) {
//            log.error("Error generating form 10 jasper " + e.getMessage());
//            responseDTO = getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return responseDTO;
//    }
//
//    /**
//     * This method generates a pdf object for a submitted form ID. It returns a
//     * Response Object with byte data, message, http status code and
//     * ZonedDateTime
//     *
//     * @param id - String Client Form ID
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO getFormReportByIdAndNumber(String id, Integer formNumber) {
//        Map<String, Object> jasperParameters = new HashMap<>();
//        jasperParameters.put("rentLogo", storageService.loadImageFile("rentcontrollogo_png"));
//        ResponseDTO responseDTO;
//        try {
//            UUID formId = UUID.fromString(id);
//
//            if(!isNotNullOrEmpty(formNumber)){
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Form Number is invalid");
//            }
//
//            /** Fetching assessment forms */
//            var res = switch (formNumber){
//                case 1 -> {
//                    var form1 = form1Repository.findById(formId);
//                    if (form1.isPresent()) {
//                        Form1JasperDTO form1JasperDTO = new Form1JasperDTO();
//                        form1JasperDTO = form1JasperDTO.convertToForm1DTO(form1.get());
//                        var jasperFileName = storageService.getJasperTemplate(formNumber);
//
//                        byte[] report = pdfService.generatePdfView(jasperFileName,
//                                jasperParameters, Arrays.asList(form1JasperDTO));
//
//                        yield report;
//                    }
//                    yield null;
//                }
//                case 5 -> {
//                    var form5 = form5Repository.findById(formId);
//                    log.info("Form Data:->>>{}", form5);
//                    if(form5.isPresent()){
//                        Form5JasperDTO form5JasperDTO = new Form5JasperDTO();
//                        form5JasperDTO = form5JasperDTO.toDTO(form5.get());
//                        log.info("Form5Jasper DTO Data:->>>{}", form5JasperDTO);
//                        var jasperFileName = storageService.getJasperTemplate(formNumber);
//                        byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters,
//                                Arrays.asList(form5JasperDTO));
//                        yield  report;
//                    }
//                    yield null;
//                }
//                case 32 -> {
//
//                    var witness = witnessRepo.findById(UUID.fromString(id));
//                    if(witness.isEmpty()){
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Witness does not exist");
//                    }
//
//                    var form7 = form7Repo.findRcClientForm7ByComplaintId(witness.get().getComplaintId());
//                    if(!isNotNullOrEmpty(form7)){
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complaint form does not exist");
//                    }
//
//                    var unit = jdbcService.getUnitById(form7.get().getUnitId());
//
//                    if(unit.isEmpty() || Boolean.FALSE.equals(unit.get().getIsActive())){
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit does not exist or is not operational");
//                    }
//                    var dto = form7.get().toOtherFormJasperDTO();
//                    var form33 = form33Repo.findByComplaintId(witness.get().getComplaintId());
//                    if(form33.isEmpty()){
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complaint has not been scheduled");
//                    }
//
//                    var jasperFileName = storageService.getJasperTemplate(32);
//
//                    log.info("Form 32 Jasper Generating");
//
//                    dto.setAppointmentDate(toLocalDate(form33.get().getAppointmentDate()));
//                    dto.setWitnessName(witness.get().getName());
//                    dto.setWitnessPhoneNumber(witness.get().getPhoneNumber());
//                    dto.setUnitName(unit.get().getName());
//                    dto.setUnitAddress(unit.get().getAddress());
//                    dto.setUnitLocation(unit.get().getLocation());
//                    dto.setHearingTime(LocalTime.of(9,0));
//
//                    byte[] report = pdfService.generatePdfView(jasperFileName, jasperParameters, Arrays.asList(dto));
//                    yield report;
//                }
//                default -> getFormReportById(id);
//            };
//
//            if(res == null){
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not load data for form");
//            }
//            responseDTO = getResponseDto("success", HttpStatus.OK, res);
//
//        }  catch (ResponseStatusException e) {
//            log.error(e.getReason());
//            responseDTO = getResponseDto(e.getReason(), HttpStatus.valueOf(e.getStatusCode().value()));
//        }  catch (Exception e) {
//            log.error("Error generating jasper report " + e.getMessage());
//            responseDTO = getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return responseDTO;
//    }
//
//    /**
//     * This method saves witness for a form 33 It returns a Response Object with
//     * a list of, message, http status code and ZonedDateTime
//     *
//     * @param witnessDTO - custom form 33 Object
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO storeForm32Witness(List<FormWitnessDTO> witnessDTO) {
//
//        if (witnessDTO == null || witnessDTO.isEmpty()) {
//
//            return ResponseDTO.builder()
//                    .message("WitnessDTO cannot empty")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//            List<RcClientWitness> rcClientWitness = new ArrayList<>();
//
//            for (FormWitnessDTO witnesses : witnessDTO) {
//
//                if (witnesses.getComplaintId() == null || (witnesses.getName() == null || witnesses.getName().isEmpty())) {
//
//                    return ResponseDTO.builder()
//                            .message("Witness name and complaint ID must be populated.")
//                            .statusCode(400)
//                            .data("error")
//                            .date(ZonedDateTime.now())
//                            .build();
//
//                }
//
//                rcClientWitness.add(witnesses.convertDTOToWitness(witnesses));
//
//            }
//
//            if (!rcClientWitness.isEmpty()) {
//
//                List<RcClientWitness> savedWitness = witnessRepo.saveAll(rcClientWitness);
//
//                return ResponseDTO.builder()
//                        .message("true")
//                        .statusCode(201)
//                        .data(savedWitness)
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else {
//
//                return ResponseDTO.builder()
//                        .message("Error saving new witness")
//                        .statusCode(400)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//        } catch (Exception e) {
//
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//
//    }
//
//    @Override
//    public ResponseDTO getForm33WitnessByClientId(String formId, String clientId) {
//        if (formId == null || formId.isEmpty()) {
//
//            return ResponseDTO.builder()
//                    .message("Form Id cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//        try {
//
//            Form rcForm = formRepo.findFormById(UUID.fromString(formId));
//
//            if (rcForm == null) {
//
//                return ResponseDTO.builder()
//                        .message("No data found for submitted form Id: " + formId)
//                        .statusCode(404)
//                        .data("No data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//            Form33DTO form33 = jdbcService.getForm33ByClientFormId(rcForm.getId());
//            if (form33 == null) {
//
//                return ResponseDTO.builder()
//                        .message("No current form 33 data found  ")
//                        .statusCode(404)
//                        .data("No data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else if (form33.getCreatedAt() == null) {
//                return ResponseDTO.builder()
//                        .message("Empty created date found for current form 33.")
//                        .statusCode(404)
//                        .data("Invalid Data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//            List<RcClientWitness> witness = witnessRepo.findWitnessByClientID(UUID.fromString(clientId), form33.getCreatedAt());
//
//            List<FormWitnessDTO> rcClientWitness = new ArrayList<>();
////SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
////            Date newAppointDT = sdf.parse(form33.getAppointmentDate());
//            for (RcClientWitness oneWitness : witness) {
//
//                FormWitnessDTO witnessDTO = FormWitnessDTO.builder()
//                        .name(oneWitness.getName())
//                        .phoneNumber(oneWitness.getPhoneNumber())
//                        .clientFormId(form33.getId())
//                        .complainantId(form33.getComplainantId())
//                        .complainantName(form33.getComplainantName())
//                        .complaintId(form33.getComplaintId())
//                        .complaintNumber(form33.getComplaintNumber())
//                        .isCurrent(form33.isCurrent())
//                        .offenceAndClaim(form33.getOffenceAndClaim())
//                        .respondentId(form33.getRespondentId())
//                        .respondentName(form33.getRespondentName())
//                        .respondentAddress(form33.getRespondentAddress())
//                        .unitId(form33.getUnitId())
//                        .districtId(form33.getDistrictId())
//                        .unitName(form33.getUnitName())
//                        .unitAddress(form33.getUnitAddress())
//                        .unitLocation(form33.getUnitLocation())
//                        .regionName(form33.getRegionName())
//                        .districtName(form33.getDistrictName())
//                        .regionId(form33.getRegionId())
//                        .appointmentDate(form33.getAppointmentDate())
//                        .hearingTime(form33.getHearingTime())
//                        .createdAt(oneWitness.getCreatedAt())
//                        .createdBy(oneWitness.getCreatedBy())
//                        .updatedAt(oneWitness.getUpdatedAt())
//                        .updatedBy(oneWitness.getUpdatedBy())
//                        .build();
//                rcClientWitness.add(witnessDTO);
//
//            }
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(rcClientWitness)
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//            return ResponseDTO.builder()
//                    .message(e.getMessage())
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//
//    }
//
//    /**
//     * This method fetches all Witness for a submited form 33.It returns a
//     * Response Object with a list of witnesses, message, http status code and
//     * ZonedDateTime
//     *
//     * @param formId - Form 33 ID
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO getForm33WitnessByComplaintId(String formId) {
//        if (formId == null || formId.isEmpty()) {
//
//            return ResponseDTO.builder()
//                    .message("Form Id cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//
//            Form rcForm = formRepo.findFormById(UUID.fromString(formId));
//
//            if (rcForm == null) {
//
//                return ResponseDTO.builder()
//                        .message("No data found for submitted form Id: " + formId)
//                        .statusCode(404)
//                        .data("No data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//            if (rcForm.getComplaintId() == null) {
//                return ResponseDTO.builder()
//                        .message("Complaint ID missing for selected form")
//                        .statusCode(404)
//                        .data("No data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//            }
//
//            Form33DTO form33 = jdbcService.getForm33ByClientFormId(rcForm.getId());
//
//            if (form33 == null) {
//
//                return ResponseDTO.builder()
//                        .message("No current form 33 data found  ")
//                        .statusCode(404)
//                        .data("No data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else if (form33.getCreatedAt() == null) {
//                return ResponseDTO.builder()
//                        .message("Empty created date found for current form 33.")
//                        .statusCode(404)
//                        .data("Invalid Data found")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//            List<RcClientWitness> witness = witnessRepo.findWitnessByComplaintId(rcForm.getComplaintId(), form33.getCreatedAt());
//
//            List<FormWitnessDTO> rcClientWitness = new ArrayList<>();
////            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
////            Date newAppointDT = sdf.parse(form33.getAppointmentDate());
//            for (RcClientWitness oneWitness : witness) {
//
//                FormWitnessDTO witnessDTO = FormWitnessDTO.builder()
//                        .name(oneWitness.getName())
//                        .phoneNumber(oneWitness.getPhoneNumber())
//                        .clientFormId(form33.getId())
//                        .complainantId(form33.getComplainantId())
//                        .complainantName(form33.getComplainantName())
//                        .complaintId(form33.getComplaintId())
//                        .complaintNumber(form33.getComplaintNumber())
//                        .isCurrent(form33.isCurrent())
//                        .offenceAndClaim(form33.getOffenceAndClaim())
//                        .respondentId(form33.getRespondentId())
//                        .respondentName(form33.getRespondentName())
//                        .respondentAddress(form33.getRespondentAddress())
//                        .unitId(form33.getUnitId())
//                        .districtId(form33.getDistrictId())
//                        .unitName(form33.getUnitName())
//                        .unitAddress(form33.getUnitAddress())
//                        .unitLocation(form33.getUnitLocation())
//                        .regionName(form33.getRegionName())
//                        .districtName(form33.getDistrictName())
//                        .regionId(form33.getRegionId())
//                        .appointmentDate(form33.getAppointmentDate())
//                        .hearingTime(form33.getHearingTime())
//                        .createdAt(form33.getCreatedAt())
//                        .createdAt(oneWitness.getCreatedAt())
//                        .createdBy(oneWitness.getCreatedBy())
//                        .updatedAt(oneWitness.getUpdatedAt())
//                        .updatedBy(oneWitness.getUpdatedBy())
//                        .build();
//                rcClientWitness.add(witnessDTO);
//
//            }
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(rcClientWitness)
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        } catch (Exception e) {
//            log.info("Error saving new form" + e.getMessage());
//            return ResponseDTO.builder()
//                    .message("Error fetching form 33")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//
//    }
//
//    @Override
//    public ResponseDTO getWitnessFormbyID(String id) {
//        if (id == null || id.isEmpty()) {
//
//            return ResponseDTO.builder()
//                    .message("Witness Id cannot null")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//        try {
//
//            Optional<RcClientWitness> witness = witnessRepo.findById(UUID.fromString(id));
//
//            if (witness.isEmpty() || !witness.isPresent()) {
//
//                return ResponseDTO.builder()
//                        .message("No Witness found for ID: " + id)
//                        .statusCode(404)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            } else if (witness.get().getComplaintId() == null) {
//
//                return ResponseDTO.builder()
//                        .message("Missing complaint ID for Witness with ID: " + id)
//                        .statusCode(404)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//            var form33 = form33Repo.findByComplaintId(witness.get().getComplaintId())
//                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Form 33 does not exist"));
//
//            RcClientWitness oneWitness = witness.get();
////            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
////            Date newAppointDT;
////
////            newAppointDT = sdf.parse(form33.getAppointmentDate());
//
//            FormWitnessDTO witnessDTO = FormWitnessDTO.builder()
//                    .name(oneWitness.getName())
//                    .phoneNumber(oneWitness.getPhoneNumber())
//                    .clientFormId(form33.getId())
//                    .complainantId(form33.getComplainantId())
//                    .complainantName(form33.getComplainantName())
//                    .complaintId(form33.getComplaintId())
////                    .complaintNumber()
//                    .isCurrent(form33.isCurrent())
//                    .offenceAndClaim(form33.getOffenceAndClaim())
//                    .respondentId(form33.getRespondentId())
//                    .respondentName(form33.getRespondentName())
//                    .respondentAddress(form33.getRespondentAddress())
//                    .unitId(form33.getUnitId())
//                    .districtId(form33.getDistrictId())
//                    .unitName(form33.getUnitName())
//                    .unitAddress(form33.getUnitAddress())
//                    .unitLocation(form33.getUnitLocation())
//                    .regionName(form33.getRegionName())
//                    .districtName(form33.getDistrictName())
//                    .regionId(form33.getRegionId())
//                    .appointmentDate(form33.getAppointmentDate())
//                    .hearingTime(form33.getHearingTime())
//                    .createdAt(form33.getCreatedAt())
//                    .createdAt(oneWitness.getCreatedAt())
//                    .createdBy(oneWitness.getCreatedBy())
//                    .updatedAt(oneWitness.getUpdatedAt())
//                    .updatedBy(oneWitness.getUpdatedBy())
//                    .build();
//
//            return ResponseDTO.builder()
//                    .message("Success")
//                    .statusCode(200)
//                    .data(witnessDTO)
//                    .date(ZonedDateTime.now())
//                    .build();
//        } catch (Exception e) {
//            log.info("Error saving new form" + e.getMessage());
//
//            return ResponseDTO.builder()
//                    .message("Error saving form")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//        }
//
//    }
//
//    /**
//     * This method creates a new Form 8,9, 11,12 record in the form-service
//     * database.It returns a Response Object with the saved Form Object,
//     * message, http status code and ZonedDateTime
//     *
//     * @param formDTO - Form Object
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO storeOtherForm(OtherFormDTO formDTO) {
//        if (formDTO == null) {
//
//            return ResponseDTO.builder()
//                    .message("DTO cannot empty")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//            RcSetupFormType formTypeDT = rcSetupFormTypeRepo.findRcSetupFormTypeByformNumber(formDTO.getFormNumber());
//
//            if (formTypeDT != null && formTypeDT.getId() != null) {
//                formDTO.setFormTypeId(formTypeDT.getId());
//            }
//
//            Boolean startcomplaints = false;
//
//            formDTO.setStartsComplaint(startcomplaints);
//
//            Form newForm = formRepo.save(formDTO.convertDTOToForm(formDTO));
//
//            if (newForm != null && newForm.getId() != null) {
//                if (null != formDTO.getFormNumber()) {
//                    switch (formDTO.getFormNumber()) {
//                        case 8 -> {
//                            if (formDTO.getNoticeDate() == null) {
//
//                                return ResponseDTO.builder()
//                                        .message("Notice date cannot empty")
//                                        .statusCode(404)
//                                        .data("error")
//                                        .date(ZonedDateTime.now())
//                                        .build();
//                            }
//                            formDTO.setId(newForm.getId());
//                            RcClientForm8 form8 = form8Repo.save(formDTO.convertDTOToForm8(formDTO));
//                            newForm.setFormData(form8);
//                        }
//                        case 9 -> {
//                            formDTO.setId(newForm.getId());
//                            RcClientForm9 form9 = form9Repo.save(formDTO.convertDTOToForm9(formDTO));
//                            newForm.setFormData(form9);
//                        }
//                        case 3 -> {
//                            if (formDTO.getNoticeDate() == null) {
//
//                                return ResponseDTO.builder()
//                                        .message("Notice date cannot empty")
//                                        .statusCode(404)
//                                        .data("error")
//                                        .date(ZonedDateTime.now())
//                                        .build();
//                            }
//                            formDTO.setId(newForm.getId());
//                            RcClientForm3 form3 = form3Repo.save(formDTO.convertDTOToForm3(formDTO));
//                            newForm.setFormData(form3);
//                        }
//
//                        case 11 -> {
//                            formDTO.setId(newForm.getId());
//                            RcClientForm11 form11 = form11Repo.save(formDTO.convertDTOToForm11(formDTO));
//                            newForm.setFormData(form11);
//                        }
//
//                        case 12 -> {
//                            if (formDTO.getDecisionDate() == null) {
//
//
//                                return ResponseDTO.builder()
//                                        .message("Decision date cannot empty")
//                                        .statusCode(404)
//                                        .data("error")
//                                        .date(ZonedDateTime.now())
//                                        .build();
//                            }
//                            formDTO.setId(newForm.getId());
//                            RcClientForm12 form12 = form12Repo.save(formDTO.convertDTOToForm12(formDTO));
//                            newForm.setFormData(form12);
//                        }
//                        default -> {
//                            return ResponseDTO.builder()
//                                    .message("Invalid or no form number provided")
//                                    .statusCode(404)
//                                    .data("error")
//                                    .date(ZonedDateTime.now())
//                                    .build();
//                        }
//                    }
//                }
//
//                return ResponseDTO.builder()
//                        .message("true")
//                        .statusCode(201)
//                        .data(newForm)
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else {
//                return ResponseDTO.builder()
//                        .message("Error saving new form")
//                        .statusCode(400)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//        } catch (Exception e) {
//            log.info("Error saving new form" + e.getMessage());
//
//            return ResponseDTO.builder()
//                    .message("Error saving form")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//    }
//
//    /**
//     * This method creates a new Form 10 record in the form-service database.It
//     * returns a Response Object with the saved Form 10 Object, message, http
//     * status code and ZonedDateTime
//     *
//     * @param formDTO - Form10 Object
//     * @return ResponseDTO - custom response object
//     * @Author : Kwaku Afreh-Nuamah
//     */
//    @Override
//    public ResponseDTO storeForm10(Form10DTO formDTO) {
//        if (formDTO == null) {
//
//            return ResponseDTO.builder()
//                    .message("Form10DTO cannot empty")
//                    .statusCode(404)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//
//        try {
//            RcSetupFormType formTypeDT = rcSetupFormTypeRepo.findRcSetupFormTypeByformNumber(formDTO.getFormNumber());
//
//            if (formTypeDT != null && formTypeDT.getId() != null) {
//                formDTO.setFormTypeId(formTypeDT.getId());
//            }
//
//            Boolean startcomplaints = false;
//
//            formDTO.setStartsComplaint(startcomplaints);
//
//            Form newForm = formRepo.save(formDTO.convertDTOToForm(formDTO));
//
//            if (newForm != null && newForm.getId() != null) {
//                if (null != formDTO.getFormNumber()) {
//                    switch (formDTO.getFormNumber()) {
//                        case 10 -> {
//                            formDTO.setId(newForm.getId());
//                            RcClientForm10 form10 = form10Repo.save(formDTO.convertDTOToForm10(formDTO));
//                            newForm.setFormData(form10);
//                        }
//                        default -> {
//                            return ResponseDTO.builder()
//                                    .message("Invalid Form Number Provided")
//                                    .statusCode(404)
//                                    .data("error")
//                                    .date(ZonedDateTime.now())
//                                    .build();
//                        }
//                    }
//                }
//
//                return ResponseDTO.builder()
//                        .message("true")
//                        .statusCode(201)
//                        .data(newForm)
//                        .date(ZonedDateTime.now())
//                        .build();
//            } else {
//                return ResponseDTO.builder()
//                        .message("Error saving new form")
//                        .statusCode(400)
//                        .data("error")
//                        .date(ZonedDateTime.now())
//                        .build();
//
//            }
//
//        } catch (Exception e) {
//            log.info("Error saving new form" + e.getMessage());
//
//            return ResponseDTO.builder()
//                    .message("Error saving form")
//                    .statusCode(400)
//                    .data("error")
//                    .date(ZonedDateTime.now())
//                    .build();
//
//        }
//    }
//
//}