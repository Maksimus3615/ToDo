package com.shpp.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TaskDTO {
    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate plannedTerm;
    @NotNull
    String taskName;

    public TaskDTO(LocalDate plannedTerm, String taskName) {
        this.plannedTerm = plannedTerm;
        this.taskName = taskName;
    }

    public TaskDTO() {
    }

    public LocalDate getPlannedTerm() {
        return plannedTerm;
    }

    public void setPlannedTerm(LocalDate plannedTerm) {
        this.plannedTerm = plannedTerm;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
