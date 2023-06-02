package com.myfileingesterpart.api.model;

/*
* this is the log class, and instance of this class will be defined in the main class.
* when we read from a file, each line is a log, so based on the pattern that is defined in the main class,
* we will get fields that we need for creating a new instance of this class.
* so every field of instances of this class will be defined based on the one line of the file
* that we read except the componentName field.
* this field is based on the file name.
 */
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