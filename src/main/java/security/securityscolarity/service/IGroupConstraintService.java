package security.securityscolarity.service;

import security.securityscolarity.entity.*;

import java.util.List;

public interface IGroupConstraintService {
    List<GroupConstraint> findAll();
    List<GroupConstraint> findGroupConstraintByGroupUniversity(University university);
    List<GroupConstraint> findGroupConstraintByGroupUniversityId(Long id);
    GroupConstraint findByGroupConstraintID(Long id);
    GroupConstraint addGroupConstraint(GroupConstraint groupConstraint);
    void deleteGroupConstraint(Long id);
    GroupConstraint updateGroupConstraint(Long id , GroupConstraint groupConstraint);
}