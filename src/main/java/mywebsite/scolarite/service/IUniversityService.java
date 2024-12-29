package mywebsite.scolarite.service;

import mywebsite.scolarite.entity.University;

import java.util.List;

public interface IUniversityService {

    public List<University> findAll();
    public University findByUniversityID(Long id);
    public University addUniversity(University university);
    public void deleteUniversity(Long id);
    public University updateUniversity(Long id , University university);

}
