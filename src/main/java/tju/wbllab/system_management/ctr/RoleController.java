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
import tju.wbllab.system_management.dao.model.Role;
import tju.wbllab.system_management.service.FunctionRightService;
import tju.wbllab.system_management.service.RoleService;
import tju.wbllab.system_management.service.RoleService;
import tju.wbllab.system_management.utils.DesEncrypter;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.utils.RedisClient;
import tju.wbllab.system_management.vo.RoleVo;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Api(value = "角色管理")
@RestController
@RequestMapping("labmanage/api/lab538/sm/role")
public class RoleController {
    private  final FunctionRightService functionRightService;
    private  final RoleService roleService;
    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
    @Value("${ftp.IP:10}")
    private String ftpIp;
    @Value("${server.port:10.0.4.101}")
    private String port;
    private RedisClient redisClient;
    @Autowired
    public RoleController(RoleService roleService,FunctionRightService functionRightService) {
        this.roleService=roleService;
        this.functionRightService=functionRightService;
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @PostConstruct
    public void init(){
        redisClient=RedisClient.getInstance("127.0.0.1","6379", "123456");
        logger.info("");
    }

    /**
     * 测试系统与系统功能无关
     *
     *
     *
     */
    @PostMapping("/config")
    public String ceshiConfig() {
        /*Set<String> keys = redisClient.getKeys();
        String key="";
        for (String k:keys) {
            key+=k;
        }*/
        return  port+ftpIp;

    }

    /**
     * 添加角色
     *
     * @param roleVo 角色
     * @return PageModel
     */
    @PostMapping("/insert")
    public PageModel insertRole(@RequestBody RoleVo roleVo) {
        PageModel pageModel = new PageModel();
        try {
            Role role=new Role();
            BeanUtils.copyProperties(roleVo, role);
            role.setFunctionRights(getFunctionList(roleVo.getFunctions()));
            roleService.create(role);
            pageModel.setStatus("200");
            pageModel.setMessage("角色添加成功");
            return  pageModel;
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("添加失败");
            return pageModel;
        }

    }

    private List<FunctionRight> getFunctionList(String functions) {
        List<FunctionRight>  functionRightList=new ArrayList<FunctionRight>();
        String[] functionArray = functions.split(",");
        for (int i = 0; i <functionArray.length ; i++) {
            Optional<FunctionRight> byId = functionRightService.findById(Integer.parseInt(functionArray[i]));
            if(byId.isPresent()) {
                functionRightList.add(byId.get());
            }
        }
        return functionRightList;
    }

    /**
     * 删除角色
     *
     * @param roleVo 角色信息
     * @return PageModel
     */
    @DeleteMapping("/delete")
    public PageModel deleteRole(@RequestBody RoleVo roleVo) {
        PageModel pageModel = new PageModel();
        ArrayList arrayList=new ArrayList();
        Role role=new Role();
        try {
            BeanUtils.copyProperties(roleVo, role);
            if(roleService.findById(role.getId()).isPresent()){
                roleService.deleteById(role.getId());
                pageModel.setStatus("200");
                pageModel.setMessage("删除角色成功");
                arrayList.add(role);
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("角色不存在" );
                return  pageModel;
            }
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("添加失败");
            return pageModel;
        }
    }
    /**
     * 修改角色全部信息
     *
     * @param roleVo 角色信息
     * @return PageModel
     */
    @PostMapping("/update")
    public PageModel updateRole(@RequestBody RoleVo roleVo) {
        PageModel pageModel = new PageModel();
        ArrayList arrayList=new ArrayList();
        Role role=new Role();
        try {
            BeanUtils.copyProperties(roleVo, role);
            if(roleService.findById(role.getId()).isPresent()){
                role.setFunctionRights(getFunctionList(roleVo.getFunctions()));
                roleService.update(role);
                pageModel.setStatus("200");
                pageModel.setMessage("修改角色成功");
                arrayList.add(role);
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("角色不存在" );
                return  pageModel;
            }
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("修改失败");
            return pageModel;
        }
    }
    /**
     * 修改角色指定信息
     *
     * @param roleVo 角色信息
     * @return PageModel
     */
    @PatchMapping("/patch")
    public PageModel patchRole(@RequestBody RoleVo roleVo) {
        PageModel pageModel = new PageModel();
        if (roleVo == null || roleVo.getId() == null) {
            pageModel.setStatus("400");
            pageModel.setMessage("参数id必填");
            return pageModel;
        }
        Role role=new Role();
        try {
            BeanUtils.copyProperties(roleVo, role);
            role.setFunctionRights(getFunctionList(roleVo.getFunctions()));
            if(roleService.patch(role)){
                pageModel.setResult(JSONArray.fromObject(ImmutableList.of(roleService.findById(role.getId()).get())));
                return pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("未找到该角色");
                return pageModel;
            }

        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setMessage("修改角色信息失败");
            return pageModel;
        }
    }

    /**
     * 查询全部角色
     * @return PageModel
     */
    @GetMapping("/all")
    public PageModel findAll() {
        PageModel pageModel = new PageModel();
        try{
            List<Role> roles=roleService.findAll();
            List<Object> resultList = new ArrayList<>(roles);
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
