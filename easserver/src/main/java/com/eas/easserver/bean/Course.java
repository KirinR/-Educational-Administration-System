package com.eas.easserver.bean;

import java.sql.Date;
import java.util.Objects;

public class Course {
    Integer cid;
    String name;
    Integer teacher_id;
    String teacher_name;
    String lessons;
    String classroom;
    String ctype;
    Date session;
    Short max;
    Short credit;
    Boolean selectable;

    public static final String COURSE_TYPES[]={"专业必修","专业选修","公共必修","公共选修"};

    public Course(Integer cid, String name, Integer teacher_id, String teacher_name, String lessons, String classroom, String ctype, Date session, Short max, Short credit, Boolean selectable) {
        this.cid = cid;
        this.name = name;
        this.teacher_id = teacher_id;
        this.teacher_name = teacher_name;
        this.lessons = lessons;
        this.classroom = classroom;
        this.ctype = ctype;
        this.session = session;
        this.max = max;
        this.credit = credit;
        this.selectable = selectable;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(Integer teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getLessons() {
        return lessons;
    }

    public void setLessons(String lessons) {
        this.lessons = lessons;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public Date getSession() {
        return session;
    }

    public void setSession(Date session) {
        this.session = session;
    }

    public Short getMax() {
        return max;
    }

    public void setMax(Short max) {
        this.max = max;
    }

    public Short getCredit() {
        return credit;
    }

    public void setCredit(Short credit) {
        this.credit = credit;
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Course course = (Course) object;
        return Objects.equals(cid, course.cid) &&
                Objects.equals(name, course.name) &&
                Objects.equals(teacher_id, course.teacher_id) &&
                Objects.equals(teacher_name, course.teacher_name) &&
                Objects.equals(lessons, course.lessons) &&
                Objects.equals(classroom, course.classroom) &&
                Objects.equals(ctype, course.ctype) &&
                Objects.equals(session, course.session) &&
                Objects.equals(max, course.max) &&
                Objects.equals(credit, course.credit) &&
                Objects.equals(selectable, course.selectable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cid, name, teacher_id, teacher_name, lessons, classroom, ctype, session, max, credit, selectable);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Course{");
        sb.append("cid=").append(cid);
        sb.append(", name='").append(name).append('\'');
        sb.append(", teacher_id=").append(teacher_id);
        sb.append(", teacher_name='").append(teacher_name).append('\'');
        sb.append(", lessons='").append(lessons).append('\'');
        sb.append(", classroom='").append(classroom).append('\'');
        sb.append(", ctype='").append(ctype).append('\'');
        sb.append(", session=").append(session);
        sb.append(", max=").append(max);
        sb.append(", credit=").append(credit);
        sb.append(", selectable=").append(selectable);
        sb.append('}');
        return sb.toString();
    }
}
