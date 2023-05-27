package com.sahabino.mySqlPart;

import jakarta.persistence.*;

@Entity
@Table(name = "ALERT")
public class Alert {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column
    private int ruleID;
    @Column
    private String componentName;
    @Column
    private String description;
    @Column
    private String date;
    @Column
    private String time;

    public Alert() {
    }

    public Alert(String componentName, String description, String date, String time) {
        super();
        this.componentName = componentName;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public int getRuleID() {
        return ruleID;
    }

    public void setRuleID(int ruleID) {
        this.ruleID = ruleID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
