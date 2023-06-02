package com.myfileingesterpart.validator;

import com.myfileingesterpart.api.model.Alert;
import com.myfileingesterpart.api.model.MyLog;

import com.myfileingesterpart.repository.AlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;

/*
* this is the second part of the project that we need to get the data from kafka using a consumer.
* then, according to the rules mentioned in the project, create an alert to send to the database.
* in this class, we have 3 lists that we use to save component logs in them.
* each list is belonged to one specific component and only saves the logs that are related to that component.
* we also have 3 concurrent sets for saving created alerts related to each list.
* it means that each list has a specific set to itself.
* we used concurrent HashMap to define the sets because we are dealing with threads and parallelism.
* we need an AlertRepository object in this class too. cause after creating alerts, we need to send them into the database.
* we have 3 methods in this class for 3 rules that are mentioned in the project. and we have one method to run all of them simultaneously.
 */
@Service
public class RuleEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEvaluator.class);
    public static ArrayList<MyLog> firstComponentLogs = new ArrayList<>();
    public static ArrayList<MyLog> secondComponentLogs = new ArrayList<>();
    public static ArrayList<MyLog> thirdComponentLogs = new ArrayList<>();

    public static Set<Alert> createdAlertsForFirstRule = ConcurrentHashMap.newKeySet();
    public static Set<Alert> createdAlertsForSecondRule = ConcurrentHashMap.newKeySet();
    public static Set<Alert> createdAlertsForThirdRule = ConcurrentHashMap.newKeySet();

    private static AlertRepository alertRepository;

    @Autowired
    public RuleEvaluator(AlertRepository alertRepository) {
        RuleEvaluator.alertRepository = alertRepository;
    }

    /*
    * this method needs a List of components to pass them into the 3 methods that it will call in itself.
    * the component will specify that the methods should run on which log list.
    * in order to be able
    * to use runAsync method to run all the 3 methods simultaneously, we used CompletableFuture<Void>.
     */
    public static void run(List<MyLog> componentLogs) {
        CompletableFuture<Void> firstRuleFuture = CompletableFuture.runAsync(()-> firstRuleAlertBuilder(componentLogs));
        CompletableFuture<Void> secondRuleFuture = CompletableFuture.runAsync(()-> secondRuleAlertBuilder(componentLogs));
        CompletableFuture<Void> thirdRuleFuture = CompletableFuture.runAsync(()-> thirdRuleAlertBuilder(componentLogs));
        CompletableFuture<Void> running = CompletableFuture.allOf(firstRuleFuture, secondRuleFuture, thirdRuleFuture);
        try {
            running.get();
        } catch (InterruptedException | ExecutionException exception) {

        }
    }
    /*
    * in this method, we try to create alerts about the first rule.
    * the instruction is easy.
    * we just need to go through the list of logs that we are given to, and for each log we need to create a new alert.
    * but if the alert is already created, we shouldn't create it again,
    * so we are going through our created logs set and with the help of a boolean variable, we do this.
    * if everything is ok and the alert is not yet in our set, we create the new alert,
    * and we will add it to our database after we create it.
     */
    public static void firstRuleAlertBuilder(List<MyLog> componentLogs) {

        componentLogs.forEach(log -> {
            String description = log.getMessage();
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            boolean isDuplicate = false;
            for (Alert alert1 : createdAlertsForFirstRule) {
                if (alert1.getDescription().equals(description)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                Alert alert = new Alert(description, date, time);
                createdAlertsForFirstRule.add(alert);
                alertRepository.save(alert);
            }
        });
    }

    /*
    * in this method, we try to create alerts about the first rule.
    * the instruction is easy.
    * we just need to create and interval of time in this case we considered 5 minutes.
    * we get the min time from the log list, and we get the max time too.
    * when our interval's end is equal or less than our max time, we can go into our loop.
    * in the loop we get every log that is in that interval.
    * based on the logs within our interval, we create a map.
    * the keys of this map are the distinct types that we have in our within interval logs.
    * and the values of them are equal to the number of times that is repeated in our list.
    * then we go through this map, and if there is a key(type) that has a value more than 2, we create an alert for it.
    * we do the kind of exact same thing to see if the alert is already.
    * if not, we will create a new alert and add it to our set and will add it to our database again.
    * at the end of while we will give the start and end of the interval new value,
    * so we can go through this process for the next interval too.
    * we add 1 minute to start and end, so if we went through 19 to 24 in the last interval, we will go through 20 to 25 in the new interval.
     */
    public static void secondRuleAlertBuilder(List<MyLog> componentLogs) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LocalTime minTime = componentLogs.stream()
                .map(log -> LocalTime.parse(log.getTime(), format))
                .min(LocalTime::compareTo)
                .orElse(null);
        LocalTime maxTime = componentLogs.stream()
                .map(log -> LocalTime.parse(log.getTime(), format))
                .max(LocalTime::compareTo)
                .orElse(null);
        if (minTime != null && maxTime != null) {
            LocalTime currentIntervalStart = minTime;
            LocalTime currentIntervalEnd = minTime.plusMinutes(5);
            Duration intervalDuration = Duration.ofMinutes(1);
            Map<String, Integer> typeCounts = new ConcurrentHashMap<>();
            while (currentIntervalEnd.isBefore(maxTime) || currentIntervalEnd.equals(maxTime)) {
                final LocalTime start = currentIntervalStart;
                final LocalTime end = currentIntervalEnd;
                List<MyLog> logsWithinInterval = componentLogs.stream()
                        .filter(log -> {
                            LocalTime logTime = LocalTime.parse(log.getTime(), format);
                            return (logTime.isAfter(start) || logTime.equals(start)) && (logTime.isBefore(end) || logTime.equals(end));
                        })
                        .toList();
                for (MyLog log : logsWithinInterval) {
                    String type = log.getType();
                    typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
                }
                LOGGER.info("\n");
                for (Map.Entry<String, Integer> type : typeCounts.entrySet()) {
                    if (type.getValue() > 1) {
                        List<MyLog> lastTwoLogs = new ArrayList<>();
                        for (MyLog log : logsWithinInterval) {
                            if (log.getType().equals(type.getKey())) {
                                lastTwoLogs.add(log);
                            }
                        }
                        String description = type.getValue() + " " + type.getKey() + " "  + lastTwoLogs.get(lastTwoLogs.size() - 1).getMessage() + " "  + lastTwoLogs.get(lastTwoLogs.size() - 2).getMessage();
                        LocalDate date = LocalDate.now();
                        LocalTime time = LocalTime.now();
                        boolean isDuplicate = false;
                        for (Alert alert : createdAlertsForSecondRule) {
                            if (alert.getDescription().equals(description)) {
                                isDuplicate = true;
                                break;
                            }
                        }
                        if (!isDuplicate) {
                            Alert alert = new Alert(description, date, time);
                            createdAlertsForSecondRule.add(alert);
                            alertRepository.save(alert);
                        }
                    }
                }
                typeCounts.clear();
                currentIntervalStart = currentIntervalStart.plus(intervalDuration);
                currentIntervalEnd = currentIntervalEnd.plus(intervalDuration);
            }
        }
    }

    /*
    * in this method, we try to create alerts about the first rule.
    * the instruction is easy. we just need to create and interval of time in this case we considered 5 minutes.
    * we get the min time from the log list, and we get the max time too. when our interval's end is equal or less than our max time we can go into our loop.
    * in the loop we get every log that is in that interval.
    * if the logs withing interval list have more than 4 elements, we create a new alert.
    * we do the kind of exact same thing to see if the alert is already. if not, we will create a new alert and add it to our set and will add it to our database again.
    * at the end of while we will give the start and end of the interval new value, so we can go through this process for the next interval too.
    * we add 1 minute to start and end, so if we went through 19 to 24 in the last interval, we will go through 20 to 25 in the new interval.
     */
    public static void thirdRuleAlertBuilder(List<MyLog> componentLogs) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LocalTime minTime = componentLogs.stream()
                .map(log -> LocalTime.parse(log.getTime(), format))
                .min(LocalTime::compareTo)
                .orElse(null);
        LocalTime maxTime = componentLogs.stream()
                .map(log -> LocalTime.parse(log.getTime(), format))
                .max(LocalTime::compareTo)
                .orElse(null);
        if (minTime != null && maxTime != null) {
            LocalTime currentIntervalStart = minTime;
            LocalTime currentIntervalEnd = minTime.plusMinutes(5);
            Duration intervalDuration = Duration.ofMinutes(1);

            while (currentIntervalEnd.isBefore(maxTime) || currentIntervalEnd.equals(maxTime)) {
                final LocalTime start = currentIntervalStart;
                final LocalTime end = currentIntervalEnd;
                List<MyLog> logsWithinInterval = componentLogs.stream()
                        .filter(log -> {
                            LocalTime logTime = LocalTime.parse(log.getTime(), format);
                            return (logTime.isAfter(start) || logTime.equals(start)) && (logTime.isBefore(end) || logTime.equals(end));
                        })
                        .toList();
                int size = logsWithinInterval.size();
                if (size > 4) {
                    String description = String.format("log ratio in the interval between %s and %s is equal to %s", currentIntervalStart, currentIntervalEnd, size);
                    LocalDate date = LocalDate.now();
                    LocalTime time = LocalTime.now();
                    boolean isDuplicate = false;
                    for (Alert alert : createdAlertsForThirdRule) {
                        if (alert.getDescription().equals(description)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        Alert alert = new Alert(description, date, time);
                        createdAlertsForThirdRule.add(alert);
                        alertRepository.save(alert);
                    }
                }

                currentIntervalStart = currentIntervalStart.plus(intervalDuration);
                currentIntervalEnd = currentIntervalEnd.plus(intervalDuration);
            }
        }
    }
}
