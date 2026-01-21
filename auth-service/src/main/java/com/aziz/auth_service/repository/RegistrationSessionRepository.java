package com.aziz.auth_service.repository;

import com.aziz.auth_service.model.RegistrationSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationSessionRepository extends CrudRepository<RegistrationSession, String> {}