package vn.thaihoc.jobhunter.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import vn.thaihoc.jobhunter.domain.User;
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

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();
    }

    public User handleGetUserById(long id) {
        Optional<User> opUser = this.userRepository.findById(id);
        if (opUser.isPresent())
            return opUser.get();
        else
            return null;
    }

    public User handleUpdateUser(User user) {
        User userupdate = this.handleGetUserById(user.getId());
        if (userupdate != null) {
            userupdate.setEmail(user.getEmail());
            userupdate.setName(user.getName());
            userupdate.setPassword(user.getPassword());
            return this.userRepository.save(userupdate);
        }
        return null;
    }
}
