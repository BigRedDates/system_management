package tju.wbllab.system_management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tju.wbllab.system_management.dao.model.User;

import java.util.List;

@Repository
public interface UserDao  extends JpaRepository<User,Integer>,JpaSpecificationExecutor<User> {
    User  findByStudentID(String studentID);


}
