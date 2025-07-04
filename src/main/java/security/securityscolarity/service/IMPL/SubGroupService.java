package security.securityscolarity.service.IMPL;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.GroupRepository;
import security.securityscolarity.repository.StudentRepository;
import security.securityscolarity.repository.SubGroupRepository;
import security.securityscolarity.service.ISubGroupService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubGroupService implements ISubGroupService{

    @Autowired
    SubGroupRepository subGroupRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;

    public List<SubGroup> findAll() {
        return subGroupRepository.findAll();
    }

    public SubGroup findBySubGroupID(Long id) {
        return subGroupRepository.findBySubGroupId(id);
    }

    public List<SubGroup> findByGroupUniversity(University university) {
        return subGroupRepository.findByGroupUniversity(university);
    }

    public List<SubGroup> findByGroupUniversityId(Long id) {
        return subGroupRepository.findByGroupUniversity(universityService.findByUniversityID(id));
    }

    @Transactional
    public SubGroup addSubGroup(SubGroup subGroup) {
        // Filtrer uniquement les étudiants qui existent déjà dans la base de données
        Set<Student> managedStudents = new HashSet<>();

        if (subGroup.getStudents() != null) {
            for (Student s : subGroup.getStudents()) {
                if (s.getId() != null) {
                    studentRepository.findById(s.getId()).ifPresent(managedStudents::add);
                }
            }
        }
        subGroup.setStudents(managedStudents);
        subGroup = subGroupRepository.save(subGroup);

        for (Student student : managedStudents) {
            student.setSubGroup(subGroup);
            studentRepository.save(student);
        }

        return subGroup;
    }
    public void deleteSubGroup(Long id) {
        SubGroup subGroup = findBySubGroupID(id);
        if (subGroup.getStudents() != null) {
            for (Student student : subGroup.getStudents()) {
                student.setSubGroup(null);
            }
        }
        subGroupRepository.deleteById(id);
    }

    public SubGroup updateSubGroup(Long id , SubGroup subGroup) {
        SubGroup subGroupToUpdate = subGroupRepository.findBySubGroupId(id);
        subGroupToUpdate.setSubGroupId(id);
        subGroupToUpdate.setSubGroupName(subGroup.getSubGroupName());
        subGroupToUpdate.setSubGroupDescription(subGroup.getSubGroupDescription());
        subGroupToUpdate.setLearnersCount(subGroup.getLearnersCount());
        subGroupToUpdate.setGroup(subGroup.getGroup());
        return subGroupRepository.save(subGroupToUpdate);
    }

    public void clearStudents(Long subGroupId) {
        SubGroup subGroup = subGroupRepository.findBySubGroupId(subGroupId);

        subGroup.getStudents().forEach(student -> student.setSubGroup(null));

        subGroupRepository.save(subGroup);
    }

    public SubGroup addSubGroupByGroup(SubGroup subGroup, Long id) {
        subGroup.setGroup(groupRepository.findByGroupId(id));

        Set<Student> managedStudents = new HashSet<>();

        if (subGroup.getStudents() != null) {
            for (Student s : subGroup.getStudents()) {
                if (s.getId() != null) {
                    studentRepository.findById(s.getId()).ifPresent(managedStudents::add);
                }
            }
        }
        subGroup.setStudents(managedStudents);

        subGroup = subGroupRepository.save(subGroup);

        for (Student student : managedStudents) {
            student.setSubGroup(subGroup);
            studentRepository.save(student);
        }
        return subGroup;
    }
}
