package com.shanlin.sxf.bean;

/**
 * Created by shaoxuefei on 2020/4/22.
 */
public class HookPersonBean {

    private String name;
    private String sex;
    private int age;

    public HookPersonBean() {
    }

    public HookPersonBean(String name) {
        this.name = name;
    }

    private HookPersonBean(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public void getUserInfo() {

    }

    private String getPersonSex(boolean needChange) {

        return sex;
    }

    public String getUserNameAge() {

        return String.format("年龄：%s,姓名：%s", age, name);
    }
}
