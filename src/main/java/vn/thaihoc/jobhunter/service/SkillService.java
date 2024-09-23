package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Skill;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
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

    public ResultPaginationDTO handleGetAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;

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

    public void handleDeleteSkill(Skill skill) {
        // delete this skill from all jobs
        skill.getJobs().forEach(user -> user.getSkills().remove(skill));
        this.skillRepository.delete(skill);
    }

    public List<Skill> handleGetSkillsByIds(List<Long> ids) {
        return this.skillRepository.findByIdIn(ids);
    }
}
