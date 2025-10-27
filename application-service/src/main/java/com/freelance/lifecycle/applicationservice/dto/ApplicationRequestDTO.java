package com.freelance.lifecycle.applicationservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequestDTO {

    @NotNull(message = "Project ID is required")
    private Long projectId;
    private String name;
    private String description;

    @NotNull(message = "Freelancer ID is required")
    private Long freelancerId;

    @Size(max = 1000, message = "Message cannot exceed 1000 characters")
    private String message;
}
