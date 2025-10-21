package com.freelance.lifecycle.applicationservice.service;

import com.freelance.lifecycle.applicationservice.dto.ApplicationRequestDTO;
import com.freelance.lifecycle.applicationservice.dto.ApplicationResponseDTO;
import com.freelance.lifecycle.applicationservice.dto.StatusUpdateDTO;
import com.freelance.lifecycle.applicationservice.exception.ApplicationNotFoundException;
import com.freelance.lifecycle.applicationservice.exception.InvalidStatusException;
import com.freelance.lifecycle.applicationservice.model.Application;
import com.freelance.lifecycle.applicationservice.model.ApplicationStatus;
import com.freelance.lifecycle.applicationservice.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.freelance.lifecycle.applicationservice.messaging.NotificationEventDTO;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ApplicationResponseDTO createApplication(ApplicationRequestDTO dto) {
        Application application = new Application();
        application.setProjectId(dto.getProjectId());
        application.setFreelancerId(dto.getFreelancerId());
        application.setMessage(dto.getMessage());
        application.setStatus(ApplicationStatus.PENDING);
        
        // Note: employerId should be retrieved from the project service
        // For now, we'll set it to null or a default value
        application.setEmployerId(1L); // This should be retrieved from project service
        
        Application savedApplication = applicationRepository.save(application);

        // Publish notification event (async)
        try {
            NotificationEventDTO event = new NotificationEventDTO(savedApplication.getEmployerId(),
                    "New application from freelancer " + savedApplication.getFreelancerId() + " for project " + savedApplication.getProjectId(),
                    "NEW_APPLICATION");
            // Send to default exchange with routing key = queue name so it is routed directly to the queue
            rabbitTemplate.convertAndSend("", "notifications.queue", event);
        } catch (Exception e) {
            // Log and continue; application creation should not fail because of notification issues
            // Using System.out for simplicity; real code should use a logger
            System.err.println("Failed to publish notification event: " + e.getMessage());
        }

        return mapToResponseDTO(savedApplication);
    }

    @Override
    public ApplicationResponseDTO updateStatus(Long id, StatusUpdateDTO dto) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with id: " + id));

        ApplicationStatus newStatus = dto.getStatus();
        
        // Validate status transition
        validateStatusTransition(application.getStatus(), newStatus);
        
        application.setStatus(newStatus);
        Application updatedApplication = applicationRepository.save(application);
        
        return mapToResponseDTO(updatedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponseDTO> getApplicationsByProjectId(Long projectId) {
        List<Application> applications = applicationRepository.findByProjectId(projectId);
        return applications.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponseDTO> getApplicationsByFreelancerId(Long freelancerId) {
        List<Application> applications = applicationRepository.findByFreelancerId(freelancerId);
        return applications.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private void validateStatusTransition(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        // Define valid status transitions
        switch (currentStatus) {
            case PENDING:
                if (newStatus != ApplicationStatus.VIEWED && 
                    newStatus != ApplicationStatus.ACCEPTED && 
                    newStatus != ApplicationStatus.REJECTED) {
                    throw new InvalidStatusException("Invalid status transition from PENDING to " + newStatus);
                }
                break;
            case VIEWED:
                if (newStatus != ApplicationStatus.ACCEPTED && 
                    newStatus != ApplicationStatus.REJECTED) {
                    throw new InvalidStatusException("Invalid status transition from VIEWED to " + newStatus);
                }
                break;
            case ACCEPTED:
            case REJECTED:
                throw new InvalidStatusException("Cannot change status from " + currentStatus + " to " + newStatus);
        }
    }

    private ApplicationResponseDTO mapToResponseDTO(Application application) {
        ApplicationResponseDTO dto = new ApplicationResponseDTO();
        dto.setId(application.getId());
        dto.setProjectId(application.getProjectId());
        dto.setFreelancerId(application.getFreelancerId());
        dto.setEmployerId(application.getEmployerId());
        dto.setMessage(application.getMessage());
        dto.setStatus(application.getStatus());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());
        return dto;
    }
}
