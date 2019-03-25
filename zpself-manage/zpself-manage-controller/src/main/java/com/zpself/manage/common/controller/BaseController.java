package com.zpself.manage.common.controller;


import com.zpself.manage.common.controller.hcm.SearchCondition;
import com.zpself.manage.modules.redis.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * common controller
 *
 * @author shixh
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final String USER_NULL_MSG = "用户没有登陆,请重新登陆!";

    @Autowired
    public HttpServletRequest request;

    @Resource
    public RedisService redisService;




    /**
     * 封装分页信息
     *
     * @param pageNumber
     * @param pagzSize
     * @param sort
     * @return
     * @author shixh
     */
    public PageRequest buildPageRequest(int pageNumber, int pagzSize, Sort sort) {
        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }

    /**
     * 封装分页信息
     *
     * @param pageNumber
     * @param pagzSize
     * @return
     * @author shixh
     */
    public PageRequest buildPageRequest(int pageNumber, int pagzSize) {
        return new PageRequest(pageNumber - 1, pagzSize);
    }

    /**
     * 封装分页信息
     *
     * @param pageNumber
     * @param pagzSize
     * @param sort
     * @param sortType
     * @return
     * @author shixh
     */
    public PageRequest buildPageRequest(int pageNumber, int pagzSize, String sort, String sortType) {
        Sort s;
        if (StringUtils.isBlank(sort) || StringUtils.isBlank(sortType)) {
            s = new Sort(Sort.Direction.ASC, "id");
        } else {
            s = new Sort(Sort.Direction.fromString(sortType), sort);
        }
        return new PageRequest(pageNumber - 1, pagzSize, s);
    }

    /**
     * 封装分页信息
     *
     * @param searchParams
     * @return
     * @author shixh
     */
    public PageRequest buildPageRequest(Map<String, Object> searchParams) {
        String pageNum = (String) searchParams.get(CommonConstant.PAGE_PAGENUM);
        String pageSize = (String) searchParams.get(CommonConstant.PAGE_PAGESIZE);
        String sortType = (String) searchParams.get(CommonConstant.PAGE_SORTTYPE);
        String sort = (String) searchParams.get(CommonConstant.PAGE_SORT);
        if (StringUtils.isBlank(pageNum)) {
            pageNum = CommonConstant.PAGE_1;
        }
        if (StringUtils.isBlank(pageSize)) {
            pageSize = CommonConstant.PAGE_10;
        }
        searchParams.remove(CommonConstant.PAGE_PAGENUM);
        searchParams.remove(CommonConstant.PAGE_PAGESIZE);
        searchParams.remove(CommonConstant.PAGE_SORTTYPE);
        searchParams.remove(CommonConstant.PAGE_SORT);
        return buildPageRequest(Integer.parseInt(pageNum), Integer.parseInt(pageSize), sort, sortType);
    }

    public PageRequest buildPageRequest(SearchCondition condition) {
        if (condition.getPageNumber() < 1) {
            condition.setPageNumber(CommonConstant.PAGE_PAGENUM_DEFAULT);
        }
        if (condition.getPageSize() < 1) {
            condition.setPageSize(CommonConstant.PAGE_PAGESIZE_DEFAULT);
        }
        List<Sort.Order> orders = new ArrayList<>();
        List<com.zpself.manage.common.controller.hcm.Sort> sortList = condition.getSortList();
        if (null != sortList && !sortList.isEmpty()) {
            for (com.zpself.manage.common.controller.hcm.Sort sort : sortList) {
                orders.add(new Sort.Order(Direction.fromString(sort.getStyle().name()), sort.getField()));
            }
        } else {
            orders.add(new Sort.Order(Direction.ASC, "id"));
        }
        return new PageRequest(condition.getPageNumber() - 1, condition.getPageSize(), new Sort(orders));
    }


    /**
     * 请求参数转MAP
     *
     * @return
     * @author shixh
     */
    public Map<String, Object> getParameterMap() {
        Map<String, String[]> properties = request.getParameterMap();
        Map<String, Object> returnMap = new HashMap<>();
        Iterator<Map.Entry<String, String[]>> entries = properties.entrySet().iterator();
        Map.Entry<String, String[]> entry;
        String name = "";
        Object value = "";
        while (entries.hasNext()) {
            entry = entries.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String valueStr = "";
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    valueStr = values[i] + ",";
                }
                value = valueStr.substring(0, valueStr.length() - 1);
            } else {
                value = valueObj;
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }


}
