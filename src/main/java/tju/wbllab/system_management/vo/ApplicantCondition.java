package tju.wbllab.system_management.vo;

import tju.wbllab.system_management.dao.model.Applicant;

public class ApplicantCondition extends Applicant{

    private String searchStartTime;
    private String searchEndTime;
    //排序字段，默认id
    private String sortName;
    //升序或者降序
    private String sortOrder;
    private Integer currentPage;
    private Integer pageSize;
    public ApplicantCondition() {
        super();
        currentPage = 1;
        pageSize = 10;
        sortName = "";
        sortOrder = "DESC";
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
