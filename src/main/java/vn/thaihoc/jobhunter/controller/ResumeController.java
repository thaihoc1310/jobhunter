package vn.thaihoc.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.Resume;
import vn.thaihoc.jobhunter.domain.response.RestCreateResumeDTO;
import vn.thaihoc.jobhunter.domain.response.RestResumeDTO;
import vn.thaihoc.jobhunter.domain.response.RestUpdateResumeDTO;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.service.ResumeService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("")
    @ApiMessage("Create a new resume")
    public ResponseEntity<RestCreateResumeDTO> createNewResume(@Valid @RequestBody Resume resume)
            throws MethodArgumentNotValidException, IdInvalidException {
        RestCreateResumeDTO newResume = this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(newResume);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Resume resume = this.resumeService.handleGetResumeById(id);
        if (resume == null) {
            throw new IdInvalidException("Resume with id = " + id + " not found");
        }
        this.resumeService.handleDeleteResume(resume);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("")
    @ApiMessage("Update a resume")
    public ResponseEntity<RestUpdateResumeDTO> updateResume(@RequestBody Resume resume)
            throws MethodArgumentNotValidException, IdInvalidException {
        RestUpdateResumeDTO resumeUpdate = this.resumeService.handleUpdateResume(resume);
        if (resumeUpdate == null) {
            throw new IdInvalidException("Skill with id = " + resume.getId() + " not found");
        }
        return ResponseEntity.ok(resumeUpdate);
    }

    @GetMapping("")
    @ApiMessage("Fetch all resumes")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Resume> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.resumeService.handleGetAllResumes(spec, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<RestResumeDTO> getResumeById(@PathVariable("id") long id) throws IdInvalidException {
        RestResumeDTO resume = this.resumeService.handleGetRestResumeDTOById(id);
        if (resume == null) {
            throw new IdInvalidException("Resume with id = " + id + " not found");
        }
        return ResponseEntity.ok(resume);
    }

    @PostMapping("/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable)
            throws MethodArgumentNotValidException, IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.fetchResumeByUser(pageable));
    }
}
