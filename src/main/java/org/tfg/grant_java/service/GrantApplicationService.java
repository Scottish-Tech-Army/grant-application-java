package org.tfg.grant_java.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.*;
import org.tfg.grant_java.entity.*;
import org.tfg.grant_java.repository.ApplicationQuestionAnswerRepository;
import org.tfg.grant_java.repository.ApplicationRepository;
import org.tfg.grant_java.repository.QuestionRepository;
import org.tfg.grant_java.repository.TenantRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GrantApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ApplicationQuestionAnswerRepository applicationQuestionAnswerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public boolean deleteApplication(String applicationId) {
        // Implement the logic to delete the grant application with the given ID
        // This is a placeholder implementation and should be replaced with actual logic
        try {
            System.out.println("Deleting grant application with ID: " + applicationId);
            Optional<Applications> optionalApplications = applicationRepository.findById(UUID.fromString(applicationId));
            optionalApplications.ifPresent(application -> {
                application.setStatus("DELETED");
                applicationRepository.save(application);
            });
            return true;
        } catch (Exception e) {
            System.out.println("Exception Occured while updating application status as DELETED : " + e);
            return false;
        }// Return true if deletion was successful, false otherwise
    }

    public ApplicationDetailsDTO getApplicationDetails(String applicationId) {
        // Implement the logic to retrieve the details of the grant application with the given ID
        // This is a placeholder implementation and should be replaced with actual logic
        ApplicationDetailsDTO applicationDetailsDTO;
        try {
            System.out.println("Retrieving details for grant application with ID: " + applicationId);
            Optional<Applications> optionalApplications = applicationRepository.findById(UUID.fromString(applicationId));
            Optional<List<ApplicationQuestionAnswers>> optionalQuestionAnswers = applicationQuestionAnswerRepository.findByApplicationId(UUID.fromString(applicationId));
            if (optionalApplications.isPresent() && optionalQuestionAnswers.isPresent()) {
                Applications application = optionalApplications.get();
                List<ApplicationQuestionAnswers> applicationQuestionAnswersList = optionalQuestionAnswers.get();
                List<Questions> questionsList = questionService.getQuestionsByTenantId(application.getTenant().getTenantId());
                List<UUID> questionIdsList = questionsList.stream().map(Questions::getQuestionId).toList();
//                List<ApplicationQADetailsDTO> applicationQADetailsDTOSList = applicationQuestionAnswersList.stream()
//                        .map(applicationQuestionAnswers -> ApplicationQADetailsDTO.builder()
//                                .question(applicationQuestionAnswers.getQuestionText())
//                                .answer(applicationQuestionAnswers.getAnswerText())
//                                .section(applicationQuestionAnswers.getSection())
//                                .questionId(applicationQuestionAnswers.getQuestion().getQuestionId())
//                                .checked("COMMON".equalsIgnoreCase(applicationQuestionAnswers.getSection()) && questionIdsList.contains(applicationQuestionAnswers.getQuestion().getQuestionId()))
//                                .build()).collect(Collectors.toList());
//                applicationQADetailsDTOSList = questionsList.stream().map(x ->  ApplicationQADetailsDTO.builder().questionId(x.getQuestionId()).question(x.getQuestionText()).answer(x).build())
                Map<UUID, ApplicationQuestionAnswers> answerMap = applicationQuestionAnswersList.stream()
                        .collect(Collectors.toMap(ApplicationQuestionAnswers::getQuestionId, a -> a));

// Set of questionIds that have answers
                Set<UUID> answeredQuestionIds = answerMap.keySet();

// 1. Add answered questions with checked = true
                List<ApplicationQADetailsDTO> applicationQADetailsDTOSList = applicationQuestionAnswersList.stream()
                        .filter(a -> questionIdsList.contains(a.getQuestionId()))
                        .map(a -> ApplicationQADetailsDTO.builder()
                                .questionId(a.getQuestionId())
                                .question(a.getQuestionText())
                                .answer(a.getAnswerText())
                                .section(a.getSection())
                                .checked(true)
                                .build())
                        .collect(Collectors.toList());

// 2. Add unanswered questions with checked = false
                applicationQADetailsDTOSList.addAll(
                        questionsList.stream()
                                .filter(q -> !answeredQuestionIds.contains(q.getQuestionId()))
                                .map(q -> ApplicationQADetailsDTO.builder()
                                        .questionId(q.getQuestionId())
                                        .question(q.getQuestionText())
                                        .answer(q.getDefaultAnswerText())
                                        .section("COMMON") // or null if not present
                                        .checked(false)
                                        .build())
                                .collect(Collectors.toList())
                );

                applicationDetailsDTO = ApplicationDetailsDTO.builder()
                        .applicationId(application.getApplicationId())
                        .userId(application.getCreatedBy())
                        .applicationName(application.getName())
                        .funderName(application.getFunderName())
                        .tenantId(application.getTenant().getTenantId())
                        .status(application.getStatus())
                        .applicationQADetails(applicationQADetailsDTOSList)
                        .build();
            } else {
                System.out.println("Grant application not found with ID: " + applicationId);
                return null; // Return null or throw an exception if the application is not found
            }
        } catch (Exception e) {
            System.out.println("Exception Occured while fetching application details : " + e);
            return null;
        }
        return applicationDetailsDTO;
    }


    @Transactional
    public ApplicationCreationResponse createOrUpdateApplication(ApplicationDetailsDTO applicationDetailsDTO) {
        UUID applicationId = UUID.randomUUID();
        Optional<Tenants> tenants = tenantRepository.findById(applicationDetailsDTO.getTenantId());
        try {
            if (applicationDetailsDTO.getApplicationId() != null) {
                // Update existing application
                Optional<Applications> optionalApplications = applicationRepository.findById(applicationDetailsDTO.getApplicationId());
                if (!tenants.isPresent() || !optionalApplications.isPresent())
                    return null;
                Applications applications = optionalApplications.get();
                Applications updatedApplciations = Applications.builder().tenant(tenants.get()).applicationId(applications.getApplicationId()).createdBy(applicationDetailsDTO.getUserId()).createdAt(Instant.now()).funderName(applicationDetailsDTO.getFunderName()).name(applicationDetailsDTO.getApplicationName()).status(applicationDetailsDTO.getStatus()).build();
                applicationRepository.save(updatedApplciations);
                applicationQuestionAnswerRepository.deleteByApplicationId(applicationDetailsDTO.getApplicationId());
                List<ApplicationQuestionAnswers> applicationQuestionAnswersList = applicationDetailsDTO.getApplicationQADetails().stream().map(applicationQADetailsDTO -> ApplicationQuestionAnswers.builder().applicationId(updatedApplciations.getApplicationId()).questionText(applicationQADetailsDTO.getQuestion()).answerText(applicationQADetailsDTO.getAnswer()).section(applicationQADetailsDTO.getSection()).questionId(applicationQADetailsDTO.getQuestionId()).build()).toList();
                applicationQuestionAnswerRepository.saveAll(applicationQuestionAnswersList);
            } else {
                // create new Application
                Applications applications = Applications.builder().applicationId(applicationId).status(applicationDetailsDTO.getStatus()).tenant(tenantRepository.findById(applicationDetailsDTO.getTenantId()).get()).createdBy(applicationDetailsDTO.getUserId()).createdAt(Instant.now()).funderName(applicationDetailsDTO.getFunderName()).name(applicationDetailsDTO.getApplicationName()).build();
                applicationRepository.save(applications);
                List<ApplicationQuestionAnswers> applicationQuestionAnswersList = applicationDetailsDTO.getApplicationQADetails().stream()
                        .map(applicationQADetailsDTO -> ApplicationQuestionAnswers.builder().applicationId(applications.getApplicationId())
                                .questionText(applicationQADetailsDTO.getQuestion()).answerText(applicationQADetailsDTO.getAnswer()).
                                section(applicationQADetailsDTO.getSection()).questionId(applicationQADetailsDTO.getQuestionId())
                                .build()).toList();
                //questionRepository.findById(applicationQADetailsDTO.getQuestionId()).orElse(Questions.builder().questionId(applicationQADetailsDTO.getQuestionId()).questionText(applicationQADetailsDTO.getQuestion()).tenant(tenants.get()).createdAt(Instant.now()).build())

                applicationQuestionAnswerRepository.saveAll(applicationQuestionAnswersList);
            }
        } catch (Exception e) {
            System.out.println("Exception Occured while creating/updating application : " + e);
            return null;
        }
        return ApplicationCreationResponse.builder().status(applicationDetailsDTO.getStatus()).applicationId(applicationDetailsDTO.getApplicationId() != null ? applicationDetailsDTO.getApplicationId() : applicationId).build();
    }

    public Page<List<Applications>> getApplicationsByTenant(GrantApplicationsRequestDTO grantApplicationsRequestDTO) {
        Pageable pageable;
        if (grantApplicationsRequestDTO.getPageSize() <= 0) {
            pageable = Pageable.unpaged();
            return applicationRepository.findByTenant_TenantId(UUID.fromString(grantApplicationsRequestDTO.getTenantId()), pageable);
        } else {
            pageable = PageRequest.of(
                    grantApplicationsRequestDTO.getPageNumber(),
                    grantApplicationsRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
            return applicationRepository.findByTenant_TenantIdAndStatusNot(UUID.fromString(grantApplicationsRequestDTO.getTenantId()), "DELETED",pageable);
        }

    }

}
