package vn.thaihoc.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thaihoc.jobhunter.domain.Permission;
import vn.thaihoc.jobhunter.domain.Role;
import vn.thaihoc.jobhunter.domain.response.ResultPaginationDTO;
import vn.thaihoc.jobhunter.repository.PermissionRepository;
import vn.thaihoc.jobhunter.util.error.IdInvalidException;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExist(Permission permission) {
        return this.permissionRepository.existsByModuleAndApiPathAndMethod(permission.getModule(),
                permission.getApiPath(),
                permission.getMethod());
    }

    public Permission handleCreatePermission(Permission permission) throws IdInvalidException {
        if (isPermissionExist(permission)) {
            throw new IdInvalidException("Permission already exists");

        }
        return this.permissionRepository.save(permission);
    }

    public Permission handleUpdatePermission(Permission permission) throws IdInvalidException {
        Permission permissionInDB = this.getPermissionById(permission.getId());
        if (permissionInDB == null) {
            throw new IdInvalidException("Permission with id = " + permission.getId() + " does not exist");
        }
        if (!(permissionInDB.getModule().equals(permission.getModule()) &&
                permissionInDB.getApiPath().equals(permission.getApiPath()) &&
                permissionInDB.getMethod().equals(permission.getMethod()))
                && isPermissionExist(permission)) {
            throw new IdInvalidException("Permission already exist");
        }
        permissionInDB.setName(permission.getName());
        permissionInDB.setApiPath(permission.getApiPath());
        permissionInDB.setMethod(permission.getMethod());
        permissionInDB.setModule(permission.getModule());
        return this.permissionRepository.save(permissionInDB);
    }

    private Permission getPermissionById(Long id) {
        return this.permissionRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO handleGetAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermission.getTotalPages());
        mt.setTotal(pagePermission.getTotalElements());
        rs.setMeta(mt);
        List<Permission> listPermission = pagePermission.getContent()
                .stream().map(Permission -> this.getPermissionById(Permission.getId()))
                .collect(Collectors.toList());
        rs.setResult(listPermission);
        return rs;
    }

    public List<Permission> getPermissionsByIds(List<Long> permissionIds) {
        return this.permissionRepository.findAllById(permissionIds);
    }

    public void handleDeletePermission(Long id) {
        Permission permission = this.getPermissionById(id);
        List<Role> roles = permission.getRoles();
        roles.forEach(role -> {
            role.getPermissions().remove(permission);
        });
        this.permissionRepository.deleteById(id);
    }
}
