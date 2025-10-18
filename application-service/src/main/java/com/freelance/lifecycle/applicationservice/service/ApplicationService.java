package com.freelance.lifecycle.applicationservice.service;

import com.freelance.lifecycle.applicationservice.dto.ApplicationRequestDTO;
import com.freelance.lifecycle.applicationservice.dto.ApplicationResponseDTO;
import com.freelance.lifecycle.applicationservice.dto.StatusUpdateDTO;

import java.util.List;

public interface ApplicationService {

    ApplicationResponseDTO createApplication(ApplicationRequestDTO dto);

    ApplicationResponseDTO updateStatus(Long id, StatusUpdateDTO dto);

    List<ApplicationResponseDTO> getApplicationsByProjectId(Long projectId);

    List<ApplicationResponseDTO> getApplicationsByFreelancerId(Long freelancerId);
}
