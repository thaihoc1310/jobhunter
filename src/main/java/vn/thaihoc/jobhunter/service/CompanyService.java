package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.domain.dto.Meta;
import vn.thaihoc.jobhunter.domain.dto.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.CompanyRepository;

import java.util.Optional;

@Service
public class CompanyService {
    final private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }

    // public List<Company> handleGetAllCompanies(Pageable pageable) {
    // return this.companyRepository.findAll(pageable).getContent();
    // }

    public ResultPaginationDTO handleGetAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company handleGetCompanyById(long id) {
        Optional<Company> company = this.companyRepository.findById(id);
        return company.isPresent() ? company.get() : null;
    }

    public Company handleUpdateCompany(Company newCompany) {
        Company companyUpdate = this.handleGetCompanyById(newCompany.getId());
        if (companyUpdate != null) {
            companyUpdate.setAddress(newCompany.getAddress());
            companyUpdate.setDescription(newCompany.getDescription());
            companyUpdate.setLogo(newCompany.getLogo());
            companyUpdate.setName(newCompany.getName());
            return this.companyRepository.save(companyUpdate);
        }
        return null;
    }

    public void handleDeleteCompanyById(long id) {
        this.companyRepository.deleteById(id);
    }
}
