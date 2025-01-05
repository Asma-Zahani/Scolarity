package security.securityscolarity.service;

import security.securityscolarity.entity.UniversityAdmin;

import java.util.List;

public interface IUniversityAdminService {

    List<UniversityAdmin> findAll();
    UniversityAdmin findByUniversityAdminID(Long id);
    UniversityAdmin addUniversityAdmin(UniversityAdmin universityAdmin);
    void deleteUniversityAdmin(Long id);
    UniversityAdmin updateUniversityAdmin(Long id , UniversityAdmin universityAdmin);
}
