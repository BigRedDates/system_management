package tju.wbllab.system_management.vo;

import tju.wbllab.system_management.dao.model.User;

public class UserVo extends User {
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
