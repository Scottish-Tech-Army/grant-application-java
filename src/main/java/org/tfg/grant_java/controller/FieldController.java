package org.tfg.grant_java.controller;

import org.springframework.web.bind.annotation.*;
import org.tfg.grant_java.entity.CommonField;
import org.tfg.grant_java.repository.CommonFieldRepository;

import java.util.List;

@RestController
@RequestMapping("/fields")
public class FieldController {


    private final CommonFieldRepository repo;

    public FieldController(CommonFieldRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public CommonField create(@RequestBody CommonField field) {
        return repo.save(field);
    }

    @GetMapping
    public List<CommonField> getAll() {
        return repo.findAll();
    }
}





