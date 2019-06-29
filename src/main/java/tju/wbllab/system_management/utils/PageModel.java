package tju.wbllab.system_management.utils;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PageModel {
    private static final String REQUIRED_ID = "id required";
    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";
    private String status;
    private int totalPage;
    private int totalNum;
    private int pageSize;
    private String message;
    private int currentPage;
    private List<Object> result;

    public PageModel() {
        this.setStatus("200");
        this.setTotalPage(0);
        this.setTotalNum(0);
        this.setPageSize(0);
        this.setMessage("backup");
        this.setCurrentPage(1);
        this.setResult((List)null);
    }
    public void setPageModelByString(String str) {
        JSONObject obj = JSONObject.fromObject(str);
        this.setStatus(obj.getString("status"));
        this.setTotalPage(obj.getInt("totalPage"));
        this.setTotalNum(obj.getInt("totalNum"));
        this.setPageSize(obj.getInt("pageSize"));
        this.setMessage(obj.getString("message"));
        this.setCurrentPage(obj.getInt("currentPage"));
        this.setResult(obj.getJSONArray("result"));
    }

    public JSONObject toJSON() {
        return JSONObject.fromObject(this);
    }

    public int countTotalPage(int pageSize, int totalNum) {
        return totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<Object> getResult() {
        return result;
    }

    public void setResult(List<Object> result) {
        this.result = result;
    }

    public boolean checkID(Long id, PageModel pm) {
        if (id == null) {
            pm.setMessage("id required");
            return true;
        } else {
            return false;
        }
    }

    public void setPageModelByMark(boolean mark) {
        if (mark) {
            this.setMessage("success");
        } else {
            this.setStatus("403");
            this.setMessage("failed");
        }

    }

    public void generatePostPM(Object object, boolean mark) {
        if (mark) {
            List<Object> list = new ArrayList();
            list.add(object);
            this.setResult(list);
            this.setMessage("success");
        } else {
            this.setStatus("403");
            this.setMessage("failed");
        }

    }
}
