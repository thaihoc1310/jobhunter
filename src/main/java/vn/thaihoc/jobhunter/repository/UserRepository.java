package vn.thaihoc.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.thaihoc.jobhunter.domain.Company;
import vn.thaihoc.jobhunter.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String email);

    public boolean existsByEmail(String email);

    public User findByRefreshTokenAndEmail(String refreshToken, String email);

    public List<User> findByCompany(Company company);
}
