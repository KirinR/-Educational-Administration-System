package com.eas.easserver.controller;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;
import com.eas.easserver.bean.User;
import com.eas.easserver.annonation.RequestPermission;
import com.eas.easserver.service.CourseService;
import com.eas.easserver.service.SCService;
import com.eas.easserver.service.UserService;
import com.eas.easserver.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.List;

@RestController
public class CourseController {
    @Autowired
    CourseService courseService;
    @Autowired
    UserService userService;
    @Autowired
    SCService scService;
    @GetMapping("/api/course/getAll")
    @RequestPermission()
    APIResponse getCourses(){
        return APIResponse.success("课程列表获取成功",courseService.getAll());
    }
    @GetMapping("/api/course/getTeacherCourses")
    @RequestPermission()
    APIResponse getCourseByTeacher(@NonNull @RequestParam(name = "teacher_id")Integer teacher_id){
        User teacher=userService.get(teacher_id);
        if(teacher==null)return APIResponse.argError("不存在该教师");
        if(!teacher.getPermission().equals(User.PERMISSION_EAS_TEACHER))return APIResponse.argError("该用户不是教师");
        return APIResponse.success("课程获取成功",courseService.getByTeacher(teacher_id));
    }
    @GetMapping("/api/course/get")
    @RequestPermission()
    APIResponse getCourse(@NonNull @RequestParam(name = "cid")Integer cid){
        if(courseService.get(cid)==null)return APIResponse.argError("不存在该课程");
        return APIResponse.success("课程获取成功",courseService.get(cid));
    }

    @PostMapping("/api/course/delete")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse deleteCourse(@NonNull @RequestParam(name = "cid")Integer cid){
        if(courseService.get(cid)==null)return APIResponse.argError("不存在该课程");
        return APIResponse.success("删除课程成功",courseService.delete(cid));
    }

    @PostMapping("/api/course/update")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse updateCourse(
            @NonNull @RequestParam(name = "cid")Integer cid,
            @NonNull @RequestParam(name = "name")String name,
            @NonNull @RequestParam(name = "teacher_id")Integer teacher_id,
            @NonNull @RequestParam(name = "lessons")String lessons,
            @NonNull @RequestParam(name = "classroom")String classroom,
            @NonNull @RequestParam(name = "ctype")String ctype,
            @NonNull @RequestParam(name = "session")Date session,
            @NonNull @RequestParam(name = "max")Short max,
            @NonNull @RequestParam(name = "credit")Short credit,
            @NonNull @RequestParam(name = "add")Boolean add
    ){
        if(name.length()<2||name.length()>31)return APIResponse.argError("课程名字长度需在2-31之间.");
        User teacher=userService.get(teacher_id);
        if(teacher==null||!teacher.getPermission().equals(User.PERMISSION_EAS_TEACHER))return APIResponse.argError("该id用户不是教师");
        if(classroom.length()<2||classroom.length()>31)return APIResponse.argError("教室名字长度需在2-31之间");
        boolean contain=false;for(String s: Course.COURSE_TYPES){if(s.equals(ctype))contain=true;}
        if(!contain)return APIResponse.argError("课程类型无效");
        if(!lessons.matches("^([1-7]\\&([1-9]||1[0-1]);)*[1-7]\\&([1-9]||1[0-1])$"))return  APIResponse.argError("课程时间无效"); //正则表达式匹配 1-7&1-11;1-7&1-11; （周几第几节之意，多节课之间拿;隔开）
        if(max>120||max<10)return APIResponse.argError("课程最大人数应在10-120之间");
        if(credit<1||credit>10)return APIResponse.argError("学分应在1-10之间");
        if(!add&&courseService.get(cid)==null)return APIResponse.argError("不存在该课程");
        return APIResponse.success("课程更新成功",courseService.update(cid,name,teacher_id,lessons,classroom,ctype,session,max,credit,add));
    }

    @PostMapping("/api/course/setSelectable")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse setSelectable(@NonNull @RequestParam(name = "cid")Integer cid,@NonNull @RequestParam(name = "selectable")Boolean selectable){
        if(courseService.get(cid)==null)return APIResponse.argError("不存在该课程");
        return APIResponse.success("修改选课状态成功",courseService.setSelectable(cid,selectable));
    }

    @GetMapping("/api/course/getMyCourses")
    @RequestPermission(permissions = {User.PERMISSION_EAS_STUDENT})
    APIResponse getMyCourses(HttpSession session){
        List<SC> scs=scService.getMySCs(session);
        return APIResponse.success("获取课表成功",courseService.getCoursesBySCs(scs));
    }
}
