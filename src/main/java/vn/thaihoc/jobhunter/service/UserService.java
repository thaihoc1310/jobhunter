package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.dto.Meta;
import vn.thaihoc.jobhunter.domain.dto.RestCreateUserDTO;
import vn.thaihoc.jobhunter.domain.dto.RestUpdateUserDTO;
import vn.thaihoc.jobhunter.domain.dto.RestUserDTO;
import vn.thaihoc.jobhunter.domain.dto.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public RestCreateUserDTO convertToRestCreateUserDTO(User user) {
        RestCreateUserDTO restCreateUserDTO = new RestCreateUserDTO();
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
        Meta mt = new Meta();
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
                        item.getUpdatedAt()))
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
