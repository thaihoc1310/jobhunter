package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Skill;
import vn.thaihoc.jobhunter.repository.SkillRepository;

import java.util.List;

@Service
public class SkillService {
    final private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill handleCreateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public boolean handleCheckSkillExistByName(String name) {
        return this.skillRepository.existsByName(name);
    }

    public List<Skill> handleGetAllSkills(Specification<Skill> spec, Pageable pageable) {
        return this.skillRepository.findAll(spec, pageable).getContent();
    }

    public Skill handleGetSkillById(long id) {
        return this.skillRepository.findById(id).orElse(null);
    }

    public Skill handleUpdateSkill(Skill newSkill) {
        Skill skillUpdate = this.handleGetSkillById(newSkill.getId());
        if (skillUpdate != null) {
            skillUpdate.setName(newSkill.getName());
            return this.skillRepository.save(skillUpdate);
        }
        return null;
    }
}
