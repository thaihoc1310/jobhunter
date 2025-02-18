package vn.thaihoc.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thaihoc.jobhunter.domain.Role;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.service.RoleService;
import vn.thaihoc.jobhunter.util.annotation.ApiMessage;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role newRole = this.roleService.handleCreateRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRole);
    }

    @PutMapping("")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws IdInvalidException {
        Role updatedRole = this.roleService.handleUpdateRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRole);
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch a role by id")
    public ResponseEntity<Role> fetchRoleById(@PathVariable Long id) throws IdInvalidException {
        return ResponseEntity.ok(this.roleService.handleGetRoleById(id));
    }

    @GetMapping("")
    @ApiMessage("Fetch all roles")
    public ResponseEntity<ResultPaginationDTO> fetchAllRoles(@Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity.ok(this.roleService.handleGetAllRoles(spec, pageable));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws IdInvalidException {
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
