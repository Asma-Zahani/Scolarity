package security.securityscolarity.service.IMPL;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.GroupRepository;
import security.securityscolarity.repository.SubjectRepository;
import security.securityscolarity.service.IGroupService;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GroupService implements IGroupService{
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private UniversityService universityService;

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public List<Group> findByUniversity(University university) {
        return groupRepository.findByUniversity(university);
    }

    public List<Group> findByUniversityId(Long id) {
        return groupRepository.findByUniversity(universityService.findByUniversityID(id));
    }

    public Group findByGroupID(Long id) {
        return groupRepository.findByGroupId(id);
    }

    public Group addGroup(Group Group) {
        return groupRepository.save(Group);
    }

    public Group addGroupByUniversity(Group group, Long universityID) {
        group.setUniversity(universityService.findByUniversityID(universityID));
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        Group group = groupRepository.findByGroupId(id);
        if (group.getSubGroups() != null) {
            for (SubGroup subGroup : group.getSubGroups()) {
                subGroup.setGroup(null);
            }
        }
        groupRepository.deleteById(id);
    }

    public Group updateGroup(Long id , Group Group) {
        Group groupToUpdate = groupRepository.findByGroupId(id);
        groupToUpdate.setGroupId(id);
        groupToUpdate.setGroupName(Group.getGroupName());
        groupToUpdate.setGroupDescription(Group.getGroupDescription());
        groupToUpdate.setLearnersCount(Group.getLearnersCount());
        groupToUpdate.setDepartment(Group.getDepartment());
        groupToUpdate.setGroupYear(Group.getGroupYear());
        return groupRepository.save(groupToUpdate);
    }

    public void assignSubjectsToGroup(Set<Subject> subjects, Group group) {
        for (Subject subject : subjects) {
            subject.getGroups().add(group);
            group.getSubjects().add(subject);
        }
        groupRepository.save(group);
        subjectRepository.saveAll(subjects);
    }
}
