package org.tfg.grant_java.controller;

import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.entity.GrantApplication;
import org.tfg.grant_java.repository.GrantApplicationRepository;

import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {


    private final GrantApplicationRepository repo;

    public ApplicationController(GrantApplicationRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public GrantApplication create(@RequestBody GrantApplication app) {
        return repo.save(app);
    }

    @GetMapping
    public List<GrantApplication> getAll() {
        return repo.findAll();
    }
}
