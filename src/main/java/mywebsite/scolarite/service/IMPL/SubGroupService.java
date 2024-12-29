package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.SubGroup;
import mywebsite.scolarite.repository.GroupRepository;
import mywebsite.scolarite.repository.SubGroupRepository;
import mywebsite.scolarite.service.ISubGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubGroupService implements ISubGroupService{

    @Autowired
    SubGroupRepository subGroupRepository;

    public List<SubGroup> findAll() {
        return subGroupRepository.findAll();
    }

    public SubGroup findBySubGroupID(Long id) {
        return subGroupRepository.findBySubGroupId(id);
    }

    public SubGroup addSubGroup(SubGroup SubGroup) {
        return subGroupRepository.save(SubGroup);
    }
    public void deleteSubGroup(Long id) {
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
