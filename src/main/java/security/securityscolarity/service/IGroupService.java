package security.securityscolarity.service;

import security.securityscolarity.entity.Group;
import security.securityscolarity.entity.Subject;
import security.securityscolarity.entity.University;

import java.util.List;
import java.util.Set;

public interface IGroupService {

    List<Group> findAll();
    List<Group> findByUniversity(University university);
    List<Group> findByUniversityId(Long universityId);
    Group findByGroupID(Long id);
    Group addGroup(Group group);
    void deleteGroup(Long id);
    Group updateGroup(Long id , Group group);
    void assignSubjectsToGroup(Set<Subject> subjects, Group group);
}
