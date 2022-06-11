package com.eas.easserver.service;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface SCService {
    //获取某课程的选课记录服务,返回cid为course_id的课程的所有选课记录
    List<SC> getCourseSCs(Integer course_id);
    //获取所有选课记录服务,返回所有选课记录
    List<SC> getAll();
    //获取选课记录服务,返回scid的选课记录
    SC get(Integer scid);
    //获取选课记录服务,根据学生student_id,和课程course_id来获得相应选课记录
    SC get(Integer student_id, Integer course_id);
    //选课服务,为student_id学生添加一条course_id课程的选课记录
    Integer add(Integer student_id, Integer course_id);
    //删除选课记录(退课)服务,删除scid的选课记录
    Integer delete(Integer scid);
    //设置成绩服务,设置scid课程记录的平时成绩usual_grade,期末成绩final_grade,总成绩grade和绩点GPA
    Integer setGrade(Integer scid, Float usual_grade, Float final_grade, Float grade, Float GPA);
    //获取已选课人数服务,返回cid课程已选课的人数
    Integer getSelected(Integer cid);
    //获取个人选课记录服务,返回已登录学生的选课记录
    List<SC> getMySCs(HttpSession session);
    //获取教师班级选课记录服务,返回已登录教师开设的课程的选课记录
    List<SC> getTeacherSCs(HttpSession session);
}
