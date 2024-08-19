package vn.thaihoc.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void handleCreateUser(User user) {
        this.userRepository.save(user);
    }
}
