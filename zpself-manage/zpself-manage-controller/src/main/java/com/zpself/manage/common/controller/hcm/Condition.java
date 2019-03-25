package com.zpself.manage.common.controller.hcm;

public class Condition {
	
    private String code;
	
    private String name;
	
    private String type;
	
    private Operator operator;
	
    private Object value;

    public Condition() {
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @SuppressWarnings("unchecked")
	public <T> T getValue() {
        return (T) this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}