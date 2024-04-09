package com.keita.restapi.user;

import java.util.Arrays;
import java.util.List;

public enum Role {
    // USER is only authorized to read items
    USER(Arrays.asList(Permission.READ_ITEMS)),

    // ADMIN is authorized to read items and post items
    ADMIN(Arrays.asList(Permission.READ_ITEMS, Permission.SAVE_ITEMS));

    private List<Permission> permissions;

    Role(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> getPermissions) {
        this.permissions = permissions;
    }


}
