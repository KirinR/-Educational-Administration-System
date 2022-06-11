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
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class SCTest {
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
    @CsvFileSource(resources = "/sc/selectCourse_cases.csv")
    void selectCourseTest(Integer uid,String password,
                          Integer scid,
                          Integer student_id,Integer course_id,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("选课成功",scid);
        else if(expect==1)expectResponse=APIResponse.argError("您只能给自己选课");
        else if(expect==2)expectResponse=APIResponse.argError("该课程不在选课时间内");
        else if(expect==3)expectResponse=APIResponse.argError("课程已满");
        else if(expect==4)expectResponse=APIResponse.argError("请勿重复选课");
        else if(expect==5)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_STUDENT,User.PERMISSION_EAS_ADMINISTRATOR});
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
                    .post("/api/sc/selectCourse")
                    .characterEncoding("UTF-8")
                    .param("student_id",student_id.toString())
                    .param("course_id",course_id.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0) Assert.assertNotNull(scMapper.selectSCByStudentIdAndCourseId(student_id,course_id));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/sc/getAll_cases.csv")
    void getAllTest(Integer uid,String password,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("获取成绩表成功",scMapper.selectSCs());
        else if(expect==1)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
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
                    .get("/api/sc/getAll")
                    .characterEncoding("UTF-8")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/sc/getIsSelected_cases.csv")
    void getIsSelectedTest(Integer uid,String password,Integer cid,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("获取选课状态成功",scMapper.selectSCByStudentIdAndCourseId(uid,cid)==null?null:scMapper.selectSCByStudentIdAndCourseId(uid,cid).getScid());
        else if(expect==1)expectResponse=APIResponse.argError("该课程不存在");
        else if(expect==2)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_STUDENT});
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
                    .get("/api/sc/getIsSelected")
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
    @CsvFileSource(resources = "/sc/getSelected_cases.csv")
    void getSelectedTest(Integer uid,String password,Integer cid,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("获取选课人数成功",scMapper.selectSCByCourseId(cid).size());
        else if(expect==1)expectResponse=APIResponse.argError("该课程不存在");
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
                    .get("/api/sc/getSelected")
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
    @CsvFileSource(resources = "/sc/getTeacherSCs_cases.csv")
    void getTeacherSCsTest(Integer uid,String password,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0){
            List<Course> courses=courseMapper.selectCoursesByUid(uid);
            List<SC> result=new ArrayList<>();
            for(Course c:courses){
                result.addAll(scMapper.selectSCByCourseId(c.getCid()));
            }
            expectResponse=APIResponse.success("获取成绩单成功",result);
        }
        else if(expect==1)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_TEACHER});
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
                    .get("/api/sc/getTeacherSCs")
                    .characterEncoding("UTF-8")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/sc/getMySCs_cases.csv")
    void getMySCsTest(Integer uid,String password,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("获取成绩单成功",scMapper.selectSCByStudentId(uid));
        else if(expect==1)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_STUDENT});
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
                    .get("/api/sc/getMySCs")
                    .characterEncoding("UTF-8")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/sc/getSC_cases.csv")
    void getSCTest(Integer uid,String password,Integer student_id,Integer course_id,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("获取成绩记录成功",scMapper.selectSCByStudentIdAndCourseId(student_id,course_id));
        else if(expect==1)expectResponse=APIResponse.argError("不存在该学生");
        else if(expect==2)expectResponse=APIResponse.argError("不存在该课程");
        else if(expect==3)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
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
                    .get("/api/sc/get")
                    .characterEncoding("UTF-8")
                    .param("student_id",student_id.toString())
                    .param("course_id",course_id.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/sc/setGrade_cases.csv")
    void setGradeTest(Integer uid,String password,
                      Integer scid,
                      Float usual_grade,
                      Float final_grade,
                      Float grade,
                      Float GPA,
                      Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("成绩设置成功",scid);
        else if(expect==1)expectResponse=APIResponse.argError("你只能给自己的课程设置成绩");
        else if(expect==2)expectResponse=APIResponse.argError("平时成绩需在0-100之间");
        else if(expect==3)expectResponse=APIResponse.argError("期末成绩需在0-100之间");
        else if(expect==4)expectResponse=APIResponse.argError("总成绩需在0-100之间");
        else if(expect==5)expectResponse=APIResponse.argError("GPA需在0-4.0之间");
        else if(expect==6)expectResponse=APIResponse.argError("该课程仍在选课中");
        else if(expect==7)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_TEACHER,User.PERMISSION_EAS_ADMINISTRATOR});
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
                    .post("/api/sc/setGrade")
                    .characterEncoding("UTF-8")
                    .param("scid",scid.toString())
                    .param("usual_grade",usual_grade.toString())
                    .param("final_grade",final_grade.toString())
                    .param("grade",grade.toString())
                    .param("GPA",GPA.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0){
                SC sc=scMapper.selectSCBySCId(scid);
                Assert.assertEquals(usual_grade,sc.getUsual_grade());
                Assert.assertEquals(final_grade,sc.getFinal_grade());
                Assert.assertEquals(grade,sc.getGrade());
                Assert.assertEquals(GPA,sc.getGPA());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/sc/delete_cases.csv")
    void deleteTest(Integer uid,String password,Integer scid,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("删除成绩记录成功",scid);
        else if(expect==1)expectResponse=APIResponse.argError("不存在该选课记录");
        else if(expect==2)expectResponse=APIResponse.argError("你只能给自己退课");
        else if(expect==3)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR,User.PERMISSION_EAS_STUDENT});
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
                    .post("/api/sc/delete")
                    .characterEncoding("UTF-8")
                    .param("scid",scid.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0){
                Assert.assertNull(scMapper.selectSCBySCId(scid));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
