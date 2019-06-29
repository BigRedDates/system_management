package tju.wbllab.system_management.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * jpa的工具类
 * 主要用于分页，约束的构建，pagehelper的转换
 * @author mmj
 * @date  2019/3/7
 */
public class JpaUtils {
    private JpaUtils() {
    }

    private static final int FRONT_PAGE_OFFSET = 1;
    private static final String BOOK_STATUS = "state";


    public static PageModel convertPage2PageModel(Page page) {
        PageModel pageModel = new PageModel();
        pageModel.setResult(page.getContent());
        pageModel.setTotalNum((int) page.getTotalElements());
        pageModel.setCurrentPage(page.getNumber() + FRONT_PAGE_OFFSET);
        pageModel.setPageSize(page.getSize());
        return pageModel;
    }



    public static Pageable buildPageable(int page, int rp, String sortName, String sortOrder) {
        Sort.Direction direction;
        if ("DESC".equalsIgnoreCase(sortOrder)) {
            direction = Sort.Direction.DESC;
        } else {
            direction = Sort.Direction.ASC;
        }
        if (StringUtils.isEmpty(sortName)) {
            sortName = "id";
        }
        //jpa的分页是从0开始，而前台是从1开始的
        return PageRequest.of(page - FRONT_PAGE_OFFSET, rp, direction, sortName);
    }

    public static <T> Specification<T> buildTaskSpec(Map<String, String> con) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = buildPredicates(con, root, criteriaBuilder);
            //Predicate inTheHallState=criteriaBuilder.equal(root.get(BOOK_STATUS),BookStateEnum.BOOK_IN_THE_HALL_STATUS);
           // return criteriaBuilder.and(criteriaBuilder.and(list.toArray(new Predicate[0])),inTheHallState);
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        };
    }

    static <T> List<Predicate> buildPredicates(Map<String, String> con, Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<>();
        //将测试任务从普通查询中去掉
        for (String key : con.keySet()) {
            String value = con.get(key);
            Predicate p;
            if (key.contains("name")) {
                p = criteriaBuilder.like(root.get(key), "%" + value + "%");

            }else {
                p = criteriaBuilder.equal(root.get(key), value);
            }
            list.add(p);
        }

        return list;
    }









}
