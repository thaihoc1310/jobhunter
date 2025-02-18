package vn.thaihoc.jobhunter.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Permission;
import vn.thaihoc.jobhunter.domain.Role;
import vn.thaihoc.jobhunter.domain.User;
import vn.thaihoc.jobhunter.repository.PermissionRepository;
import vn.thaihoc.jobhunter.repository.RoleRepository;
import vn.thaihoc.jobhunter.repository.UserRepository;
import vn.thaihoc.jobhunter.util.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(PermissionRepository permissionRepository, RoleRepository roleRepository,
            UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> Start initializing database");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> permissions = new ArrayList<>();
            permissions.add(new Permission("Create a user", "/api/v1/users", "POST", "USERS"));
            permissions.add(new Permission("Update a user", "/api/v1/users", "PUT", "USERS"));
            permissions.add(new Permission("Delete a user by id", "/api/v1/users/{id}", "DELETE", "USERS"));
            permissions.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET", "USERS"));
            permissions.add(new Permission("Get all users with pagination", "/api/v1/users", "GET", "USERS"));

            permissions.add(new Permission("Create a company", "/api/v1/companies", "POST", "COMPANIES"));
            permissions.add(new Permission("Update a company", "/api/v1/companies", "PUT", "COMPANIES"));
            permissions.add(new Permission("Delete a company by id", "/api/v1/companies/{id}", "DELETE", "COMPANIES"));
            permissions.add(new Permission("Get a company by id", "/api/v1/companies/{id}", "GET", "COMPANIES"));
            permissions
                    .add(new Permission("Get all companies with pagination", "/api/v1/companies", "GET", "COMPANIES"));

            permissions.add(new Permission("Create a job", "/api/v1/jobs", "POST", "JOBS"));
            permissions.add(new Permission("Update a job", "/api/v1/jobs", "PUT", "JOBS"));
            permissions.add(new Permission("Delete a job by id", "/api/v1/jobs/{id}", "DELETE", "JOBS"));
            permissions.add(new Permission("Get a job by id", "/api/v1/jobs/{id}", "GET", "JOBS"));
            permissions.add(new Permission("Get all jobs with pagination", "/api/v1/jobs", "GET", "JOBS"));

            permissions.add(new Permission("Create a skill", "/api/v1/skills", "POST", "SKILLS"));
            permissions.add(new Permission("Update a skill", "/api/v1/skills", "PUT", "SKILLS"));
            permissions.add(new Permission("Delete a skill by id", "/api/v1/skills/{id}", "DELETE", "SKILLS"));
            permissions.add(new Permission("Get all skills with pagination", "/api/v1/skills", "GET", "SKILLS"));

            permissions.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            permissions.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            permissions.add(
                    new Permission("Delete a permission by id", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            permissions.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            permissions.add(
                    new Permission("Get all permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));

            permissions.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
            permissions.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
            permissions.add(new Permission("Delete a role by id", "/api/v1/roles/{id}", "DELETE", "ROLES"));
            permissions.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET", "ROLES"));
            permissions.add(new Permission("Get all roles with pagination", "/api/v1/roles", "GET", "ROLES"));

            permissions.add(new Permission("Create a resume", "/api/v1/resumes", "POST", "RESUMES"));
            permissions.add(new Permission("Update a resume", "/api/v1/resumes", "PUT", "RESUMES"));
            permissions.add(new Permission("Delete a resume by id", "/api/v1/resumes/{id}", "DELETE", "RESUMES"));
            permissions.add(new Permission("Get a resume by id", "/api/v1/resumes/{id}", "GET", "RESUMES"));
            permissions.add(new Permission("Get all resumes with pagination", "/api/v1/resumes", "GET", "RESUMES"));

            permissions.add(new Permission("Create a subscriber", "/api/v1/subscribers", "POST", "SUBSCRIBERS"));
            permissions.add(new Permission("Update a subscriber", "/api/v1/subscribers", "PUT", "SUBSCRIBERS"));
            permissions.add(
                    new Permission("Delete a subscriber by id", "/api/v1/subscribers/{id}", "DELETE", "SUBSCRIBERS"));
            permissions.add(new Permission("Get a subscriber by id", "/api/v1/subscribers/{id}", "GET", "SUBSCRIBERS"));
            permissions.add(
                    new Permission("Get all subscribers with pagination", "/api/v1/subscribers", "GET", "SUBSCRIBERS"));

            permissions.add(new Permission("Download a file", "/api/v1/files", "GET", "FILES"));
            permissions.add(new Permission("Upload a file", "/api/v1/files", "POST", "FILES"));

            this.permissionRepository.saveAll(permissions);
        }

        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role();
            adminRole.setName("SUPER_ADMIN");
            adminRole.setPermissions(allPermissions);
            adminRole.setActive(true);
            adminRole.setDescription("full permissions for admin");
            this.roleRepository.save(adminRole);
        }

        if (countUsers == 0) {
            User admin = new User();
            admin.setEmail("thaihoc131005@gmail.com");
            admin.setPassword(this.passwordEncoder.encode("123456"));
            admin.setAge(19);
            admin.setName("Thai Hoc");
            admin.setGender(GenderEnum.MALE);

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                admin.setRole(adminRole);
            }
            this.userRepository.save(admin);
        }
        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> Database is already initialized");
        } else {
            System.out.println(">>> Database is initialized successfully");
        }
    }

}
