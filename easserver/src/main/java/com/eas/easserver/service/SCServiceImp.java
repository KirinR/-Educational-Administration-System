package com.eas.easserver.service;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;
import com.eas.easserver.bean.User;
import com.eas.easserver.mapper.SCMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class SCServiceImp implements SCService {
    @Autowired
    UserService userService;
    @Autowired
    CourseService courseService;
    @Autowired
    SCMapper scMapper;
    @Override
    public List<SC> getCourseSCs(Integer course_id) {
        return scMapper.selectSCByCourseId(course_id);
    }

    @Override
    public List<SC> getAll() {
        return scMapper.selectSCs();
    }

    @Override
    public SC get(Integer scid) {
        return scMapper.selectSCBySCId(scid);
    }

    @Override
    public SC get(Integer student_id, Integer course_id) {
        return scMapper.selectSCByStudentIdAndCourseId(student_id,course_id);
    }

    @Override
    public Integer add(Integer student_id, Integer course_id) {
        SC sc=new SC(-1,student_id,null,course_id,null,null,null,null,null);
        if(scMapper.insertSC(sc)==1) return sc.getScid();
        else return -1;
    }

    @Override
    public Integer delete(Integer scid) {
        scMapper.deleteSC(scid);
        return scid;
    }

    @Override
    public Integer setGrade(Integer scid, Float usual_grade, Float final_grade, Float grade, Float GPA) {
        scMapper.updateSCBySCId(scid,usual_grade,final_grade,grade,GPA);
        return scid;
    }

    @Override
    public Integer getSelected(Integer cid) {
        return scMapper.selectSelectedByCid(cid);
    }

    @Override
    public List<SC> getMySCs(HttpSession session) {
        User u=userService.isLogin(session);
        if(u==null)return null;
        return scMapper.selectSCByStudentId(u.getUid());
    }

    @Override
    public List<SC> getTeacherSCs(HttpSession session) {
        User u=userService.isLogin(session);
        if(u==null)return null;
        List<Course> courses=courseService.getByTeacher(u.getUid());
        List<SC> scs=new ArrayList<>();
        for(Course c:courses){
            scs.addAll(scMapper.selectSCByCourseId(c.getCid()));
        }
        return scs;
    }
}
