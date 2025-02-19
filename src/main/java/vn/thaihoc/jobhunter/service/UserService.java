package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.domain.Role;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.domain.response.user.RestCreateUserDTO;
import vn.thaihoc.jobhunter.domain.response.user.RestUpdateUserDTO;
import vn.thaihoc.jobhunter.domain.response.user.RestUserDTO;
import vn.thaihoc.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, CompanyService companyService, RoleService roleService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        // check company
        Company curCompany = user.getCompany();
        if (curCompany != null) {
            Company dbCompany = this.companyService.handleGetCompanyById(curCompany.getId());
            user.setCompany(dbCompany);
        }

        // check role
        Role curRole = user.getRole();
        if (curRole != null) {
            Role dbRole = this.roleService.getRoleById(curRole.getId());
            user.setRole(dbRole);
        }
        return this.userRepository.save(user);
    }

    public RestCreateUserDTO convertToRestCreateUserDTO(User user) {
        RestCreateUserDTO restCreateUserDTO = new RestCreateUserDTO();
        RestCreateUserDTO.CompanyUser companyUser = new RestCreateUserDTO.CompanyUser();
        RestCreateUserDTO.RoleUser roleUser = new RestCreateUserDTO.RoleUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            restCreateUserDTO.setCompany(companyUser);
        }
        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            restCreateUserDTO.setRole(roleUser);
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
        RestUserDTO.RoleUser roleUser = new RestUserDTO.RoleUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            restUserDTO.setCompany(companyUser);
        }
        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            restUserDTO.setRole(roleUser);
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
        RestUpdateUserDTO.RoleUser roleUser = new RestUpdateUserDTO.RoleUser();
        if (user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            restUpdateUserDTO.setCompany(companyUser);
        }
        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            restUpdateUserDTO.setRole(roleUser);
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
                .stream().map(item -> this.convertToRestUserDTO(item))
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
            // check role
            Role curRole = user.getRole();
            if (curRole != null) {
                Role dbRole = this.roleService.getRoleById(curRole.getId());
                userUpdate.setRole(dbRole);
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

    public boolean handleCheckEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

}
