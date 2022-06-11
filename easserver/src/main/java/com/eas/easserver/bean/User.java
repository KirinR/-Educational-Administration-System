package com.eas.easserver.bean;

import java.sql.Date;
import java.util.Objects;

public class User {
    Integer uid;
    String name;
    Long phone;
    String department;
    String permission;
    Date regTime;

    public static final String PERMISSION_EAS_STUDENT="PERMISSION_EAS_STUDENT";
    public static final String PERMISSION_EAS_TEACHER="PERMISSION_EAS_TEACHER";
    public static final String PERMISSION_EAS_ADMINISTRATOR="PERMISSION_EAS_ADMINISTRATOR";


    public User(Integer uid, String name, Long phone, String department, String permission, Date regTime) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.department = department;
        this.permission = permission;
        this.regTime = regTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public boolean equalsWithOutRegTime(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(uid, user.uid) &&
                Objects.equals(name, user.name) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(department, user.department) &&
                Objects.equals(permission, user.permission);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(uid, user.uid) &&
                Objects.equals(name, user.name) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(department, user.department) &&
                Objects.equals(permission, user.permission) &&
                Objects.equals(regTime, user.regTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, name, phone, department, permission, regTime);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("uid=").append(uid);
        sb.append(", name='").append(name).append('\'');
        sb.append(", phone=").append(phone);
        sb.append(", department='").append(department).append('\'');
        sb.append(", permission='").append(permission).append('\'');
        sb.append(", regTime=").append(regTime);
        sb.append('}');
        return sb.toString();
    }
}
