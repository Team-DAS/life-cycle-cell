package com.freelance.lifecycle.applicationservice.dto;

import com.freelance.lifecycle.applicationservice.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDTO {

    private Long id;
    private Long projectId;
    private Long freelancerId;
    private Long employerId;
    private String message;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
