package com.myfileingesterpart.model;

public class MyLog {
    private String time;
    private String date;
    private String threadName;
    private String info;
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

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
                ", info='" + info + '\'' +
                ", className='" + className + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
