package vn.thaihoc.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.thaihoc.jobhunter.domain.Job;
import vn.thaihoc.jobhunter.domain.Resume;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.response.RestCreateResumeDTO;
import vn.thaihoc.jobhunter.domain.response.RestResumeDTO;
import vn.thaihoc.jobhunter.domain.response.RestUpdateResumeDTO;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.ResumeRepository;
import vn.thaihoc.jobhunter.util.SecurityUtil;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@Service
public class ResumeService {
    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;
    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final JobService jobService;

    public ResumeService(ResumeRepository resumeRepository, UserService userService, JobService jobService) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.jobService = jobService;
    }

    public void checkResumeExistByUserAndJob(Resume resume) throws IdInvalidException {
        if (resume.getUser() != null) {
            if (this.userService.handleGetUserById(resume.getUser().getId()) == null) {
                throw new IdInvalidException("User with id = " + resume.getUser().getId() + " not found");
            }
        }
        if (resume.getJob() != null) {
            if (this.jobService.handleGetJobById(resume.getJob().getId()) == null) {
                throw new IdInvalidException("Job with id = " + resume.getJob().getId() + " not found");
            }
        }
    }

    public RestCreateResumeDTO handleCreateResume(Resume resume) throws IdInvalidException {
        checkResumeExistByUserAndJob(resume);
        resume = this.resumeRepository.save(resume);
        RestCreateResumeDTO createResumeDTO = new RestCreateResumeDTO(
                resume.getId(),
                resume.getCreatedAt(),
                resume.getCreatedBy());
        return createResumeDTO;
    }

    public Resume handleGetResumeById(long id) {
        return this.resumeRepository.findById(id).orElse(null);
    }

    public void handleDeleteResume(Resume resume) {
        this.resumeRepository.deleteById(resume.getId());
    }

    public RestResumeDTO handleGetRestResumeDTOById(long id) {
        Resume resume = handleGetResumeById(id);
        RestResumeDTO resumeDTO = null;
        if (resume != null) {
            Job job = resume.getJob();
            resumeDTO = new RestResumeDTO(
                    resume.getId(),
                    resume.getEmail(),
                    resume.getUrl(),
                    resume.getStatus(),
                    job != null && job.getCompany() != null ? job.getCompany().getName() : null,
                    new RestResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()),
                    job != null ? new RestResumeDTO.JobResume(job.getId(), job.getName()) : null,
                    resume.getCreatedAt(),
                    resume.getUpdatedAt(),
                    resume.getCreatedBy(),
                    resume.getUpdatedBy());
        }
        return resumeDTO;
    }

    public RestUpdateResumeDTO handleUpdateResume(Resume resume) {
        Resume resumeUpdate = handleGetResumeById(resume.getId());
        RestUpdateResumeDTO updateResumeDTO = null;
        if (resumeUpdate != null) {
            resumeUpdate.setStatus(resume.getStatus());
            resumeUpdate.setUpdatedAt(resume.getUpdatedAt());
            resumeUpdate.setUpdatedBy(resume.getUpdatedBy());
            resumeUpdate = this.resumeRepository.save(resumeUpdate);
            updateResumeDTO = new RestUpdateResumeDTO(
                    resumeUpdate.getUpdatedAt(),
                    resumeUpdate.getUpdatedBy());
        }
        return updateResumeDTO;
    }

    public ResultPaginationDTO handleGetAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        List<RestResumeDTO> listResume = pageResume.getContent()
                .stream().map(resume -> this.handleGetRestResumeDTOById(resume.getId()))
                .collect(Collectors.toList());
        rs.setResult(listResume);
        return rs;
    }

    public ResultPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User user = this.userService.handleGetUserByUsername(email);
        FilterNode node = filterParser.parse("user.id='" + user.getId() + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        List<RestResumeDTO> listResume = pageResume.getContent()
                .stream().map(resume -> this.handleGetRestResumeDTOById(resume.getId()))
                .collect(Collectors.toList());
        rs.setResult(listResume);
        return rs;
    }

}
