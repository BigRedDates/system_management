package tju.wbllab.system_management.ctr;

import com.google.common.collect.ImmutableList;
import io.swagger.annotations.Api;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import tju.wbllab.system_management.dao.model.FunctionRight;
import tju.wbllab.system_management.dao.model.Role;
import tju.wbllab.system_management.service.FunctionRightService;
import tju.wbllab.system_management.service.RoleService;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.utils.RedisClient;
import tju.wbllab.system_management.vo.FunctionRightVo;

import java.util.ArrayList;
import java.util.List;
@Api(value = "权限管理")
@RestController
@RequestMapping("labmanage/api/lab538/sm/functionRight")
public class FunctionRightController {
    private  final FunctionRightService functionRightService;
    private final static Logger logger = LoggerFactory.getLogger(FunctionRightController.class);
    @Autowired
    public FunctionRightController(FunctionRightService functionRightService) {
        this.functionRightService=functionRightService;
    }

    /**
     * 添加权限
     *
     * @param functionRightVo 权限
     * @return PageModel
     */
    @PostMapping("/insert")
    public PageModel insertFR(@RequestBody FunctionRightVo functionRightVo) {
        PageModel pageModel = new PageModel();
        try {
            FunctionRight functionRight=new FunctionRight();
            BeanUtils.copyProperties(functionRightVo, functionRight);
            functionRightService.create(functionRight);
            pageModel.setStatus("200");
            pageModel.setMessage("权限添加成功");
            return  pageModel;
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("添加失败");
            return pageModel;
        }

    }
    /**
     * 删除权限
     *
     * @param functionRightVo 权限信息
     * @return PageModel
     */
    @DeleteMapping("/delete")
    public PageModel deleteFR(@RequestBody FunctionRightVo functionRightVo) {
        PageModel pageModel = new PageModel();
        ArrayList arrayList=new ArrayList();
        FunctionRight functionRight=new FunctionRight();
        try {
            BeanUtils.copyProperties(functionRightVo, functionRight);
            if(functionRightService.findById(functionRight.getId()).isPresent()){
                functionRightService.deleteById(functionRight.getId());
                pageModel.setStatus("200");
                pageModel.setMessage("删除权限成功");
                arrayList.add(functionRight);
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("权限不存在" );
                return  pageModel;
            }
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("添加失败");
            return pageModel;
        }
    }
    /**
     * 修改权限全部信息
     *
     * @param functionRightVo 权限信息
     * @return PageModel
     */
    @PostMapping("/update")
    public PageModel updateFR(@RequestBody FunctionRightVo functionRightVo) {
        PageModel pageModel = new PageModel();
        ArrayList arrayList=new ArrayList();
        FunctionRight functionRight=new FunctionRight();
        try {
            BeanUtils.copyProperties(functionRightVo, functionRight);
            if(functionRightService.findById(functionRight.getId()).isPresent()){
                functionRightService.update(functionRight);
                pageModel.setStatus("200");
                pageModel.setMessage("修改权限成功");
                arrayList.add(functionRight);
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("权限不存在" );
                return  pageModel;
            }
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("修改失败");
            return pageModel;
        }
    }
    /**
     * 修改权限指定信息
     *
     * @param functionRightVo 权限信息
     * @return PageModel
     */
    @PostMapping("/patch")
    public PageModel patchFR(@RequestBody FunctionRightVo functionRightVo) {
        PageModel pageModel = new PageModel();
        if (functionRightVo == null || functionRightVo.getId() == null) {
            pageModel.setStatus("400");
            pageModel.setMessage("参数id必填");
            return pageModel;
        }
       FunctionRight functionRight=new FunctionRight();
        try {
            BeanUtils.copyProperties(functionRightVo, functionRight);
            if(functionRightService.patch(functionRight)){
                pageModel.setResult(JSONArray.fromObject(ImmutableList.of(functionRightService.findById(functionRight.getId()).get())));
                return pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("未找到该权限");
                return pageModel;
            }

        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setMessage("修改权限信息失败");
            return pageModel;
        }
    }

    /**
     * 查询全部权限
     * @return PageModel
     */
    @GetMapping("/all")
    public PageModel findAll() {
        PageModel pageModel = new PageModel();
        try{
            List<FunctionRight> functionRights=functionRightService.findAll();
            List<Object> resultList = new ArrayList<>(functionRights);
            pageModel.setResult(resultList);
            return pageModel;
        }catch (Exception e){
            e.printStackTrace();
            pageModel.setStatus("500");
            pageModel.setMessage("查询全部失败");
            return  pageModel;
        }
    }
}
