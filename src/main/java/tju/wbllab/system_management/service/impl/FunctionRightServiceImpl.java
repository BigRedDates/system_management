package tju.wbllab.system_management.service.impl;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tju.wbllab.system_management.dao.FunctionRightDao;
import tju.wbllab.system_management.dao.model.FunctionRight;
import tju.wbllab.system_management.service.FunctionRightService;

import java.util.List;
import java.util.Optional;
@Service
public class FunctionRightServiceImpl implements FunctionRightService {

    @Autowired
    private FunctionRightDao functionDao;

    private final static Logger logger = LoggerFactory.getLogger(FunctionRightServiceImpl.class);
    /**
     * 创建权限
     *
     * @param functionRight  权限信息
     * @return boolean
     *
     */
    @Override
    public boolean create(FunctionRight functionRight) {
        try {
            functionDao.save(functionRight);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("权限添加失败");
            return false;
        }
    }
    /**
     * 根据Id查询权限
     * @param id  权限id
     * @return role 权限
     */
    @Override
    public Optional<FunctionRight> findById(int id) {
        return functionDao.findById(id);
    }
    /**
     * 修改权限信息
     * @param functionRight 权限信息
     * @return 是否修改成功
     */
    @Override
    public boolean update(FunctionRight functionRight) {
        Optional<FunctionRight> roleById = findById(functionRight.getId());
        try {
            if(roleById.isPresent()){
                functionDao.save(functionRight);
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            logger.error("权限修改失败" + JSONObject.fromObject(functionRight).toString(), e);
            return false;
        }
    }
    /**
     * 修改权限中不为空的字段
     *
     * @param functionRight 权限信息
     * @return 是否修改成功
     */
    @Override
    public boolean patch(FunctionRight functionRight) {
        Optional<FunctionRight> functionRightOptional = findById(functionRight.getId());
        if (!functionRightOptional.isPresent()) {
            return false;
        } else {
            FunctionRight functionDb = functionRightOptional.get();
            FunctionRight mergedFunction = mergeInfo(functionRight, functionDb);
            try {
                functionDao.save(mergedFunction);
                return true;
            } catch (Exception e) {
                logger.error("权限修改失败", e);
                return false;
            }

        }
    }

    private FunctionRight mergeInfo(FunctionRight functionRight, FunctionRight functionDb) {
        if (StringUtils.isNotEmpty(functionRight.getName())) {
            functionDb.setName(functionRight.getName());
        }
        if (StringUtils.isNotEmpty(functionRight.getRemark())) {
            functionDb.setRemark(functionRight.getRemark());
        }
        return functionDb;

    }

    /**
     * 获取全部的权限信息
     *
     * @return 全部权限信息
     */
    @Override
    public List<FunctionRight> findAll() {
        return functionDao.findAll();
    }
    /**
     * 删除指定权限
     *
     * @param id 权限id
     * @return 是否删除成功
     */
    @Override
    public boolean deleteById(Integer id) {
        try {
            functionDao.deleteById(id);
            return true;
        } catch (Exception e) {
            logger.error("权限删除失败，id为" + id, e);
            return false;
        }
    }

}
