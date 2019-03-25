package com.zpself.manage.common.controller.hcm;

public class Sort {
	
    private String field;
	
    private Sort.OrderStyle style;

    public Sort(String field, Sort.OrderStyle style) {
        this.style = Sort.OrderStyle.ASC;
        this.field = field;
        this.style = style;
    }

    public Sort() {
        this.style = Sort.OrderStyle.ASC;
    }

    public static Sort asc(String field) {
        return new Sort(field, Sort.OrderStyle.ASC);
    }

    public static Sort desc(String field) {
        return new Sort(field, Sort.OrderStyle.DESC);
    }

    public static Sort add(String field, Sort.OrderStyle style) {
        return new Sort(field, style);
    }

    public boolean isAsc() {
        return Sort.OrderStyle.ASC.equals(this.style);
    }

    public String toString() {
        return this.field + " " + this.style;
    }

    public int hashCode() {
        int result = 1;
         result = result * this.getField().hashCode();
        result *= this.style.hashCode();
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Sort)) {
            return false;
        } else {
            Sort s = (Sort)obj;
            return s.style == this.style && s.getField() != null && s.getField().equals(this.field);
        }
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setAscStyle() {
        this.style = Sort.OrderStyle.ASC;
    }

    public void setDescStyle() {
        this.style = Sort.OrderStyle.DESC;
    }

    public Sort.OrderStyle getStyle() {
        return this.style;
    }

    public static enum OrderStyle {
        ASC,
        DESC;

        private OrderStyle() {
        }
    }
}
