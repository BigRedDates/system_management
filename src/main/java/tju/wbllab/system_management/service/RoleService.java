package tju.wbllab.system_management.service;

import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.model.Role;
import tju.wbllab.system_management.utils.PageModel;

import java.util.List;
import java.util.Optional;

/**
 * 角色管理服务层接口
 * @author mmj
 */
@Service
public interface RoleService {
    /**
     * 创建角色
     *
     * @param role  角色信息
     * @return boolean
     *
     */
    boolean create(Role role);

    /**
     * 根据Id查询角色
     * @param id  角色id
     * @return role 角色
     */
    Optional<Role> findById(int id);
    /**
     * 修改角色信息
     * @param role 用户信息
     * @return 是否修改成功
     */
    boolean update(Role role);
    /**
     * 修改用户中不为空的字段
     *
     * @param role 用户信息
     * @return 是否修改成功
     */
    boolean patch(Role role);

    /**
     * 获取全部的角色信息
     *
     * @return 全部角色信息
     */
    List<Role> findAll();


    /**
     * 删除指定角色
     *
     * @param id 角色id
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);





}
