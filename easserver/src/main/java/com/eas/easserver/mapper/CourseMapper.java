package com.eas.easserver.mapper;

import com.eas.easserver.bean.Course;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface CourseMapper {
    @Select("SELECT c.cid,c.name,u.uid,u.name as teacher_name,c.lessons,c.classroom,c.ctype,c.session,c.max,c.credit,c.selectable FROM courses c JOIN users u ON u.uid=c.teacher_id")
    List<Course> selectCourses();
    @Select("SELECT c.cid,c.name,u.uid,u.name as teacher_name,c.lessons,c.classroom,c.ctype,c.session,c.max,c.credit,c.selectable FROM courses c JOIN users u ON u.uid=c.teacher_id WHERE u.uid=#{uid}")
    List<Course> selectCoursesByUid(Integer uid);
    @Select("SELECT c.cid,c.name,u.uid,u.name as teacher_name,c.lessons,c.classroom,c.ctype,c.session,c.max,c.credit,c.selectable FROM courses c JOIN users u ON u.uid=c.teacher_id WHERE c.cid=#{cid}")
    Course selectCourseByCid(Integer cid);
    @Update("UPDATE courses SET name=#{name},teacher_id=#{teacher_id},lessons=#{lessons},classroom=#{classroom},ctype=#{ctype},session=#{session},max=#{max},credit=#{credit} WHERE cid=#{cid}")
    void updateCourseByCid(Integer cid, String name, Integer teacher_id, String lessons , String classroom, String ctype, Date session, Short max, Short credit);
    @Update("UPDATE courses SET selectable=#{selectable} WHERE cid=#{cid}")
    void updateSelectableByCid(Integer cid,Boolean selectable);
    @Insert("INSERT INTO courses(name,teacher_id,lessons,classroom,ctype,session,max,credit,selectable) VALUES(#{name},#{teacher_id},#{lessons},#{classroom},#{ctype},#{session},#{max},#{credit},0)")
    @Options(useGeneratedKeys = true,keyProperty = "cid",keyColumn = "cid")
    int insertCourse(Course c);
    @Delete("DELETE FROM courses WHERE cid=#{cid}")
    void deleteCourseByCid(Integer cid);
}
