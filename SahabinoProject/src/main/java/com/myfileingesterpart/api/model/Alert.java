package com.myfileingesterpart.api.model;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalTime;

/*
* this is the alert class.
* we will create a new object of this class in the rule evaluator class, and we will add them into our database.
* this is a simple class, and all we should know is that we don't create the id. the annotation will handle that.
* the description will be defined according to the type of the rule in the rule evaluator class.
* time and date will be defined as soon as an alert is created by using LocalDate.now and LocalTime.now
 */
@Entity
public class Alert {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    private int Id;
    @Column
    private String description;
    @Column
    private LocalDate date;
    @Column
    private LocalTime time;

    public Alert() {}

    public Alert(String description, LocalDate date, LocalTime time) {
        super();
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

    @Override
    public String toString() {
        return "Alert{" +
                "description='" + description + '\'' +
                ", date=" + date +
                ", time=" + time +
                '}';
    }
}
