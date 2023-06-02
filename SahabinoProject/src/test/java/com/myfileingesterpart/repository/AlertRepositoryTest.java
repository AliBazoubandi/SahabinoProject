package com.myfileingesterpart.repository;

import com.myfileingesterpart.api.model.Alert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AlertRepositoryTest {

    @Autowired
    private AlertRepository alertRepository;

    @Test
    @Order(1)
    public void saveAlertTest() {
        String description = "this is save alert test case";
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        Alert alert = new Alert(description, date, time);

        alertRepository.save(alert);
        Assertions.assertThat(alert.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getAlertTest() {
        Alert alert = alertRepository.findById(1).get();
        Assertions.assertThat(alert.getId()).isEqualTo(1);
    }

    @Test
    @Order(3)
    public void getDescriptionFromAnIdTest() {
        Alert alert = alertRepository.findById(1).get();
        Assertions.assertThat(alert.getDescription()).isEqualTo("third component this is a FATAL test for the first class");
    }

    @Test
    @Order(4)
    public void getDateFromAnIdTest() {
        Alert alert = alertRepository.findById(1).get();
        Assertions.assertThat(alert.getDate().toString()).isEqualTo("2023-06-02");
    }

    @Test
    @Order(5)
    public void getTimeFromAnIdTest() {
        Alert alert = alertRepository.findById(1).get();
        Assertions.assertThat(alert.getTime().toString()).isEqualTo("04:28:59");
    }

    @Test
    @Order(6)
    public void getListOfAlertsTest() {
        List<Alert> alerts = alertRepository.findAll();
        Assertions.assertThat(alerts.size()).isGreaterThan(0);
    }

    @Test
    @Order(7)
    public void deleteAlertTest() {
        Alert alert = alertRepository.findById(1).get();
        alertRepository.deleteById(1);

        Alert alert1 = null;
        Optional<Alert> optionalAlert = alertRepository.findById(1);
        if (optionalAlert.isPresent()) {
            alert1 = optionalAlert.get();
        }
        Assertions.assertThat(alert1).isNull();
    }

}