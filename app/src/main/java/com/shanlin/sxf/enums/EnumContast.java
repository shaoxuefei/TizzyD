package com.shanlin.sxf.enums;

/**
 * @Author: SXF
 * @CreateDate: 2020/12/3 20:06
 */
public enum EnumContast {
    ONE("Excutor");

    private String valueName;
    EnumContast(String valueName) {
        this.valueName = valueName;
    }
    public String getValueName() {
        return valueName;
    }
}
