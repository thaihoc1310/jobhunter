package vn.thaihoc.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.Job;
import vn.thaihoc.jobhunter.domain.response.RestCreateJobDTO;
import vn.thaihoc.jobhunter.domain.response.RestUpdateJobDTO;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.service.JobService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/v1/jobs")
public class JobController {
    final private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("")
    @ApiMessage("Create a new job")
    public ResponseEntity<RestCreateJobDTO> createNewJob(@Valid @RequestBody Job job)
            throws MethodArgumentNotValidException {
        RestCreateJobDTO newJob = this.jobService.handleCreateJob(job);
        return ResponseEntity.ok(newJob);
    }

    @GetMapping("")
    @ApiMessage("Fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.jobService.handleGetAllJobs(spec, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch a job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("id") long id) throws IdInvalidException {
        Job job = this.jobService.handleGetJobById(id);
        if (job == null) {
            throw new IdInvalidException("Job with id = " + id + " not found");
        }
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<Void> deleteJob(@PathVariable("id") long id) throws IdInvalidException {
        if (this.jobService.handleGetJobById(id) == null) {
            throw new IdInvalidException("Job with id = " + id + " not found");
        }
        this.jobService.handleDeleteJobById(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("")
    @ApiMessage("Update a job")
    public ResponseEntity<RestUpdateJobDTO> updateJob(@Valid @RequestBody Job job)
            throws IdInvalidException, MethodArgumentNotValidException {
        RestUpdateJobDTO newJob = this.jobService.handleUpdateJob(job);
        if (newJob == null) {
            throw new IdInvalidException("Job with id = " + job.getId() + " not found");
        }
        return ResponseEntity.ok(newJob);
    }
}
