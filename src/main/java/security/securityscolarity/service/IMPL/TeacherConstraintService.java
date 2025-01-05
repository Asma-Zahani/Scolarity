package security.securityscolarity.service.IMPL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.securityscolarity.entity.*;
import security.securityscolarity.repository.TeacherConstraintRepository;
import security.securityscolarity.repository.TeacherRepository;
import security.securityscolarity.repository.UniversityRepository;
import security.securityscolarity.service.ITeacherConstraintService;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherConstraintService implements ITeacherConstraintService {

    @Autowired
    private TeacherConstraintRepository teacherConstraintRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<TeacherConstraint> findAll() {
        return teacherConstraintRepository.findAll();
    }

    public List<TeacherConstraint> findTeacherConstraintByTeacherUniversity(University university) {
        return teacherConstraintRepository.findTeacherConstraintByTeacherUniversity(university);
    }

    public List<TeacherConstraint> findTeacherConstraintByTeacherUniversityId(Long id) {
        return teacherConstraintRepository.findTeacherConstraintByTeacherUniversity(universityService.findByUniversityID(id));
    }

    @Override
    public TeacherConstraint findByTeacherConstraintID(Long id) {
        Optional<TeacherConstraint> constraint = teacherConstraintRepository.findById(id);
        if (constraint.isPresent()) {
            return constraint.get();
        } else {
            throw new RuntimeException("TeacherConstraint with ID " + id + " not found.");
        }
    }

    @Override
    public TeacherConstraint addTeacherConstraint(TeacherConstraint teacherConstraint) {
        return teacherConstraintRepository.save(teacherConstraint);
    }

    @Override
    public void deleteTeacherConstraint(Long id) {
        TeacherConstraint constraint = teacherConstraintRepository.findTeacherConstraintById(id);

        Teacher teacher = constraint.getTeacher();
        if (teacher != null) {
            teacher.setConstraint(null);
            teacherRepository.save(teacher);
        }
        teacherConstraintRepository.deleteById(id);
    }

    @Override
    public TeacherConstraint updateTeacherConstraint(Long id, TeacherConstraint teacherConstraint) {
        if (teacherConstraintRepository.existsById(id)) {
            teacherConstraint.setId(id); // Assurez-vous que l'entitÃ© conserve le bon ID
            return teacherConstraintRepository.save(teacherConstraint);
        } else {
            throw new RuntimeException("TeacherConstraint with ID " + id + " not found.");
        }
    }
}