package security.securityscolarity.service.IMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Room;
import security.securityscolarity.entity.Student;
import security.securityscolarity.entity.SubGroup;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.SubGroupRepository;
import security.securityscolarity.service.ISubGroupService;

import java.util.List;

@Service
public class SubGroupService implements ISubGroupService{

    @Autowired
    SubGroupRepository subGroupRepository;
    @Autowired
    private UniversityService universityService;

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

    public SubGroup addSubGroup(SubGroup SubGroup) {
        return subGroupRepository.save(SubGroup);
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
}
