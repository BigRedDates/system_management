package tju.wbllab.system_management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tju.wbllab.system_management.dao.model.FunctionRight;
@Repository
public interface FunctionRightDao extends JpaRepository<FunctionRight,Integer>,JpaSpecificationExecutor<FunctionRight> {
}
