package com.tech.sv.calentra.auth_service.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tech.sv.calentra.auth_service.dtos.requests.PermissionRequestDTO;
import com.tech.sv.calentra.auth_service.dtos.requests.RoleRequestDTO;
import com.tech.sv.calentra.auth_service.dtos.responses.PermissionResponseDTO;
import com.tech.sv.calentra.auth_service.dtos.responses.RoleResponseDTO;
import com.tech.sv.calentra.auth_service.entities.Permission;
import com.tech.sv.calentra.auth_service.entities.Role;
import com.tech.sv.calentra.auth_service.mappers.PermissionMapper;
import com.tech.sv.calentra.auth_service.mappers.RoleMapper;
import com.tech.sv.calentra.auth_service.services.PermissionService;
import com.tech.sv.calentra.auth_service.services.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class RolePermissionController {
	
	private final RoleService roleService;
    private final PermissionService permissionService;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

	/* 
    |=================
    | ROLE CRUD
    |=================
 	*/

    @PostMapping("/roles")
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody RoleRequestDTO request) {
    	Role role = roleMapper.toRole(request);
    	role = roleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toResponseDTO(role));
    }

    @PutMapping("/roles/{roleId}")
	public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable UUID roleId, @Valid @RequestBody RoleRequestDTO request) {
    	Role role = roleMapper.toRole(request);
    	role = roleService.update(roleId, request);
        return ResponseEntity.ok(roleMapper.toResponseDTO(role));
    }

    @DeleteMapping("/roles/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles/{roleId}")
    public ResponseEntity<Role> getRole(@PathVariable UUID roleId) {
        return ResponseEntity.ok(roleService.getById(roleId));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> listRoles() {
        return ResponseEntity.ok(roleService.listAll());
    }

	/* 
    |=================
    | PERMISSION CRUD
    |=================
 	*/

    @PostMapping("/permissions")
    public ResponseEntity<PermissionResponseDTO> createPermission(@Valid @RequestBody PermissionRequestDTO request) {
    	Permission permission = permissionMapper.toPermission(request);
    	permission = permissionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(permissionMapper.toPermissionResponseDTO(permission));
    }

    @PutMapping("/permissions/{id}")
	public ResponseEntity<PermissionResponseDTO> updatePermission(@PathVariable UUID id, @Valid @RequestBody PermissionRequestDTO request) {
    	Permission permission = permissionMapper.toPermission(request);
    	permission = permissionService.update(id, request);
        return ResponseEntity.ok(permissionMapper.toPermissionResponseDTO(permission));
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable UUID id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<PermissionResponseDTO> getPermission(@PathVariable UUID id) {
    	Permission permission = permissionService.getById(id);
        return ResponseEntity.ok(permissionMapper.toPermissionResponseDTO(permission));
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> listPermissions() {
        return ResponseEntity.ok(permissionService.listAll());
    }
    
	/* 
    |=================
    | ASSIGNMENT
    |=================
 	*/
   
    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Role> attachPermission(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        return ResponseEntity.ok(roleService.attachPermission(roleId, permissionId));
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Role> detachPermission(
            @PathVariable UUID roleId,
            @PathVariable UUID permissionId) {
        return ResponseEntity.ok(roleService.detachPermission(roleId, permissionId));
    }
}
