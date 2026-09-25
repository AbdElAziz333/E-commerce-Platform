package com.aziz.api_gateway.repository;

import com.aziz.api_gateway.model.RegistrationSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationSessionRepository extends CrudRepository<RegistrationSession, String> {}