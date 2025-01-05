package security.securityscolarity.entity;

import java.io.Serializable;

public enum Role implements Serializable {

    GLOBAL_ADMIN,
    UNIVERSITY_ADMIN,
    STUDENT,
    TEACHER,
    USER
}