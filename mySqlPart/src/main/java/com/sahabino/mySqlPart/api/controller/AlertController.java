package com.sahabino.mySqlPart.api.controller;

import com.sahabino.mySqlPart.api.model.Alert;
import com.sahabino.mySqlPart.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    private final AlertRepository alertRepository;

    @Autowired
    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping
    public List<Alert> getAlerts(){
        return alertRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Alert::getDate).
                        thenComparing(Alert::getTime))
                .collect(Collectors.toList());
    }

    @GetMapping("{ruleComponentName}")
    public List<Alert> getAlertsBasedOnRuleOrComponent(@PathVariable("ruleComponentName") String ruleComponentName) {
        return alertRepository.findAll()
                .stream()
                .filter(a -> a.getRuleName().equals(ruleComponentName)
                || a.getComponentName().equals(ruleComponentName))
                .collect(Collectors.toList());
    }

    record NewAlertRequest(
            String ruleName,
            String componentName,
            String description,
            LocalDate date,
            LocalTime time
    ){}

    @PostMapping
    public void addAlert (@RequestBody NewAlertRequest request) {
        Alert alert = new Alert();
        alert.setRuleName(request.ruleName());
        alert.setComponentName(request.componentName());
        alert.setDescription(request.description());
        alert.setDate(request.date());
        alert.setTime(request.time());
        alertRepository.save(alert);
    }

    @DeleteMapping("{Id}")
    public void deleteAlert(@PathVariable("Id") Integer id) {
        alertRepository.deleteById(id);
    }
}

