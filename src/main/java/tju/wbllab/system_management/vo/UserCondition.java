package tju.wbllab.system_management.vo;

import org.apache.commons.lang.StringUtils;
import tju.wbllab.system_management.dao.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserCondition extends User{
    private String searchStartTime;
    private String searchEndTime;
    //排序字段，默认id
    private String sortName;
    //升序或者降序
    private String sortOrder;
    private Integer currentPage;
    private Integer pageSize;
    public UserCondition() {
        super();
        currentPage = 1;
        pageSize = 10;
        sortName = "";
        sortOrder = "DESC";
    }
    public Map<String, String> toMap() {
        Map<String, String> conditionMap = new HashMap<>(16);
        if (StringUtils.isNotEmpty(this.getSearchStartTime())) {
            conditionMap.put("searchStartTime", this.getSearchStartTime());
        }
        if (StringUtils.isNotEmpty(this.getSearchEndTime())) {
            conditionMap.put("searchEndTime", this.getSearchEndTime());
        }
        if (StringUtils.isNotEmpty(this.getAge())) {
            conditionMap.put("age", this.getAge());
        }
        if (this.getId() != null) {
            conditionMap.put("id", String.valueOf(this.getId()));
        }
        if (StringUtils.isNotEmpty(this.getName())) {
            conditionMap.put("name", this.getName());
        }
        if (StringUtils.isNotEmpty(this.getEmail())) {
            conditionMap.put("email", this.getEmail());
        }
        if (StringUtils.isNotEmpty(this.getEnrollmentYear())) {
            conditionMap.put("enrollmentYear", this.getEnrollmentYear());
        }
        if (StringUtils.isNotEmpty(this.getGender())) {
            conditionMap.put("gender", this.getGender());
        }
        if (StringUtils.isNotEmpty(this.getPhoneNumber())) {
            conditionMap.put("phoneNumber", this.getPhoneNumber());
        }
        // TODO: 2019/3/13  密码问题，

        return conditionMap;
    }

    public String getSearchStartTime() {
        return searchStartTime;
    }

    public void setSearchStartTime(String searchStartTime) {
        this.searchStartTime = searchStartTime;
    }

    public String getSearchEndTime() {
        return searchEndTime;
    }

    public void setSearchEndTime(String searchEndTime) {
        this.searchEndTime = searchEndTime;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
