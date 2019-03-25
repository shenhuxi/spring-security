package com.zpself.manage.common.controller.hcm;


import com.zpself.manage.common.controller.util.DateUtils;
import com.zpself.manage.common.controller.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* Title: SearchCondition 
* Description:  查询条件封装对象
* @author dicky  
* @date 2018年9月13日 下午2:45:57
 */
public class SearchCondition {
	private static final String TYPE_CONSTANT_DATE = "Date";
    protected int pageNumber = 1;
    protected int pageSize = 10;
	
    List<Condition> conditionList;
    
    List<Sort> sortList;

	boolean autoCount;
	
    public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public SearchCondition() {
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}



	public List<Condition> getConditionList() {
        return this.conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }

    public List<Sort> getSortList() {
        return this.sortList;
    }

    public void setSortList(List<Sort> sortList) {
        this.sortList = sortList;
    }

    public String getInString(String inKey, String field) {
    	StringBuilder subQ = new StringBuilder();
        subQ.append("(");
        if (!"".equals(inKey) && inKey != null) {
            String[] inKeys = inKey.split(",");
            if (inKeys.length > 1000) {
                for(int i = 0; i < inKeys.length; ++i) {
                    if ((i + 1) % 1000 == 0) {
                        String temp = subQ.substring(0, subQ.length() - 1);
                        subQ.delete(0, subQ.length());
                        subQ.append(temp);
                        subQ.append(") or ");
                        subQ.append(field);
                        subQ.append(" in (");
                        subQ.append(inKeys[i] + ",");
                    } else {
                        subQ.append(inKeys[i] + ",");
                    }
                }

                String temp = subQ.substring(0, subQ.length() - 1);
                subQ.delete(0, subQ.length());
                subQ.append(temp);
                subQ.append(")");
            } else {
                subQ.append(inKey);
                subQ.append(")");
            }
        } else {
            subQ.append(")");
        }

        return subQ.toString();
    }

    /**
     * 获取排序
     * @return
     */
    public String getSortString() {
        if (this.sortList.isEmpty()) {
            return "";
        } else {
        	StringBuilder s = new StringBuilder(" order by");
            
            for (Sort sort : this.sortList) {
            	 s.append(" ");
                 s.append(sort.getField());
                 s.append(" ");
                 s.append(sort);

			}
            return s.toString();
        }
    }

    public void addSort(Sort sort) {
        this.sortList.add(sort);
    }

    /**
     * 获取查询参数
     * @return
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getParameterMap() {
        Map<String, Object> map = new HashMap<>();
        if(this.getConditionList()!=null && !this.getConditionList().isEmpty()) {
            for (Condition condition : conditionList) {
                String key = "";
                key = getSearchKey(condition);
                Object value = condition.getValue();
                if(value instanceof String && StringUtils.isBlank(value.toString())){
                    continue;
                }
                if(value instanceof Map) {
                    Map<String,Object> valueMap = new HashMap<>();
                    valueMap =  (Map<String, Object>) value ;
                    value  = valueMap.get("value");
                }
                if(TYPE_CONSTANT_DATE.equalsIgnoreCase(condition.getType())){
                    value = DateUtils.parseDate(value);
                }
                map.put(key+condition.getCode(), value);
            }
        }
        return map;
    }

    /**
     * 获取查询参数(不带操作)
     * @return
     */
    public Map<String, Object> getParameterValueMap() {
        Map<String, Object> map = new HashMap<>();
        if(this.getConditionList()!=null && !this.getConditionList().isEmpty()) {
            for (Condition condition : conditionList) {
                if(TYPE_CONSTANT_DATE.equals(condition.getType())){
                    map.put(condition.getCode(),  DateUtils.parseDate(condition.getValue()));
                }else {
                    map.put(condition.getCode(), condition.getValue());
                }
            }
        }
        return map;
    }

    
    /**
     * 获取查询字段名
     * @param condition
     * @return
     */
    private static String getSearchKey(Condition condition) {
    	String key = "";
        switch(condition.getOperator()) {
        case EQ:
            key = "EQ_";
            break;
        case GT:
            key = "GT_";
            break;
        case GTE:
            key = "GTE_";
            break;
        case LT:
            key = "LT_";
            break;
        case LTE:
            key = "LTE_";
            break;
        case NEQ:
            key = "NEQ_";
            break;
        case RLIKE:
        	key = "RLIKE_";
        	 break;
        case LLIKE:
        	key = "LLIKE_";
        	 break;
        case LIKE:
            key = "LIKE_";
            break;
        case IN:
        	key = "IN_";
        	break;
        }
    
    return key;
    }
}
