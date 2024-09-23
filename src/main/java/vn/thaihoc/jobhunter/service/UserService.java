package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.response.RestCreateUserDTO;
import vn.thaihoc.jobhunter.domain.response.RestUpdateUserDTO;
import vn.thaihoc.jobhunter.domain.response.RestUserDTO;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User user) {
        // check company
        Company curCompany = user.getCompany();
        if (curCompany != null) {
            Company dbCompany = this.companyService.handleGetCompanyById(curCompany.getId());
            user.setCompany(dbCompany);
        }
        return this.userRepository.save(user);
    }

    public RestCreateUserDTO convertToRestCreateUserDTO(User user) {
        RestCreateUserDTO restCreateUserDTO = new RestCreateUserDTO();
        RestCreateUserDTO.CompanyUser companyUser = restCreateUserDTO.new CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            restCreateUserDTO.setCompany(companyUser);
        }
        restCreateUserDTO.setId(user.getId());
        restCreateUserDTO.setName(user.getName());
        restCreateUserDTO.setEmail(user.getEmail());
        restCreateUserDTO.setGender(user.getGender());
        restCreateUserDTO.setAge(user.getAge());
        restCreateUserDTO.setAddress(user.getAddress());
        restCreateUserDTO.setCreatedAt(user.getCreatedAt());
        return restCreateUserDTO;
    }

    public RestUserDTO convertToRestUserDTO(User user) {
        RestUserDTO restUserDTO = new RestUserDTO();
        RestUserDTO.CompanyUser companyUser = new RestUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            restUserDTO.setCompany(companyUser);
        }
        restUserDTO.setId(user.getId());
        restUserDTO.setName(user.getName());
        restUserDTO.setEmail(user.getEmail());
        restUserDTO.setGender(user.getGender());
        restUserDTO.setAge(user.getAge());
        restUserDTO.setAddress(user.getAddress());
        restUserDTO.setCreatedAt(user.getCreatedAt());
        restUserDTO.setUpdatedAt(user.getUpdatedAt());
        return restUserDTO;
    }

    public RestUpdateUserDTO convertToRestUpdateUserDTO(User user) {
        RestUpdateUserDTO restUpdateUserDTO = new RestUpdateUserDTO();
        RestUpdateUserDTO.CompanyUser companyUser = new RestUpdateUserDTO.CompanyUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            restUpdateUserDTO.setCompany(companyUser);
        }
        restUpdateUserDTO.setId(user.getId());
        restUpdateUserDTO.setName(user.getName());
        restUpdateUserDTO.setGender(user.getGender());
        restUpdateUserDTO.setAge(user.getAge());
        restUpdateUserDTO.setAddress(user.getAddress());
        restUpdateUserDTO.setUpdatedAt(user.getUpdatedAt());
        return restUpdateUserDTO;
    }

    public void handleDeleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    public boolean handleCheckUserExistByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResultPaginationDTO handleGetAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());
        rs.setMeta(mt);
        List<RestUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new RestUserDTO(
                        item.getId(),
                        item.getName(),
                        item.getEmail(),
                        item.getAge(),
                        item.getAddress(),
                        item.getGender(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCompany() != null
                                ? new RestUserDTO.CompanyUser(item.getCompany().getId(), item.getCompany().getName())
                                : null))
                .collect(Collectors.toList());
        rs.setResult(listUser);
        return rs;
    }

    public User handleGetUserById(long id) {
        Optional<User> opUser = this.userRepository.findById(id);
        if (opUser.isPresent())
            return opUser.get();
        else
            return null;
    }

    public User handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public User handleUpdateUser(User user) {
        User userUpdate = this.handleGetUserById(user.getId());
        if (userUpdate != null) {
            // check company
            Company curCompany = user.getCompany();
            if (curCompany != null) {
                Company dbCompany = this.companyService.handleGetCompanyById(curCompany.getId());
                userUpdate.setCompany(dbCompany);
            }
            userUpdate.setName(user.getName());
            userUpdate.setGender(user.getGender());
            userUpdate.setAge(user.getAge());
            userUpdate.setAddress(user.getAddress());
            return this.userRepository.save(userUpdate);
        }
        return null;
    }

    public void handleUpdateUserToken(String email, String token) {
        User user = this.handleGetUserByUsername(email);
        if (user != null) {
            user.setRefreshToken(token);
            this.userRepository.save(user);
        }
    }

    public User handleGetUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

}
