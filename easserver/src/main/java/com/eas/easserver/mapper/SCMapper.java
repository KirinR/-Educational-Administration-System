package com.eas.easserver.mapper;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SCMapper {
    @Select("SELECT scid,student_id,s.name,course_id,c.name as course_name,usual,final,grade,GPA FROM scs JOIN courses c ON scs.course_id=c.cid JOIN users s ON scs.student_id=s.uid WHERE student_id=#{uid} AND course_id=#{cid}")
    SC selectSCByStudentIdAndCourseId(Integer uid,Integer cid);
    @Select("SELECT scid,student_id,s.name,course_id,c.name as course_name,usual,final,grade,GPA FROM scs JOIN courses c ON scs.course_id=c.cid JOIN users s ON scs.student_id=s.uid WHERE scid=#{scid}")
    SC selectSCBySCId(Integer scid);
    @Select("SELECT scid,student_id,s.name,course_id,c.name as course_name,usual,final,grade,GPA FROM scs JOIN courses c ON scs.course_id=c.cid JOIN users s ON scs.student_id=s.uid WHERE student_id=#{uid}")
    List<SC> selectSCByStudentId(Integer uid);
    @Select("SELECT scid,student_id,s.name,course_id,c.name as course_name,usual,final,grade,GPA FROM scs JOIN courses c ON scs.course_id=c.cid JOIN users s ON scs.student_id=s.uid WHERE course_id=#{cid}")
    List<SC> selectSCByCourseId(Integer cid);
    @Select("SELECT scid,student_id,s.name,course_id,c.name as course_name,usual,final,grade,GPA FROM scs JOIN courses c ON scs.course_id=c.cid JOIN users s ON scs.student_id=s.uid")
    List<SC> selectSCs();
    @Select("SELECT COUNT(scid) FROM scs WHERE course_id=#{cid}")
    Integer selectSelectedByCid(Integer cid);
    @Update("UPDATE scs SET usual=#{usual_grade},final=#{final_grade},grade=#{grade},GPA=#{GPA} WHERE scid=#{scid}")
    void updateSCBySCId(Integer scid,Float usual_grade,Float final_grade,Float grade,Float GPA);
    @Insert("INSERT INTO scs(student_id,course_id) VALUES(#{student_id},#{course_id})")
    @Options(useGeneratedKeys = true,keyProperty = "scid",keyColumn = "scid")
    int insertSC(SC sc);
    @Delete("DELETE FROM scs WHERE scid=#{scid}")
    void deleteSC(Integer scid);
    @Delete("DELETE FROM scs WHERE course_id=#{cid}")
    void deleteSCByCourseId(Integer cid);
    @Delete("DELETE FROM scs WHERE student_id=#{uid}")
    void deleteSCByStudentId(Integer uid);
}
