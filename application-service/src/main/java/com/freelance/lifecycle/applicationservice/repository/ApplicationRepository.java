package com.freelance.lifecycle.applicationservice.repository;

import com.freelance.lifecycle.applicationservice.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByProjectId(Long projectId);

    List<Application> findByFreelancerId(Long freelancerId);

    List<Application> findByEmployerId(Long employerId);
}
