package tju.wbllab.system_management.ctr;

import com.google.common.collect.ImmutableList;
import io.swagger.annotations.Api;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tju.wbllab.system_management.dao.model.User;
import tju.wbllab.system_management.service.RoleService;
import tju.wbllab.system_management.service.UserService;
import tju.wbllab.system_management.utils.DesEncrypter;
import tju.wbllab.system_management.utils.LabFileUtils;
import tju.wbllab.system_management.utils.PageModel;
import tju.wbllab.system_management.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Api(value = "申请人管理")
@RestController
@RequestMapping("labmanage/api/lab538/sm/user")
public class UserController {
    private  final UserService userService ;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private  final RoleService roleService;
    @Autowired
    public UserController(UserService userService,RoleService roleService) {
        this.userService = userService;
        this.roleService=roleService;
    }

    /**
     * 用于测试读取配置文件，与业务无关,忽略
     */
    @GetMapping("/path")
    public JSONObject readFileTest() {
        String readFile="author.json";
        //组绝对路径文件
        String absolutePath=System.getProperty("user.dir")+"/ext/conf/"+readFile;
        //相对路径
        String relativePath="/ext/conf/"+readFile;
        //resulta==resultr,,, 将工程打包为jar 包，路径容易出问题，，，打包完的路径结构为    target----
        //                                                                                        -------.jar
        //                                                                                        -------ext
        //                                                                                                  --conf
         //                                                                                                      --author.json
        //若运行环境仅为上述目录结构，路径写为绝对路径则，程序无法找到对应文件
        //若运行环境为生产环境，包含整个工程，上述结构为工程下的一个文件，则程序可以找到。
        String resulta=LabFileUtils.readConfigurationFile(absolutePath);
        String resultr=LabFileUtils.readConfigurationFile(relativePath);
        return JSONObject.fromObject(resulta);

    }

    /**
     * 用户登录
     *
     * @param userVo 用户信息
     * @return PageModel
     */
    @PostMapping("/login")
    public PageModel loginVerify(@RequestBody UserVo userVo) {
        PageModel pageModel = new PageModel();
        User user=userService.findByStudentId(userVo.getStudentID());
        ArrayList arrayList=new ArrayList();
        if(user==null){
            pageModel.setStatus("400");
            pageModel.setMessage("用户名不存在");
            return pageModel;
        }

        try {
            if(userService.loginVerify(user.getPassword(),userVo.getPassword())){
                pageModel.setMessage("success");
                arrayList.add(userService.findByStudentId(userVo.getStudentID()));
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("密码错误");
            }

        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("登录失败");
            return pageModel;
        }

        return pageModel;

    }

    /**
     * 添加用户
     *
     * @param userVo 用户信息
     * @return PageModel
     */
    @PostMapping("/insert")
    public PageModel insertUser(@RequestBody UserVo userVo) {
        PageModel pageModel = new PageModel();
        try {
            User user=new User();
            BeanUtils.copyProperties(userVo, user);
            if(StringUtils.isNotEmpty(userVo.getRoleId())){
                user.setRole(roleService.findById(Integer.parseInt(userVo.getRoleId())).get());
            }
            DesEncrypter desEncrypter=new DesEncrypter();
            user.setPassword(desEncrypter.userEncryption(user.getPassword().getBytes(),"lab538",desEncrypter.getSalt()));
            userService.create(user);
            pageModel.setStatus("200");
            pageModel.setMessage("用户添加成功");
            return  pageModel;
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("添加失败");
            return pageModel;
        }

    }
    /**
     * 删除用户
     *
     * @param userVo 用户信息
     * @return PageModel
     */
    @DeleteMapping("/delete")
    public PageModel deleteUser(@RequestBody UserVo userVo) {
        PageModel pageModel = new PageModel();
        ArrayList arrayList=new ArrayList();
        User user=new User();
        try {
            BeanUtils.copyProperties(userVo, user);
            if(userService.findById(user.getId()).isPresent()){
                userService.deleteById(user.getId());
                pageModel.setStatus("200");
                pageModel.setMessage("删除用户成功");
                arrayList.add(user);
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("用户不存在" );
                return  pageModel;
            }
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("删除失败");
            return pageModel;
        }
    }
    /**
     * 修改用户全部信息
     *
     * @param userVo 用户信息
     * @return PageModel
     */
    @PostMapping("/update")
    public PageModel updateUser(@RequestBody UserVo userVo) {
        PageModel pageModel = new PageModel();
        ArrayList arrayList=new ArrayList();
        User user=new User();
        try {
            BeanUtils.copyProperties(userVo, user);
            if(userService.findById(user.getId()).isPresent()){
                DesEncrypter desEncrypter=new DesEncrypter();
                user.setPassword(desEncrypter.userEncryption(user.getPassword().getBytes(),"lab538",desEncrypter.getSalt()));
                if(StringUtils.isNotEmpty(userVo.getRoleId())){
                    user.setRole(roleService.findById(Integer.parseInt(userVo.getRoleId())).get());
                }
                userService.update(user);
                pageModel.setStatus("200");
                pageModel.setMessage("修改用户成功");
                arrayList.add(user);
                pageModel.setResult(arrayList);
                return  pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("用户不存在" );
                return  pageModel;
            }
        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setStatus("修改失败");
            return pageModel;
        }
    }
    /**
     * 修改用户指定信息
     *
     * @param userVo 用户信息
     * @return PageModel
     */
    @PatchMapping("/patch")
    public PageModel patchUser(@RequestBody UserVo userVo) {
        PageModel pageModel = new PageModel();
        if (userVo == null || userVo.getId() == null) {
            pageModel.setStatus("400");
            pageModel.setMessage("参数id必填");
            return pageModel;
        }
        User user = new User();
        try {
            BeanUtils.copyProperties(userVo, user);
            if(StringUtils.isNotEmpty(userVo.getRoleId())){
                user.setRole(roleService.findById(Integer.parseInt(userVo.getRoleId())).get());
            }
            if(userService.patch(user)){
                pageModel.setResult(JSONArray.fromObject(ImmutableList.of(userService.findById(user.getId()).get())));
                return pageModel;
            }else{
                pageModel.setStatus("400");
                pageModel.setMessage("未找到该用户");
                return pageModel;
            }

        } catch (Exception e) {
            pageModel.setStatus("500");
            pageModel.setMessage("修改用户信息失败");
            return pageModel;
        }
    }
    /**
     * 修改用户密码
     *
     * @param  id  用户id
     * @param  password  密码
     * @return PageModel
     */
    @GetMapping("/updatepsw")
    public boolean updateUserPsw(@PathVariable String id,@PathVariable String password) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        if (password == null || password.isEmpty()) {
            return false;
        }
        try{
            if(userService.findById(Integer.valueOf(id)).isPresent()){
                User user=userService.findById(Integer.valueOf(id)).get();
                DesEncrypter desEncrypter=new DesEncrypter();
                user.setPassword(desEncrypter.userEncryption(password.getBytes(),"lab538",desEncrypter.getSalt()));
                userService.update(user);
                return true;
            }else{
                return  false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @RequestMapping("/session")
    public @ResponseBody
    User getUserBySession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (User) session.getAttribute("user");

    }

    /**
     * 查询全部用户
     * @return PageModel
     */
    @GetMapping("/all")
    public PageModel findAll() {
        PageModel pageModel = new PageModel();
        try{
        List<User> users=userService.findAll();
        List<Object> resultList = new ArrayList<>(users);
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
