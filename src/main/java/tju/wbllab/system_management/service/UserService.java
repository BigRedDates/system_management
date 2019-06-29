package tju.wbllab.system_management.service;

import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.model.User;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.vo.UserCondition;

import java.util.List;
import java.util.Optional;

/**
 * 用户管理服务实现层
 * @author  mmj
 */
@Service
public interface UserService {
    /**
     * 创建用户
     *
     * @param user  用户信息
     * @return boolean
     *
     */
    boolean create(User user);

    /**
     * 根据Id查询用户
     * @param id  用户id
     * @return user 用户
     */
    Optional<User> findById(int id);
    /**
     * 根据学号查询用户
     * @param studentId  学号
     * @return user
     */
    User findByStudentId(String studentId);
    /**
     * 修改用户信息
     * @param user 用户信息
     * @return 是否修改成功
     */
    boolean update(User user);
    /**
     * 修改用户中不为空的字段
     *
     * @param user 用户
     * @return 是否修改成功
     */
    boolean patch(User user);

    /**
     * 获取全部的用户信息
     *
     * @return 全部用户信息
     */
    List<User> findAll();

    /**
     * 分页查询接口
     *
     * @param condition 用户检索条件
     * @return 分页数据
     */
    PageModel queryByPage(UserCondition condition);

    /**
     * 删除指定用户
     *
     * @param id 任务id
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);
    /**
     * 更新用户状态
     * @param id 任务id
     * @param state 修改的用户状态
     * @return 是否成功
     */
    boolean updateState(Integer id, String state);
    /**
     * 根据图书状态查询用户列表
     *
     * @param state 状态
     * @return 用户列表
     */
    List<User> findAllByState(String state);

    /**
     * 用户登录
     */
    boolean loginVerify(String userPassword,String password);



}
