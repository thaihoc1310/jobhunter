package vn.thaihoc.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {
    final private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company company) {
        return this.companyRepository.save(company);
    }
}
