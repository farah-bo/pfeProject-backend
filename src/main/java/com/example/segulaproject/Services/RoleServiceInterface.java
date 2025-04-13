package com.example.segulaproject.Services;

import com.example.segulaproject.Entities.Enum.RoleUser;
import com.example.segulaproject.Entities.Role;

import java.util.List;

public interface RoleServiceInterface {
    void addRole(RoleUser roleUser);
    void deleteRole(RoleUser roleUser);
    List<Role> getAllRoles();
    void AddALLRoles();
}
