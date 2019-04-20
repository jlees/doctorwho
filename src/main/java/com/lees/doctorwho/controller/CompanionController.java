package com.lees.doctorwho.controller;

import com.lees.doctorwho.model.CompanionModel;
import com.lees.doctorwho.model.CompanionWithIdModel;
import com.lees.doctorwho.service.CompanionService;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;


@RestController
@RequestMapping("/api/companions")
@AllArgsConstructor
public class CompanionController {

    @Autowired
    private CompanionService companionService;

    @PostMapping
    public void addCompanion(@ApiParam(name="companion") @Valid @RequestBody final CompanionModel companionModel) {
        companionService.addCompanion(companionModel);
    }

    @GetMapping
    public Set<CompanionWithIdModel> getCompanions() {
        return companionService.getCompanions();
    }

    @GetMapping("/{companionId}")
    public CompanionWithIdModel getCompanion(@PathVariable long companionId) {
        return companionService.getCompanion(companionId);
    }

    @PutMapping("/{companionId}")
    public void editCompanion(@PathVariable final long companionId,
                              @ApiParam(name="companion") @Valid @RequestBody final CompanionModel companionModel) {
        companionService.editCompanion(companionId, companionModel);
    }

    @DeleteMapping("/{companionId}")
    public void deleteCompanion(@PathVariable final long companionId) {
        companionService.deleteCompanion(companionId);
    }

}
