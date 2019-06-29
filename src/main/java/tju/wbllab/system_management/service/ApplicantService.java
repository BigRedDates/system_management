package tju.wbllab.system_management.service;


import tju.wbllab.system_management.dao.model.Applicant;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.vo.ApplicantCondition;
import tju.wbllab.system_management.vo.ApplicantVo;

import java.util.List;
import java.util.Optional;

/**
 * 申请人的服务层
 */
public interface ApplicantService {



    /**
     * 用户注册接口，申请注册
     *
     * @param applicant  申请人信息
     * @return boolean
     *
     */
    boolean register(Applicant applicant);



    /**
     * 删除申请人
     *
     * @param id 申请人id
     * @return 是否删除成功
     */
    boolean deleteById(Integer id);

    /**
     * 分页查询接口
     *
     * @param condition 申请人检索条件
     * @return 分页数据
     */
    PageModel queryByPage(ApplicantCondition condition);


    /**
     *管理员审核申请人
     * @param  applicant 申请人信息
     * @return boolean
     *
     */
    boolean updateApplicantState(Applicant applicant);

    /**
     * 根据Id查询申请人
     * @param id  申请人id
     * @return applicant 申请人
     */
    Optional<Applicant> findById(int id);

    /**
     * 获取全部的申请人信息
     *
     * @return 全部申请人信息
     */
    List<Applicant> findAll();
}
