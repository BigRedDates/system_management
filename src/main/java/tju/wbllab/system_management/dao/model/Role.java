package tju.wbllab.system_management.dao.model;

import javax.persistence.*;
import java.util.List;

/**
 * 角色实体
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String roleName;
    private String remark;
    @ManyToMany(targetEntity = FunctionRight.class,fetch = FetchType.EAGER)
    private List<FunctionRight> functionRights;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<FunctionRight> getFunctionRights() {
        return functionRights;
    }
    public void setFunctionRights(List<FunctionRight> functionRights) {
        this.functionRights = functionRights;
    }
}
