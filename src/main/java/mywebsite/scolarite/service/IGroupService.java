package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.Group;

import java.util.List;

public interface IGroupService {

    public List<Group> findAll();
    public Group findByGroupID(Long id);
    public Group addGroup(Group group);
    public void deleteGroup(Long id);
    public Group updateGroup(Long id , Group group);

}
