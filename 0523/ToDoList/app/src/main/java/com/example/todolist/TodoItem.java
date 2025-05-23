package com.example.todolist;

public class TodoItem {
    private int id;
    private String task;
    private boolean isCompleted;
    private long alarmTime;

    public TodoItem() {
    }

    public TodoItem(int id, String task, boolean isCompleted, long alarmTime) {
        this.id = id;
        this.task = task;
        this.isCompleted = isCompleted;
        this.alarmTime = alarmTime;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public long getAlarmTime() {
        return alarmTime;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setAlarmTime(long alarmTime) {
        this.alarmTime = alarmTime;
    }
}