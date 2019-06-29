package tju.wbllab.system_management.vo;

import tju.wbllab.system_management.dao.model.Role;

public class RoleVo extends Role{
    String functions;

    public String getFunctions() {
        return functions;
    }

    public void setFunctions(String functions) {
        this.functions = functions;
    }
}
