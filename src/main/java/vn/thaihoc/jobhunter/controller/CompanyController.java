package vn.thaihoc.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.service.CompanyService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    final private CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("")
    @ApiMessage("Create a company")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company company)
            throws MethodArgumentNotValidException {
        Company newCompany = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @GetMapping("")
    @ApiMessage("Fetch companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(
            @Filter Specification<Company> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.companyService.handleGetAllCompanies(spec, pageable));
    }

    @PutMapping("")
    @ApiMessage("Update a company")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company)
            throws MethodArgumentNotValidException, IdInvalidException {
        Company companyUpdate = this.companyService.handleUpdateCompany(company);
        if (companyUpdate == null) {
            throw new IdInvalidException("Company with id = " + company.getId() + " not found");
        }
        return ResponseEntity.ok(companyUpdate);
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a company")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        if (this.companyService.handleGetCompanyById(id) == null) {
            throw new IdInvalidException("Company with id = " + id + " not found");
        }
        this.companyService.handleDeleteCompanyById(id);
        return ResponseEntity.ok(null);
    }
}
