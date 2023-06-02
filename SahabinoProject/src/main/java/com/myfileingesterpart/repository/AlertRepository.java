package com.myfileingesterpart.repository;

import com.myfileingesterpart.api.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Integer>{

}
