package vn.thaihoc.jobhunter.service;

import org.hibernate.internal.util.collections.ConcurrentReferenceHashMap.Option;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.repository.CompanyRepository;

import java.util.List;
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

    public List<Company> handleGetAllCompanies() {
        return this.companyRepository.findAll();
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
