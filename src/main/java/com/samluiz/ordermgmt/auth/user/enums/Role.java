package com.samluiz.ordermgmt.auth.user.enums;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ROLE_ADMIN,
    ROLE_VIEWER,
    ROLE_EDITOR;

    public static List<String> getRoles() {
        return Arrays.asList(ROLE_ADMIN.name(), ROLE_VIEWER.name(), ROLE_EDITOR.name());
    }
}
