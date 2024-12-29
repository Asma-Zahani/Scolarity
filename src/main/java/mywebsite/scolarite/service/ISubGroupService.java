package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.SubGroup;
import java.util.List;

public interface ISubGroupService {

    List<SubGroup> findAll();
    SubGroup findBySubGroupID(Long id);
    SubGroup addSubGroup(SubGroup subGroup);
    void deleteSubGroup(Long id);
    SubGroup updateSubGroup(Long id , SubGroup subGroup);
}
