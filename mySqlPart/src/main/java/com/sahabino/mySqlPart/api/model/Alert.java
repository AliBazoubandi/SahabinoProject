package com.sahabino.mySqlPart.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Alert {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    private int Id;
    @Column
    private String ruleName;
    @Column
    private String componentName;
    @Column
    private String description;
    @Column
    private LocalDate date;
    @Column
    private LocalTime time;

    public Alert() {}

    public Alert(String ruleName, String componentName, String description, LocalDate date, LocalTime time) {
        super();
        this.ruleName = ruleName;
        this.componentName = componentName;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() { return time; }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
