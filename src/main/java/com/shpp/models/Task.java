package com.shpp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "TASKS")
public class Task {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonProperty(value = "task_name")
    @Column(name = "task_name")
    private String name;

    @JsonProperty(value = "planned_term")
    @Column(name = "planned_term")
    private LocalDate plannedTerm;

    @JsonProperty(value = "last_status_change")
    @Column(name = "last_status_change")
    private LocalDate lastStatusChange;

    @JsonProperty(value = "status")
    @Column(name = "status_index")
    private Status status;

    @JsonProperty(value = "status_translated")
    @Column(name = "status")
    private String statusTranslated;

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public Task setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Task setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDate getPlannedTerm() {
        return plannedTerm;
    }

    public Task setPlannedTerm(LocalDate plannedTerm) {
        this.plannedTerm = plannedTerm;
        return this;
    }

    public LocalDate getLastStatusChange() {
        return lastStatusChange;
    }

    public Task setLastStatusChange(LocalDate lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public Task setStatus(Status status) {
        this.status = status;
        return this;
    }

    public String getStatusTranslated() {
        return statusTranslated;
    }

    public Task setStatusTranslated(String statusTranslate) {
        this.statusTranslated = statusTranslate;
        return this;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", plannedTerm=" + plannedTerm +
                ", lastStatusChange=" + lastStatusChange +
                ", status=" + status +
                ", statusTranslated='" + statusTranslated + '\'' +
                '}';
    }
}
