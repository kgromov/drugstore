package org.kgromov.drugstore.controller;

import lombok.RequiredArgsConstructor;
import org.kgromov.drugstore.model.DrugsInfo;
import org.kgromov.drugstore.repository.DrugsRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drugs")
@RequiredArgsConstructor
public class DrugsInfoController {
    private final DrugsRepository drugsRepository;

    @GetMapping
    public List<DrugsInfo> getAll() {
        return drugsRepository.findAll();
    }

    @GetMapping("/{id}")
    public DrugsInfo getById(@PathVariable int id) {
        return drugsRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable int id, @RequestBody DrugsInfo drugsInfo) {
        drugsRepository.update(drugsInfo);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        drugsRepository.deleteById(id);
    }
}
