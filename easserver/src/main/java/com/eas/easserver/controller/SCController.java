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

@RestController
public class SCController {
    @Autowired
    CourseService courseService;
    @Autowired
    SCService scService;
    @Autowired
    UserService userService;
    @PostMapping("/api/sc/selectCourse")
    @RequestPermission(permissions = {User.PERMISSION_EAS_STUDENT,User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse selectCourse(
            @NonNull @RequestParam(name = "student_id")Integer student_id,
            @NonNull @RequestParam(name = "course_id")Integer course_id,
            HttpSession session
    ){
        User u=userService.isLogin(session);
        if(u.getPermission().equals(User.PERMISSION_EAS_STUDENT)&&u.getUid().intValue()!=student_id)return APIResponse.argError("您只能给自己选课");
        Course c=courseService.get(course_id);
        Integer count=scService.getSelected(course_id);
        if(!c.getSelectable())return APIResponse.argError("该课程不在选课时间内");
        if(count>=c.getMax())return APIResponse.argError("课程已满");
        if(scService.get(student_id,course_id)!=null)return APIResponse.argError("请勿重复选课");
        else{
            return APIResponse.success("选课成功",scService.add(student_id,course_id));
        }
    }

    @PostMapping("/api/sc/delete")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR,User.PERMISSION_EAS_STUDENT})
    APIResponse deleteCourse(@NonNull @RequestParam(name = "scid")Integer scid,HttpSession session){
        User u=userService.isLogin(session);
        SC sc=scService.get(scid);
        if(sc==null)return APIResponse.argError("不存在该选课记录");
        if(u.getPermission().equals(User.PERMISSION_EAS_STUDENT)){
            if(u.getUid().intValue()!=sc.getStudent_id().intValue())return APIResponse.argError("你只能给自己退课");
        }
        return APIResponse.success("删除成绩记录成功",scService.delete(scid));
    }

    @PostMapping("/api/sc/setGrade")
    @RequestPermission(permissions = {User.PERMISSION_EAS_TEACHER,User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse setGrade(
            @NonNull @RequestParam(name = "scid")Integer scid,
            @NonNull @RequestParam(name = "usual_grade")Float usual_grade,
            @NonNull @RequestParam(name = "final_grade")Float final_grade,
            @NonNull @RequestParam(name = "grade")Float grade,
            @NonNull @RequestParam(name = "GPA")Float GPA,
            HttpSession session
    ){
        User u=userService.isLogin(session);
        Course c=courseService.get(scService.get(scid).getCourse_id());
        if(u.getPermission().equals(User.PERMISSION_EAS_TEACHER)&&c.getTeacher_id().intValue()!=u.getUid().intValue()){
            return APIResponse.argError("你只能给自己的课程设置成绩");
        }
        if(usual_grade<0||usual_grade>100)return APIResponse.argError("平时成绩需在0-100之间");
        if(final_grade<0||final_grade>100)return APIResponse.argError("期末成绩需在0-100之间");
        if(grade<0||grade>100)return APIResponse.argError("总成绩需在0-100之间");
        if(GPA<0||GPA>4.0)return APIResponse.argError("GPA需在0-4.0之间");
        if(c.getSelectable())return APIResponse.argError("该课程仍在选课中");
        return APIResponse.success("成绩设置成功",scService.setGrade(scid,usual_grade,final_grade,grade,GPA));
    }

    @GetMapping("/api/sc/get")
    @RequestPermission( permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse getSC(
        @NonNull @RequestParam(name = "student_id")Integer student_id,
        @NonNull @RequestParam(name = "course_id")Integer course_id
    ){
        if(userService.get(student_id)==null||!userService.get(student_id).getPermission().equals(User.PERMISSION_EAS_STUDENT))return APIResponse.argError("不存在该学生");
        if(courseService.get(course_id)==null)return APIResponse.argError("不存在该课程");
        return APIResponse.success("获取成绩记录成功",scService.get(student_id,course_id));
    }
    @GetMapping("/api/sc/getMySCs")
    @RequestPermission(permissions = {User.PERMISSION_EAS_STUDENT})
    APIResponse getMySCs(HttpSession session){
        return APIResponse.success("获取成绩单成功",scService.getMySCs(session));
    }
    @GetMapping("/api/sc/getTeacherSCs")
    @RequestPermission(permissions = {User.PERMISSION_EAS_TEACHER})
    APIResponse getTeacherSCs(HttpSession session){
        return APIResponse.success("获取成绩单成功",scService.getTeacherSCs(session));
    }
    @GetMapping("/api/sc/getSelected")
    @RequestPermission()
    APIResponse getSelected(@NonNull @RequestParam Integer cid){
        if(courseService.get(cid)==null)return APIResponse.argError("该课程不存在");
        return APIResponse.success("获取选课人数成功",scService.getSelected(cid));
    }
    @GetMapping("/api/sc/getIsSelected")
    @RequestPermission(permissions = {User.PERMISSION_EAS_STUDENT})
    APIResponse getIsSelected(@NonNull @RequestParam Integer cid,HttpSession session){
        if(courseService.get(cid)==null)return APIResponse.argError("该课程不存在");
        User u=userService.isLogin(session);
        SC sc=scService.get(u.getUid(),cid);
        return APIResponse.success("获取选课状态成功", sc==null?null:sc.getScid());
    }
    @GetMapping("/api/sc/getAll")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    APIResponse getAll(){
        return APIResponse.success("获取成绩表成功",scService.getAll());
    }

}
