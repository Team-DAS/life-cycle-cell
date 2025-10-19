package com.freelance.lifecycle.applicationservice.controller;

import com.freelance.lifecycle.applicationservice.dto.ApplicationRequestDTO;
import com.freelance.lifecycle.applicationservice.dto.ApplicationResponseDTO;
import com.freelance.lifecycle.applicationservice.dto.StatusUpdateDTO;
import com.freelance.lifecycle.applicationservice.service.ApplicationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationResponseDTO> createApplication(@Valid @RequestBody ApplicationRequestDTO dto) {
        ApplicationResponseDTO response = applicationService.createApplication(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponseDTO> updateStatus(
            @PathVariable Long id, 
            @Valid @RequestBody StatusUpdateDTO dto) {
        ApplicationResponseDTO response = applicationService.updateStatus(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByProjectId(@PathVariable Long projectId) {
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsByProjectId(projectId);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/user/{freelancerId}")
    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByFreelancerId(@PathVariable Long freelancerId) {
        List<ApplicationResponseDTO> applications = applicationService.getApplicationsByFreelancerId(freelancerId);
        return ResponseEntity.ok(applications);
    }
}
