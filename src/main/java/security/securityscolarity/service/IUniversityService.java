package security.securityscolarity.service;

import security.securityscolarity.entity.University;

import java.util.List;

public interface IUniversityService {

    List<University> findAll();
    University findByUniversityID(Long id);
    University addUniversity(University university);
    void deleteUniversity(Long id);
    University updateUniversity(Long id , University university);
    List<University> getUniversityNotAssignedToAdminUniversity();
}
