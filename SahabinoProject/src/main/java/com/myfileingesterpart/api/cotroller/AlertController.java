package com.myfileingesterpart.api.cotroller;

import com.myfileingesterpart.api.model.Alert;
import com.myfileingesterpart.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
* this is the third part of the project
* in this class that is a rest controller class we use JPA and Springboot to communicate with a database.
* by using the proper annotation, we can do anything based on CRUD concept that we want.
* we need a field for this class that is an instance of AlertRepository.
* because we want to use its methods to send or get data from a database.
* in this case,
* we used @GetMapping on top of method
* that is using alert repositories method to get a list of alerts from the database.
* and we used @GetMapping("{id}")
* so if we go to this path and send a GetRequest, we get a specific alert based on the id.
* we also used @DeleteMapping("{id}")
* so if we go to this path and send a DeleteRequest, we can delete a specific alert based on the id.
* we could have defined two other methods, using @PostMapping and @PutMapping to create a new alert via PostRequest or updating an alert via PutRequest.
 */
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

    @GetMapping("{Id}")
    public Alert getAlert(@PathVariable("Id") Integer id) {
        Alert alert = null;
        Optional<Alert> alert1 = alertRepository.findById(id);
        alert = alert1.orElse(null);
        return alert;
    }
    @DeleteMapping("{Id}")
    public void deleteAlert(@PathVariable("Id") Integer id) {
        alertRepository.deleteById(id);
    }
}
