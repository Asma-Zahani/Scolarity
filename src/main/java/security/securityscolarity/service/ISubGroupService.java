package security.securityscolarity.service;

import security.securityscolarity.entity.SubGroup;
import security.securityscolarity.entity.University;

import java.util.List;

public interface ISubGroupService {

    List<SubGroup> findAll();
    SubGroup findBySubGroupID(Long id);
    List<SubGroup> findByGroupUniversity(University university);
    List<SubGroup> findByGroupUniversityId(Long universityId);
    SubGroup addSubGroup(SubGroup subGroup);
    void deleteSubGroup(Long id);
    SubGroup updateSubGroup(Long id , SubGroup subGroup);
}
