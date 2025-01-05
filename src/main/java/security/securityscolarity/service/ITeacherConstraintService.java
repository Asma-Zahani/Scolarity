package security.securityscolarity.service;

import security.securityscolarity.entity.Building;
import security.securityscolarity.entity.TeacherConstraint;
import security.securityscolarity.entity.University;

import java.util.List;

public interface ITeacherConstraintService {

    List<TeacherConstraint> findAll();
    List<TeacherConstraint> findTeacherConstraintByTeacherUniversity(University university);
    List<TeacherConstraint> findTeacherConstraintByTeacherUniversityId(Long id);
    TeacherConstraint findByTeacherConstraintID(Long id);
    TeacherConstraint addTeacherConstraint(TeacherConstraint teacherConstraint);
    void deleteTeacherConstraint(Long id);
    TeacherConstraint updateTeacherConstraint(Long id , TeacherConstraint teacherConstraint);
}
