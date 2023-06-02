package com.myfileingesterpart.api.model;

public class MyLog {
    private String time;
    private String date;
    private String componentName;
    private String threadName;
    private String type;
    private String className;
    private String message;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MyLog{" +
                "time=" + time +
                ", date=" + date +
                ", threadName='" + threadName + '\'' +
                ", type='" + type + '\'' +
                ", className='" + className + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}