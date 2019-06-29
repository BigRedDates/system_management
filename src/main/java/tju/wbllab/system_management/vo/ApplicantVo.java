package tju.wbllab.system_management.vo;

import tju.wbllab.system_management.dao.model.Applicant;

public class ApplicantVo extends Applicant{
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
