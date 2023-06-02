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

@Service
public class RuleEvaluator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEvaluator.class);
    public static ArrayList<MyLog> firstComponentLogs = new ArrayList<>();
    public static ArrayList<MyLog> secondComponentLogs = new ArrayList<>();
    public static ArrayList<MyLog> thirdComponentLogs = new ArrayList<>();

    public static Set<Alert> createdAlertsForFirstRule = ConcurrentHashMap.newKeySet();
    public static Set<Alert> createdAlertsForSecondRule = ConcurrentHashMap.newKeySet();
    public static Set<Alert> createdAlertsForThirdRule = ConcurrentHashMap.newKeySet();

    private AlertRepository alertRepository;

    @Autowired
    public RuleEvaluator(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public static void run(ArrayList<MyLog> componentLogs) {
        CompletableFuture<Void> firstRuleFuture = CompletableFuture.runAsync(()-> firstRuleAlertBuilder(componentLogs));
        CompletableFuture<Void> secondRuleFuture = CompletableFuture.runAsync(()-> secondRuleAlertBuilder(componentLogs));
        CompletableFuture<Void> running = CompletableFuture.allOf(firstRuleFuture,secondRuleFuture);
        try {
            running.get();
            if (componentLogs.equals(firstComponentLogs)) {
                LOGGER.info("--------END firstComponentLogs:");
            } else if (componentLogs.equals(secondComponentLogs)) {
                LOGGER.info("--------END secondComponentLogs:");
            } else {
                LOGGER.info("--------END thirdComponentLogs:");
            }
        } catch (InterruptedException | ExecutionException exception) {

        }
    }

    public static void firstRuleAlertBuilder(ArrayList<MyLog> componentLogs) {

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
                LOGGER.info(alert.toString());
            }
        });
        LOGGER.info(String.format("size of list -> %s", createdAlertsForFirstRule.size()));

//        alertRepository.saveAll(createdAlertsForFirstRule);
    }
    public static void secondRuleAlertBuilder(ArrayList<MyLog> componentLogs) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
        LOGGER.info("before min and max");
        LocalTime minTime = componentLogs.stream()
                .map(log -> LocalTime.parse(log.getTime(), format))
                .min(LocalTime::compareTo)
                .orElse(null);
        LOGGER.info(String.format("Min time -> %s", minTime));
        LocalTime maxTime = componentLogs.stream()
                .map(log -> LocalTime.parse(log.getTime(), format))
                .max(LocalTime::compareTo)
                .orElse(null);
        LOGGER.info(String.format("Max time -> %s", maxTime));
        LOGGER.info("after min and max");
        if (minTime != null && maxTime != null) {
            LocalTime currentIntervalStart = minTime;
            LOGGER.info(String.format("currentIntervalStart -> %s", currentIntervalStart));
            LocalTime currentIntervalEnd = minTime.plusMinutes(5);
            LOGGER.info(String.format("currentIntervalEnd -> %s", currentIntervalEnd));
            Duration intervalDuration = Duration.ofMinutes(1);
            LOGGER.info("before while");
            Map<String, Integer> typeCounts = new HashMap<>();
            while (currentIntervalEnd.isBefore(maxTime) || currentIntervalEnd.equals(maxTime)) {
                final LocalTime start = currentIntervalStart;
                LOGGER.info(String.format("currentIntervalStart -> %s", currentIntervalStart));
                final LocalTime end = currentIntervalEnd;
                LOGGER.info(String.format("currentIntervalEnd -> %s", currentIntervalEnd));
                LOGGER.info("before finding logs in this interval");
                List<MyLog> logsWithinInterval = componentLogs.stream()
                        .filter(log -> {
                            LocalTime logTime = LocalTime.parse(log.getTime(), format);
                            return (logTime.isAfter(start) || logTime.equals(start)) && (logTime.isBefore(end) || logTime.equals(end));
                        })
                        .toList();
                LOGGER.info(String.format("this is logs within interval now -> %s",logsWithinInterval.toString()));
                LOGGER.info("before creating a map from logs types");
                for (MyLog log : logsWithinInterval) {
                    String type = log.getType();
                    typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
                }
                LOGGER.info("before finding if there is more than 10 of one type");
                for (Map.Entry<String, Integer> type : typeCounts.entrySet()) {
                    if (type.getValue() > 10) {
                        List<MyLog> lastTwoLogs = new ArrayList<>();
                        for (MyLog log : logsWithinInterval) {
                            if (log.getType().equals(type.getKey())) {
                                lastTwoLogs.add(log);
                            }
                        }
                        String description = type.getValue() + type.getKey() + lastTwoLogs.get(lastTwoLogs.size() - 1) + lastTwoLogs.get(lastTwoLogs.size() - 2);
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
                            LOGGER.info("new alert added to the second set");
                        }
                    }
                }

                currentIntervalStart = currentIntervalStart.plus(intervalDuration);
                currentIntervalEnd = currentIntervalEnd.plus(intervalDuration);
            }
        }
        createdAlertsForSecondRule.forEach(alert1 -> LOGGER.info(String.format("....... we are in rules2 -> %s", alert1.getDescription())));
//        alertRepository.saveAll(createdAlertsForSecondRule);
    }

//    public static void thirdRuleAlertBuilder(ArrayList<MyLog> componentLogs) {
//
//        LocalDateTime minTime = componentLogs.stream()
//                .map(log -> LocalDateTime.parse(log.getTime()))
//                .min(LocalDateTime::compareTo)
//                .orElse(null);
//        LocalDateTime maxTime = componentLogs.stream()
//                .map(log -> LocalDateTime.parse(log.getTime()))
//                .max(LocalDateTime::compareTo)
//                .orElse(null);
//        if (minTime != null && maxTime != null) {
//            LocalDateTime currentIntervalStart = minTime;
//            LocalDateTime currentIntervalEnd = minTime.plusMinutes(5);
//            Duration intervalDuration = Duration.ofMinutes(5);
//
//            List<String> typesWithinInterval = new ArrayList<>();
//            while (currentIntervalEnd.isBefore(maxTime) || currentIntervalEnd.isEqual(maxTime)) {
//                final LocalDateTime start = currentIntervalStart;
//                final LocalDateTime end = currentIntervalEnd;
//                List<MyLog> logsWithinInterval = componentLogs.stream()
//                        .filter(log -> {
//                            LocalDateTime logTime = LocalDateTime.parse(log.getTime());
//                            return logTime.isAfter(start) && logTime.isBefore(end);
//                        })
//                        .toList();
//                int size = logsWithinInterval.size();
//                if (size > 10) {
//                    typesWithinInterval = logsWithinInterval.stream()
//                            .map(MyLog::getType)
//                            .distinct()
//                            .collect(Collectors.toList());
//                    String description = size
//                            + typesWithinInterval.toString()
//                            + logsWithinInterval.get(size - 2).getMessage()
//                            + logsWithinInterval.get(size - 1).getMessage();
//                    LocalDate date = LocalDate.now();
//                    LocalTime time = LocalTime.now();
//                    Alert alert = new Alert(description, date, time);
//                    createdAlertsForThirdRule.add(alert);
//                }
//
//                currentIntervalStart = currentIntervalStart.plus(intervalDuration);
//                currentIntervalEnd = currentIntervalEnd.plus(intervalDuration);
//            }
//        }
//        createdAlertsForThirdRule.forEach(alert1 -> {
//            LOGGER.info(String.format("....... we are in rules3 -> %s", alert1.getDescription()));
//        });
//
////        alertRepository.saveAll(createdAlertsForThirdRule);
//    }
}
