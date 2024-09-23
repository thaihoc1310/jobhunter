package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Job;
import vn.thaihoc.jobhunter.domain.response.RestCreateJobDTO;
import vn.thaihoc.jobhunter.domain.response.RestUpdateJobDTO;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.JobRepository;

import java.util.List;

@Service
public class JobService {
    final private JobRepository jobRepository;
    final private SkillService skillService;

    public JobService(JobRepository jobRepository, SkillService skillService) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
    }

    public RestCreateJobDTO handleCreateJob(Job job) {
        // check skill
        if (job.getSkills() != null) {
            List<Long> skillIds = job.getSkills().stream().map(skill -> skill.getId()).toList();
            job.setSkills(this.skillService.handleGetSkillsByIds(skillIds));
        }
        job = this.jobRepository.save(job);

        // convert to DTO
        RestCreateJobDTO createJobDTO = new RestCreateJobDTO(
                job.getId(),
                job.getName(),
                job.getSalary(),
                job.getQuantity(),
                job.getLocation(),
                job.getLevel(),
                job.getStartDate(),
                job.getEndDate(),
                job.isActive(),
                job.getCreatedAt(),
                job.getCreatedBy(),
                job.getSkills() != null ? job.getSkills().stream().map(skill -> skill.getName()).toList() : null);
        return createJobDTO;
    }

    public Job handleGetJobById(long id) {
        return this.jobRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO handleGetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageSkill = this.jobRepository.findAll(spec, pageable);
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

    public void handleDeleteJobById(long id) {
        this.jobRepository.deleteById(id);
    }

    public RestUpdateJobDTO handleUpdateJob(Job newJob) {
        Job jobUpdate = this.handleGetJobById(newJob.getId());
        RestUpdateJobDTO updateJobDTO = null;
        if (jobUpdate != null) {
            if (newJob.getSkills() != null) {
                List<Long> skillIds = newJob.getSkills().stream().map(skill -> skill.getId()).toList();
                jobUpdate.setSkills(this.skillService.handleGetSkillsByIds(skillIds));
            }
            if (jobUpdate != null) {
                jobUpdate.setName(newJob.getName());
                jobUpdate.setSalary(newJob.getSalary());
                jobUpdate.setQuantity(newJob.getQuantity());
                jobUpdate.setLocation(newJob.getLocation());
                jobUpdate.setLevel(newJob.getLevel());
                jobUpdate.setStartDate(newJob.getStartDate());
                jobUpdate.setEndDate(newJob.getEndDate());
                jobUpdate.setActive(newJob.isActive());
                this.jobRepository.save(jobUpdate);
            }
            updateJobDTO = new RestUpdateJobDTO(
                    jobUpdate.getId(),
                    jobUpdate.getName(),
                    jobUpdate.getSalary(),
                    jobUpdate.getQuantity(),
                    jobUpdate.getLocation(),
                    jobUpdate.getLevel(),
                    jobUpdate.getStartDate(),
                    jobUpdate.getEndDate(),
                    jobUpdate.isActive(),
                    jobUpdate.getUpdatedAt(),
                    jobUpdate.getUpdatedBy(),
                    jobUpdate.getSkills() != null
                            ? jobUpdate.getSkills().stream().map(skill -> skill.getName()).toList()
                            : null);
        }
        return updateJobDTO;
    }
}
