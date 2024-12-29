package mywebsite.scolarite.service.IMPL;

import mywebsite.scolarite.entity.Student;
import mywebsite.scolarite.entity.Teacher;
import mywebsite.scolarite.entity.University;
import mywebsite.scolarite.repository.UniversityRepository;
import mywebsite.scolarite.service.IUniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService implements IUniversityService{
    @Autowired
    UniversityRepository universityRepository;

    public List<University> findAll() {
        return universityRepository.findAll();
    }

    public University findByUniversityID(Long id) {
        return universityRepository.findByUniversityId(id);
    }

    public University addUniversity(University university) {
        return universityRepository.save(university);
    }
    public void deleteUniversity(Long id) {
        University university = universityRepository.findByUniversityId(id);
        if (university.getTeachers() != null) {
            for (Teacher teacher : university.getTeachers()) {
                teacher.setUniversity(null);
            }
        }
        if (university.getStudents() != null) {
            for (Student student : university.getStudents()) {
                student.setUniversity(null);
            }
        }
        universityRepository.deleteById(id);
    }

    public University updateUniversity(Long id , University university) {
        University universityToUpdate = this.findByUniversityID(id);
        universityToUpdate.setUniversityName(university.getUniversityName());
        universityToUpdate.setUniversityDescription(university.getUniversityDescription());
        return universityRepository.save(universityToUpdate);
    }
}
