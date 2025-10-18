package com.freelance.lifecycle.applicationservice.dto;

import com.freelance.lifecycle.applicationservice.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;
}
