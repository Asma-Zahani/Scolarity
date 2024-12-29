package mywebsite.scolarite.service.IMPL;

import jakarta.transaction.Transactional;
import mywebsite.scolarite.entity.Group;
import mywebsite.scolarite.entity.Subject;
import mywebsite.scolarite.repository.GroupRepository;
import mywebsite.scolarite.repository.SubjectRepository;
import mywebsite.scolarite.service.IGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GroupService implements IGroupService{
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Group findByGroupID(Long id) {
        return groupRepository.findByGroupId(id);
    }

    public Group addGroup(Group Group) {
        return groupRepository.save(Group);
    }
    public void deleteGroup(Long id) {
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
