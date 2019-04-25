package com._51doit.pojo;


public class LogBean {

    private User u;

    private String logType;

    private String commit_time;

    private Event event;

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getCommit_time() {
        return commit_time;
    }

    public void setCommit_time(String commit_time) {
        this.commit_time = commit_time;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "LogEntity{" +
                "u=" + u +
                ", logType='" + logType + '\'' +
                ", commit_time='" + commit_time + '\'' +
                ", event=" + event +
                '}';
    }
}
