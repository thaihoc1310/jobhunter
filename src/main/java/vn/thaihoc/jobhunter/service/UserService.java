package vn.thaihoc.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.domain.dto.Meta;
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
        rs.setResult(pageUser.getContent());
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
}
