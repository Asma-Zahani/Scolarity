package security.securityscolarity.service.IMPL;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.GroupRepository;
import security.securityscolarity.repository.ScheduleRepository;
import security.securityscolarity.repository.SubGroupRepository;
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
    @Autowired
    private SubGroupService subGroupService;
    @Autowired
    private ScheduleRepository scheduleRepository;

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
                subGroupService.deleteSubGroup(subGroup.getSubGroupId());
            }
        }
        if (group.getSchedules() != null) {
            scheduleRepository.deleteAll(group.getSchedules());
        }
        groupRepository.deleteById(id);
    }

    public Group updateGroup(Long id , Group group) {
        Group groupToUpdate = groupRepository.findByGroupId(id);
        groupToUpdate.setGroupId(id);
        groupToUpdate.setGroupName(group.getGroupName());
        groupToUpdate.setGroupDescription(group.getGroupDescription());
        groupToUpdate.setLearnersCount(group.getLearnersCount());
        groupToUpdate.setDepartment(group.getDepartment());
        groupToUpdate.setGroupYear(group.getGroupYear());
        groupToUpdate.setSubjects(group.getSubjects());
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

    public void clearSubjects(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId);

        group.getSubjects().forEach(subject -> subject.getGroups().remove(group));

        groupRepository.save(group);
    }
}
