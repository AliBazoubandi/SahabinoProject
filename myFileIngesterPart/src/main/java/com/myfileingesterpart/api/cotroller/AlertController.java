package com.myfileingesterpart.api.cotroller;

import com.myfileingesterpart.api.model.Alert;
import com.myfileingesterpart.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private AlertRepository alertRepository;

    @Autowired
    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping
    public List<Alert> getAlerts() {
        return alertRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Alert::getDate)
                        .thenComparing(Alert::getTime))
                .collect(Collectors.toList());
    }

//    @GetMapping("{ruleComponentName}")
//    public List<Alert> getAlertsBasedOnRuleOrComponent(@PathVariable("ruleComponentName") String ruleComponentName) {
//        return alertRepository.findAll()
//                .stream()
//                .filter(a -> a.getRuleName().equals(ruleComponentName)
//                        || a.getComponentName().equals(ruleComponentName))
//                .collect(Collectors.toList());
//    }
    @DeleteMapping("{Id}")
    public void deleteAlert(@PathVariable("Id") Integer id) {
        alertRepository.deleteById(id);
    }
}
