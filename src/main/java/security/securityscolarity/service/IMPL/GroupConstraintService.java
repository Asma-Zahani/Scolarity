package security.securityscolarity.service.IMPL;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.Group;
import security.securityscolarity.entity.GroupConstraint;
import security.securityscolarity.entity.TeacherConstraint;
import security.securityscolarity.entity.University;
import security.securityscolarity.repository.GroupConstraintRepository;
import security.securityscolarity.repository.GroupRepository;
import security.securityscolarity.service.IGroupConstraintService;

import java.util.List;
import java.util.Optional;

@Service
public class GroupConstraintService implements IGroupConstraintService {

    @Autowired
    private GroupConstraintRepository groupConstraintRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private GroupRepository groupRepository;

    @Override
    public List<GroupConstraint> findAll() {
        return groupConstraintRepository.findAll();
    }

    public List<GroupConstraint> findGroupConstraintByGroupUniversity(University university) {
        return groupConstraintRepository.findGroupConstraintByGroupUniversity(university);
    }

    public List<GroupConstraint> findGroupConstraintByGroupUniversityId(Long id) {
        return groupConstraintRepository.findGroupConstraintByGroupUniversity(universityService.findByUniversityID(id));
    }

    @Override
    public GroupConstraint findByGroupConstraintID(Long id) {
        Optional<GroupConstraint> constraint = groupConstraintRepository.findById(id);
        if (constraint.isPresent()) {
            return constraint.get();
        } else {
            throw new RuntimeException("GroupConstraint with ID " + id + " not found.");
        }
    }

    @Override
    public GroupConstraint addGroupConstraint(GroupConstraint groupConstraint) {
        return groupConstraintRepository.save(groupConstraint);
    }

    @Override
    public void deleteGroupConstraint(Long id) {
        GroupConstraint constraint = groupConstraintRepository.findGroupConstraintById(id);

        Group group = constraint.getGroup();
        if (group != null) {
            group.setConstraint(null);
            groupRepository.save(group);
        }
        groupConstraintRepository.deleteById(id);
    }

    @Override
    public GroupConstraint updateGroupConstraint(Long id, GroupConstraint groupConstraint) {
        if (groupConstraintRepository.existsById(id)) {
            groupConstraint.setId(id);
            return groupConstraintRepository.save(groupConstraint);
        } else {
            throw new RuntimeException("GroupConstraint with ID " + id + " not found.");
        }
    }
}