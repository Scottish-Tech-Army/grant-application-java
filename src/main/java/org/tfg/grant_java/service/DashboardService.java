package org.tfg.grant_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.grant_java.dto.DashboardRequestDto;
import org.tfg.grant_java.dto.DashboardResponseDto;
import org.tfg.grant_java.dto.RecentApplicationsDto;
import org.tfg.grant_java.entity.Applications;
import org.tfg.grant_java.repository.ApplicationRepository;

import java.util.List;

@Service
public class DashboardService {

    @Autowired
    ApplicationRepository applicationRepository;


    public DashboardResponseDto getDashboard(DashboardRequestDto dashboardRequestDto) {
        DashboardResponseDto dashboardResponseDto = new DashboardResponseDto();
        List<Object[]> result = applicationRepository.countByStatusAndTenant(dashboardRequestDto.getTenantId());

        for (Object[] row : result) {
            String status = (String) row[0];
            Long count = ((Number) row[1]).longValue();

            if("Drafted".equalsIgnoreCase(status)) {
                dashboardResponseDto.setDrafted(count.intValue());
            }
            else if("Submitted".equalsIgnoreCase(status)) {
                dashboardResponseDto.setSubmitted(count.intValue());
            }

            dashboardResponseDto.addTotal(count.intValue());
        }

        List<Applications> applications = applicationRepository.findTop5ByOrderByCreatedAtDesc();
        for (Applications application : applications) {
            RecentApplicationsDto recentApplicationsDto = new RecentApplicationsDto();
            recentApplicationsDto.setApplicationId(application.getApplicationId());
            recentApplicationsDto.setName(application.getName());
            recentApplicationsDto.setFunderName(application.getFunderName());
            recentApplicationsDto.setStatus(application.getStatus());
            recentApplicationsDto.setCreatedAt(application.getCreatedAt());
            dashboardResponseDto.getRecentApplications().add(recentApplicationsDto);
        }

        return dashboardResponseDto;
    }
}
