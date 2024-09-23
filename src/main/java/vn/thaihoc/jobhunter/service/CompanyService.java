package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.CompanyRepository;
import vn.thaihoc.jobhunter.repository.UserRepository;

import java.util.Optional;
import java.util.List;

@Service
public class CompanyService {
    final private CompanyRepository companyRepository;
    final private UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
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
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
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
        Company com = this.handleGetCompanyById(id);
        if (com != null) {
            List<User> users = this.userRepository.findByCompany(com);
            this.userRepository.deleteAll(users);
        }
        this.companyRepository.deleteById(id);
    }
}
