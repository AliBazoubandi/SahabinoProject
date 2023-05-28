package com.sahabino.mySqlPart.repository;

import com.sahabino.mySqlPart.api.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
}
