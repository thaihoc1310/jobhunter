package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.Skill;
import vn.thaihoc.jobhunter.service.SkillService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/v1/skills")
public class SkillController {

    final private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill skill)
            throws MethodArgumentNotValidException, IdInvalidException {
        if (this.skillService.handleCheckSkillExistByName(skill.getName())) {
            throw new IdInvalidException("Skill with name = " + skill.getName() + " already exists");
        }
        Skill newSkill = this.skillService.handleCreateSkill(skill);
        return ResponseEntity.ok(newSkill);
    }

    @GetMapping("")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<List<Skill>> getAllSkills(@Filter Specification<Skill> spec,
            Pageable pageable) {
        List<Skill> skills = this.skillService.handleGetAllSkills(spec, pageable);
        return ResponseEntity.ok(skills);
    }

    @PutMapping("")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill)
            throws MethodArgumentNotValidException, IdInvalidException {
        Skill skillUpdate = this.skillService.handleUpdateSkill(skill);
        if (skillUpdate == null) {
            throw new IdInvalidException("Skill with id = " + skill.getId() + " not found");
        }
        return ResponseEntity.ok(skillUpdate);
    }
}
