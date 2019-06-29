package tju.wbllab.system_management.service.impl;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.RoleDao;
import tju.wbllab.system_management.dao.model.Role;
import tju.wbllab.system_management.service.RoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService{
    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private RoleDao roleDao;
    /**
     * 创建角色
     *
     * @param role  角色信息
     * @return boolean
     *
     */
    @Override
    public boolean create(Role role) {
        try {
            roleDao.save(role);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("角色添加失败");
            return false;
        }
    }
    /**
     * 根据Id查询角色
     * @param id  角色id
     * @return role 角色
     */
    @Override
    public Optional<Role> findById(int id) {
        return roleDao.findById(id);
    }
    /**
     * 修改角色信息
     * @param role 用户信息
     * @return 是否修改成功
     */
    @Override
    public boolean update(Role role) {
        Optional<Role> roleById = findById(role.getId());
        try {
            if(roleById.isPresent()){
                roleDao.save(role);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            logger.error("角色修改失败" + JSONObject.fromObject(role).toString(), e);
            return false;
        }
    }
    /**
     * 修改用户中不为空的字段
     *
     * @param role 用户信息
     * @return 是否修改成功
     */
    @Override
    public boolean patch(Role role) {
        Optional<Role> roleOptional = findById(role.getId());
        if (!roleOptional.isPresent()) {
            return false;
        } else {
            Role roleDb = roleOptional.get();
            Role mergedRole = mergeInfo(role, roleDb);
            try {
                roleDao.save(mergedRole);
                return true;
            } catch (Exception e) {
                logger.error("角色修改失败", e);
                return false;
            }

        }
    }

    private Role mergeInfo(Role role, Role roleDb) {
        if (StringUtils.isNotEmpty(role.getRoleName())) {
            roleDb.setRoleName(role.getRoleName());
        }
        if (StringUtils.isNotEmpty(role.getRemark())) {
            roleDb.setRemark(role.getRemark());
        }
        if(!role.getFunctionRights().isEmpty()){
            roleDb.setFunctionRights(role.getFunctionRights());
        }
        return roleDb;

    }

    /**
     * 获取全部的角色信息
     *
     * @return 全部角色信息
     */
    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }
    /**
     * 删除指定角色
     *
     * @param id 角色id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteById(Integer id) {
        try {
            roleDao.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("角色删除失败，id为" + id, e);
            return false;
        }
    }
}
