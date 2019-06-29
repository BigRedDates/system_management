package tju.wbllab.system_management.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.ApplicantDao;
import tju.wbllab.system_management.dao.model.Applicant;
import tju.wbllab.system_management.service.ApplicantService;
import tju.wbllab.system_management.utils.DesEncrypter;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.vo.ApplicantCondition;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicantServiceImpl implements ApplicantService {
    private final ApplicantDao dao;
    private final static Logger logger = LoggerFactory.getLogger(ApplicantServiceImpl.class);
    @Autowired
    public ApplicantServiceImpl(ApplicantDao dao) {
        this.dao = dao;
    }
    /**
     * 用户注册接口，申请注册
     *
     * @param applicant  申请人信息
     * @return boolean
     *
     */
    @Override
   public boolean register(Applicant applicant){
        try {
             DesEncrypter desEncrypter=new DesEncrypter();
             applicant.setPassword(desEncrypter.userEncryption(applicant.getPassword().getBytes(),"lab538",desEncrypter.getSalt()));
            dao.save(applicant);
            return true;
        } catch (Exception e) {
            logger.error("注册失败", e);
            return false;
        }

    }


    /**
     * 删除申请人
     *
     * @param id 任务id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteById(Integer id){
        try {
            dao.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("申请人删除失败，id为" + id, e);
            return false;
        }
    }

    /**
     * 分页查询接口
     *
     * @param condition 申请人检索条件
     * @return 分页数据
     */
    @Override
    public PageModel queryByPage(ApplicantCondition condition){
        return new PageModel();
    }

    /**
     *修改申请人申请状态
     * @param  applicant 申请人信息
     * @return boolean
     *
     */
    @Override
    public  boolean updateApplicantState(Applicant applicant){
        Optional<Applicant> applicantOptional = findById(applicant.getId());
        if (!applicantOptional.isPresent()) {
            return false;
        } else {
               Applicant applicantDb=applicantOptional.get();
               applicantDb.setApplicantState(applicant.getApplicantState());
            try {
                dao.save(applicantDb);
                return true;
            } catch (Exception e) {
                logger.error("申请人状态修改失败", e);
                return false;
            }
        }
    }
    /**
     * 根据Id查询申请人
     * @param id  申请人id
     * @return applicant 申请人
     */
    @Override
     public Optional<Applicant> findById(int id){
        return dao.findById(id);

    }

    /**
     * 获取全部的申请人信息
     *
     * @return 全部申请人信息
     */
    @Override
    public List<Applicant> findAll(){
        return dao.findAll();
    }

}
