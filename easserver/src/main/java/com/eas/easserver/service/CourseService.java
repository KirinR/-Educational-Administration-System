package com.eas.easserver.service;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;

import java.sql.Date;
import java.util.List;

public interface CourseService {
    //获取所有课程服务,返回所有课程
    List<Course> getAll();
    //获取教师课程服务,返回uid为teacher_id的用户教授的课程
    List<Course> getByTeacher(Integer teacher_id);
    //获取课程服务,返回相应cid的课程
    Course get(Integer cid);
    //更新/开设课程,add参数表示更新还是开设课程,返回更新/开设后的课程cid
    Integer update(Integer cid, String name, Integer uid, String lessons, String classroom, String ctype, Date session, Short max, Short credit, Boolean add);
    //删除课程服务,删除相应cid的课程,同时删除相应选课记录
    Integer delete(Integer cid);
    //设置课程选课状态服务,将cid课程的可选课状态为selectable
    boolean setSelectable(Integer cid,Boolean selectable);
    //获取课表服务,根据选课记录获取用户的课表
    List<Course> getCoursesBySCs(List<SC> scs);
}
