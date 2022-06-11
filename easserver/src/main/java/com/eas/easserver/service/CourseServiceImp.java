package com.eas.easserver.service;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;
import com.eas.easserver.mapper.CourseMapper;
import com.eas.easserver.mapper.SCMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImp implements CourseService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    SCMapper scMapper;

    @Override
    public List<Course> getAll() {
        return courseMapper.selectCourses();
    }

    @Override
    public List<Course> getByTeacher(Integer teacher_id) {
        return courseMapper.selectCoursesByUid(teacher_id);
    }

    @Override
    public Course get(Integer cid) {
        return courseMapper.selectCourseByCid(cid);
    }

    @Override
    public Integer update(Integer cid, String name, Integer uid, String lessons, String classroom, String ctype, Date session, Short max, Short credit, Boolean add) {
        if(add){
            Course c=new Course(cid,name,uid,null,lessons,classroom,ctype,session,max,credit,false);
            if(courseMapper.insertCourse(c)==1) return c.getCid();
            else return -1;
        }else{
            courseMapper.updateCourseByCid(cid,name,uid,lessons,classroom,ctype,session,max,credit);
            return cid;
        }
    }

    @Override
    public Integer delete(Integer cid) {
        scMapper.deleteSCByCourseId(cid);
        courseMapper.deleteCourseByCid(cid);
        return cid;
    }

    @Override
    public boolean setSelectable(Integer cid, Boolean selectable) {
        courseMapper.updateSelectableByCid(cid,selectable);
        return selectable;
    }

    @Override
    public List<Course> getCoursesBySCs(List<SC> scs) {
        List<Course> courses=new ArrayList<>();
        for(SC sc:scs){
            courses.add(get(sc.getCourse_id()));
        }
        return courses;
    }
}
