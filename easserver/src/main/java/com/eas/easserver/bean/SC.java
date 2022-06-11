package com.eas.easserver.bean;

import java.util.Objects;

public class SC {
    Integer scid;
    Integer student_id;
    String student_name;
    Integer course_id;
    String course_name;
    Float usual_grade;
    Float final_grade;
    Float grade;
    Float GPA;

    public SC(Integer scid, Integer student_id, String student_name, Integer course_id, String course_name, Float usual_grade, Float final_grade, Float grade, Float GPA) {
        this.scid = scid;
        this.student_id = student_id;
        this.student_name = student_name;
        this.course_id = course_id;
        this.course_name = course_name;
        this.usual_grade = usual_grade;
        this.final_grade = final_grade;
        this.grade = grade;
        this.GPA = GPA;
    }

    public Integer getScid() {
        return scid;
    }

    public void setScid(Integer scid) {
        this.scid = scid;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Float getUsual_grade() {
        return usual_grade;
    }

    public void setUsual_grade(Float usual_grade) {
        this.usual_grade = usual_grade;
    }

    public Float getFinal_grade() {
        return final_grade;
    }

    public void setFinal_grade(Float final_grade) {
        this.final_grade = final_grade;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public Float getGPA() {
        return GPA;
    }

    public void setGPA(Float GPA) {
        this.GPA = GPA;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SC sc = (SC) object;
        return Objects.equals(scid, sc.scid) &&
                Objects.equals(student_id, sc.student_id) &&
                Objects.equals(student_name, sc.student_name) &&
                Objects.equals(course_id, sc.course_id) &&
                Objects.equals(course_name, sc.course_name) &&
                Objects.equals(usual_grade, sc.usual_grade) &&
                Objects.equals(final_grade, sc.final_grade) &&
                Objects.equals(grade, sc.grade) &&
                Objects.equals(GPA, sc.GPA);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scid, student_id, student_name, course_id, course_name, usual_grade, final_grade, grade, GPA);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SC{");
        sb.append("scid=").append(scid);
        sb.append(", student_id=").append(student_id);
        sb.append(", student_name='").append(student_name).append('\'');
        sb.append(", course_id=").append(course_id);
        sb.append(", course_name='").append(course_name).append('\'');
        sb.append(", usual_grade=").append(usual_grade);
        sb.append(", final_grade=").append(final_grade);
        sb.append(", grade=").append(grade);
        sb.append(", GPA=").append(GPA);
        sb.append('}');
        return sb.toString();
    }
}
