package com.bazinga.lantoonnew.home.chapter.lesson;

public class TaskModel {
    private @TaskState int status = TaskState.CREATED;
    private float value;


    public TaskModel(@TaskState int status) {
        this.status = status;
    }

    public TaskModel(@TaskState int status, float value) {
        this.status = status;
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public @TaskState int getStatus() {
        return status;
    }

    public void setStatus(@TaskState int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "status=" + status +
                ", value=" + value +
                '}';
    }
}
