package tju.wbllab.system_management.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import tju.wbllab.system_management.dao.model.Applicant;
@Repository
public interface ApplicantDao extends JpaRepository<Applicant,Integer>,JpaSpecificationExecutor<Applicant> {
}
