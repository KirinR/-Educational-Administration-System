package com.eas.easserver;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.SC;
import com.eas.easserver.bean.User;
import com.eas.easserver.mapper.CourseMapper;
import com.eas.easserver.mapper.SCMapper;
import com.eas.easserver.mapper.UserMapper;
import com.eas.easserver.util.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.DigestUtils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class CourseTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    SCMapper scMapper;
    @Autowired
    UserMapper userMapper;

    static MockHttpSession session=new MockHttpSession();
    static ObjectMapper objectMapper=new ObjectMapper();
    static APIResponse expectResponse=null;

    @BeforeAll
    static void setObjectMapper(){
        objectMapper.setDateFormat(new SimpleDateFormat("YYYY-MM-DD"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/course/getAll_cases.csv")
    void getAllTest(Integer uid,String password,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("课程列表获取成功",courseMapper.selectCourses());
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/course/getAll")
                    .characterEncoding("UTF-8")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/course/getTeacherCourse_cases.csv")
    void getTeacherCourseTest(Integer uid,String password,Integer teacher_id,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("课程获取成功",courseMapper.selectCoursesByUid(teacher_id));
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==1)expectResponse=APIResponse.argError("不存在该教师");
        else if(expect==2)expectResponse=APIResponse.argError("该用户不是教师");
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/course/getTeacherCourses")
                    .characterEncoding("UTF-8")
                    .param("teacher_id",teacher_id.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/course/get_cases.csv")
    void getTest(Integer uid,String password,Integer cid,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("课程获取成功",courseMapper.selectCourseByCid(cid));
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==1)expectResponse=APIResponse.argError("不存在该课程");
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/course/get")
                    .characterEncoding("UTF-8")
                    .param("cid",cid.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/course/setSelectable_cases.csv")
    void setSelectableTest(Integer uid,String password,Integer cid,Boolean selectable,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)APIResponse.success("修改选课状态成功",selectable);
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==2)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
        else if(expect==1)expectResponse=APIResponse.argError("不存在该课程");
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/course/setSelectable")
                    .characterEncoding("UTF-8")
                    .param("cid",cid.toString())
                    .param("selectable",selectable.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0){
                Assert.assertEquals(selectable,courseMapper.selectCourseByCid(cid).getSelectable());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/course/getMyCourses_cases.csv")
    void getMyCoursesTest(Integer uid,String password,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0){
            List<SC> scs = scMapper.selectSCByStudentId(uid);
            List<Course> result=new ArrayList<>();
            for(SC sc:scs){
                result.add(courseMapper.selectCourseByCid(sc.getCourse_id()));
            }
            expectResponse=APIResponse.success("获取课表成功",result);
        }
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==1)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_STUDENT});
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/course/getMyCourses")
                    .characterEncoding("UTF-8")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/course/delete_cases.csv")
    void deleteTest(Integer uid,String password,Integer cid,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("删除课程成功",cid);
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==1)expectResponse=APIResponse.argError("不存在该课程");
        else if(expect==2)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/course/delete")
                    .characterEncoding("UTF-8")
                    .param("cid",cid.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0){
                Assert.assertEquals(0,scMapper.selectSCByCourseId(cid).size());
                Assert.assertNull(courseMapper.selectCourseByCid(cid));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/course/update_cases.csv")
    void updateTest(Integer uid,String password,
                   Integer update_cid,
                   String update_name,
                   Integer update_teacher_id,
                   String update_lessons,
                   String update_classroom,
                   String update_ctype,
                   String update_session,
                   Short update_max,
                   Short update_credit,
                   Boolean add,
                   Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("课程更新成功",update_cid);
        else if(expect==1)expectResponse=APIResponse.argError("课程名字长度需在2-31之间.");
        else if(expect==2)expectResponse=APIResponse.argError("该id用户不是教师");
        else if(expect==3)expectResponse=APIResponse.argError("教室名字长度需在2-31之间");
        else if(expect==4)expectResponse=APIResponse.argError("课程类型无效");
        else if(expect==5)expectResponse=APIResponse.argError("课程时间无效");
        else if(expect==6)expectResponse=APIResponse.argError("课程最大人数应在10-120之间");
        else if(expect==7)expectResponse=APIResponse.argError("学分应在1-10之间");
        else if(expect==8)expectResponse=APIResponse.argError("不存在该课程");
        else if(expect==9)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        try{
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/course/update")
                    .characterEncoding("UTF-8")
                    .param("cid",update_cid.toString())
                    .param("name",update_name)
                    .param("teacher_id",update_teacher_id.toString())
                    .param("lessons",update_lessons)
                    .param("classroom",update_classroom)
                    .param("ctype",update_ctype)
                    .param("session",update_session)
                    .param("max",update_max.toString())
                    .param("credit",update_credit.toString())
                    .param("add",add.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0){
                Course c=new Course(update_cid,update_name,update_teacher_id,userMapper.selectUserByUid(update_teacher_id).getName(),update_lessons,update_classroom,update_ctype,Date.valueOf(update_session),update_max,update_credit,false);
                Assert.assertEquals(c,courseMapper.selectCourseByCid(update_cid));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
