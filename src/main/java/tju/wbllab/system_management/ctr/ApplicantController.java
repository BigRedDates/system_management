package tju.wbllab.system_management.ctr;


import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tju.wbllab.system_management.dao.model.Applicant;
import tju.wbllab.system_management.dao.model.User;
import tju.wbllab.system_management.service.ApplicantService;
import tju.wbllab.system_management.service.RoleService;
import tju.wbllab.system_management.service.UserService;
import tju.wbllab.system_management.service.impl.ApplicantStatusEnum;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.vo.ApplicantVo;

import java.util.ArrayList;
import java.util.List;

@Api(value = "申请人管理")
@RestController
@RequestMapping("labmanage/api/lab538/sm/applicant")
public class ApplicantController {
    private  final UserService userService ;
    private  final RoleService roleService;
    private static final String BAD_REQUEST_MESSAGE = "请核对参数是否存在错误";
    private  final  ApplicantService applicantService;
    private final static Logger logger = LoggerFactory.getLogger(ApplicantController.class);

    @Autowired
    public ApplicantController(UserService userService, ApplicantService applicantService,RoleService roleService) {
        this.userService = userService;
        this.applicantService = applicantService;
        this.roleService=roleService;
    }

    /**
     * 用户注册，申请注册
     *
     * @param applicantVo 申请人信息
     * @return PageModel
     */
    @PostMapping("/register")
    public PageModel register(@RequestBody ApplicantVo applicantVo) {
        PageModel pageModel = new PageModel();
        Applicant applicant = new Applicant();
        if(!StringUtils.isNotEmpty(applicantVo.getStudentID())){
            pageModel.setMessage("学号不能为空");
            pageModel.setStatus("400");
            return pageModel;
        }else if(!StringUtils.isNotEmpty(applicantVo.getPassword())){
            pageModel.setMessage("密码不能为空");
            pageModel.setStatus("400");
            return pageModel;
        }else if(userService.findByStudentId(applicantVo.getStudentID())!=null){
            pageModel.setMessage("用户已存在，请修改");
            pageModel.setStatus("400");
            return pageModel;
        }
        try {
            applicant=parserApplicant(applicantVo);
        } catch (Exception e) {
            pageModel.setStatus("400");
            pageModel.setMessage(BAD_REQUEST_MESSAGE);
            return pageModel;
        }
        if (applicantService.register(applicant)) {
            List<Object> resultList = new ArrayList<>();
            resultList.add(applicant);
            pageModel.setResult(resultList);
            return pageModel;
        } else {
            pageModel.setStatus("500");
            pageModel.setMessage("申请失败");
        }
        return pageModel;

    }

    private Applicant parserApplicant(ApplicantVo applicantVo) {
        Applicant applicant=new Applicant();
        BeanUtils.copyProperties(applicantVo, applicant);
        if(StringUtils.isNotEmpty(applicantVo.getRoleId())){
            applicant.setRole(roleService.findById(Integer.parseInt(applicantVo.getRoleId())).get());
        }
        return  applicant;
    }


    /**
     * 管理员审核申请人
     *
     * @param applicantVo 申请人信息
     * @return PageModel
     */
    @PostMapping("/reviewApplicant")
    public PageModel reviewApplicant(@RequestBody ApplicantVo applicantVo) {
        PageModel result = new PageModel();
        Applicant applicant = new Applicant();
        BeanUtils.copyProperties(applicantVo, applicant);
        if (applicant.getId() == null) {
            result.setStatus("400");
            result.setMessage("修改申请人的id字段为必填");
            return result;
        }else if(!StringUtils.isNotEmpty(applicant.getApplicantState())){
            result.setStatus("400");
            result.setMessage("申请人状态必填");
            return result;
        }else if(!applicantService.findById(applicant.getId()).isPresent()){
            result.setStatus("400");
            result.setMessage("该id的申请人不存在，id:"+applicantVo.getId());
            return result;
        }
        if (applicantService.updateApplicantState(applicant)) {
            if(applicant.getApplicantState().equals(ApplicantStatusEnum.PASSED_STATUS)){
                User user =mergeInfo(applicant);
                userService.create(user);
                applicantService.deleteById(applicantVo.getId());
            }else if(applicant.getApplicantState().equals(ApplicantStatusEnum.REJECTED_STATUS)){
                applicantService.deleteById(applicantVo.getId());
                result.setMessage("已删除该申请人");
                return result;
            }
            List<Object> resultList = new ArrayList<>();
            resultList.add(applicant);
            result.setResult(resultList);
            return result;
        } else {
            result.setStatus("500");
            result.setMessage("审核失败");
            return result;
        }
    }
    /**
     * 查询全部申请人
     * @return PageModel
     */
    @GetMapping("/all")
    public PageModel findAll() {
        PageModel pageModel = new PageModel();
        try{
            List<Applicant> applicants=applicantService.findAll();
            List<Object> resultList = new ArrayList<>(applicants);
            pageModel.setResult(resultList);
            return pageModel;
        }catch (Exception e){
            e.printStackTrace();
            pageModel.setStatus("500");
            pageModel.setMessage("查询全部失败");
            return  pageModel;
        }
    }
    private User mergeInfo(Applicant applicant) {
        User user = new User();
        if (StringUtils.isNotEmpty(applicant.getAge())) {
            user.setAge(applicant.getAge());
        }

        if (StringUtils.isNotEmpty(applicant.getName())) {
            user.setName(applicant.getName());
        }
        if (StringUtils.isNotEmpty(applicant.getEnrollmentYear())) {
            user.setEnrollmentYear(applicant.getEnrollmentYear());
        }

        if (StringUtils.isNotEmpty(applicant.getPassword())) {
            user.setPassword(applicant.getPassword());
        }
        if (StringUtils.isNotEmpty(applicant.getEmail())) {
            user.setEmail(applicant.getEmail());
        }
        if (StringUtils.isNotEmpty(applicant.getGender())) {
            user.setGender(applicant.getGender());
        }
        if (StringUtils.isNotEmpty(applicant.getPhoneNumber())) {
            user.setPhoneNumber(applicant.getPhoneNumber());
        }
        if (StringUtils.isNotEmpty(applicant.getStudentID())) {
            user.setStudentID(applicant.getStudentID());
        }
        if(applicant.getRole()!=null){
            user.setRole(applicant.getRole());
        }

        return user;
    }


}
