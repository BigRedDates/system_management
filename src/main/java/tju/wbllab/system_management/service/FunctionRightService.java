package tju.wbllab.system_management.service;

import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.model.FunctionRight;

import java.util.List;
import java.util.Optional;
@Service
public interface FunctionRightService {
    /**
     * 创建权限
     *
     * @param functionRight  权限信息
     * @return boolean
     *
     */
    boolean create(FunctionRight functionRight);

    /**
     * 根据Id查询权限
     * @param id  权限id
     * @return role 权限
     */
    Optional<FunctionRight> findById(int id);
    /**
     * 修改权限信息
     * @param functionRight 权限信息
     * @return 是否修改成功
     */
    boolean update(FunctionRight functionRight);
    /**
     * 修改用户中不为空的字段
     *
     * @param role 权限信息
     * @return 是否修改成功
     */
    boolean patch(FunctionRight role);

    /**
     * 获取全部的权限信息
     *
     * @return 全部权限信息
     */
    List<FunctionRight> findAll();


    /**
     * 删除指定权限
     *
     * @param id 权限id
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);


}
