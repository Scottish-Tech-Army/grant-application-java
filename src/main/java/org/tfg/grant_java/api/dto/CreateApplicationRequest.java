package org.tfg.grant_java.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class CreateApplicationRequest {
    @NotBlank
    private String name;
    private String funderName;
    private LocalDate deadline;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFunderName() { return funderName; }
    public void setFunderName(String funderName) { this.funderName = funderName; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
}

