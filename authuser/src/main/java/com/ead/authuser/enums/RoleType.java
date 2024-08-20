package com.ead.authuser.enums;

import lombok.Getter;

public enum RoleType {

    ROLE_STUDENT(UserType.STUDENT),
    ROLE_INSTRUCTOR(UserType.INSTRUCTOR),
    ROLE_ADMIN(UserType.ADMIN),
    ROLE_USER(UserType.USER);

    @Getter
    private UserType userType;

    RoleType(UserType userType) {
        this.userType = userType;
    }

}
