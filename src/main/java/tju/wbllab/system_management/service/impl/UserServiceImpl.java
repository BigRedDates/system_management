package tju.wbllab.system_management.service.impl;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.UserDao;
import tju.wbllab.system_management.dao.model.User;
import tju.wbllab.system_management.service.UserService;
import tju.wbllab.system_management.utils.DesEncrypter;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.vo.UserCondition;

import java.util.List;
import java.util.Optional;

/**
 * 用户管理接口实现层
 * @author mmj
 */
@Service
public class UserServiceImpl implements UserService {
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserDao userDao;

    /**
     * 创建用户
     * @param user  用户信息
     * @return    boolean
     */
    @Override
    public boolean create(User user) {
        try {
            userDao.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据id查询用户
     * @param id  用户id
     * @return   用户实体
     */
    @Override
    public Optional<User> findById(int id) {
        return userDao.findById(id);
    }

    /**
     * 修改用户,全部字段修改
     * @param user 用户信息
     * @return
     */
    @Override
    public boolean update(User user) {
        Optional<User> userById = findById(user.getId());
        try {
            if(userById.isPresent()){
                userDao.save(user);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            logger.error("用户修改失败" + JSONObject.fromObject(user).toString(), e);
            return false;
        }
    }

    @Override
    public boolean patch(User user) {
        Optional<User> userOptional = findById(user.getId());
        if (!userOptional.isPresent()) {
            return false;
        } else {
            User userDb = userOptional.get();
            User mergedUser = mergeInfo(user, userDb);
            try {
                userDao.save(mergedUser);
                return true;
            } catch (Exception e) {
                logger.error("用户修改失败", e);
                return false;
            }

        }
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public PageModel queryByPage(UserCondition condition) {
        return null;
    }

    @Override
    public boolean deleteById(Integer id) {
        try {
            userDao.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("用户删除失败，id为" + id, e);
            return false;
        }
    }

    @Override
    public boolean updateState(Integer id, String state) {
        return false;
    }

    @Override
    public List<User> findAllByState(String state) {
        return null;
    }

    /**
     * 根据学号查询用户
     * @param studentId 学号
     * @return boolean
     */
    public User findByStudentId(String studentId){
        try {

           return userDao.findByStudentID(studentId);
        } catch (Exception e) {
            logger.error("查询用户学号失败" +studentId , e);
            return new User();
        }


    }

    /**
     * 判断输入密码是否一致
     * @param userPassword   用户密码
     * @param password   输入密码
     * @return    boolen
     */
    @Override
    public  boolean loginVerify(String userPassword,String password){
        DesEncrypter desEncrypter=new DesEncrypter();
        if(userPassword.equals(desEncrypter.userEncryption(password.getBytes(),"lab538",desEncrypter.getSalt()))){
            return true;
        }else {
            return false;
        }
    }

    private User mergeInfo(User user, User userDb) {
        if (StringUtils.isNotEmpty(user.getAge())) {
            userDb.setAge(user.getAge());
        }
        if (StringUtils.isNotEmpty(user.getEmail())) {
            userDb.setEmail(user.getEmail());
        }
        if (StringUtils.isNotEmpty(user.getEnrollmentYear())) {
            userDb.setEnrollmentYear(user.getEnrollmentYear());
        }
        if (StringUtils.isNotEmpty(user.getGender())) {
            userDb.setGender(user.getGender());
        }
        if (StringUtils.isNotEmpty(user.getName())) {
            userDb.setName(user.getName());
        }
        if (StringUtils.isNotEmpty(user.getPhoneNumber())) {
            userDb.setPhoneNumber(user.getPhoneNumber());
        }
        if (StringUtils.isNotEmpty(user.getPassword())) {
            DesEncrypter desEncrypter=new DesEncrypter();
            user.setPassword(desEncrypter.userEncryption(user.getPassword().getBytes(),"lab538",desEncrypter.getSalt()));
            userDb.setPassword(user.getPassword());
        }
        if (user.getRole()!=null) {
            userDb.setRole(user.getRole());
        }
        if (StringUtils.isNotEmpty(user.getStudentID())) {
            userDb.setStudentID(user.getStudentID());
        }
        return userDb;
    }
}
