package security.securityscolarity.service;

import security.securityscolarity.entity.Admin;

import java.util.List;

public interface IAdminService {

    List<Admin> findAll();
    Admin findByAdminID(Long id);
    Admin addAdmin(Admin admin);
    void deleteAdmin(Long id);
    Admin updateAdmin(Long id , Admin admin);
}
