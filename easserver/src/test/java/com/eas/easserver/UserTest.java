package com.eas.easserver;

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

import java.text.SimpleDateFormat;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    SCMapper scMapper;

    static MockHttpSession session=new MockHttpSession();
    static ObjectMapper objectMapper=new ObjectMapper();
    static APIResponse expectResponse=null;

    @BeforeAll
    static void setObjectMapper(){
        objectMapper.setDateFormat(new SimpleDateFormat("YYYY-MM-DD"));
    }

    @ParameterizedTest
    @CsvFileSource(resources="/user/updateUser_cases.csv")
    void updateUserTest(Integer uid,
                        String password,
                        Integer update_uid,
                        String update_password,
                        String update_name,
                        Long update_phone,
                        String update_department,
                        String update_permission,
                        Boolean register,
                        Integer expect
                        ){
        session.removeAttribute("loginToken");
        if(expect==1)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==2)expectResponse=APIResponse.argError("??????????????????2-8??????");
        else if(expect==3)expectResponse=APIResponse.argError("??????????????????6-18??????");
        else if(expect==4)expectResponse=APIResponse.argError("????????????????????????????????????");
        else if(expect==5)expectResponse=APIResponse.argError("????????????????????????????????????");
        else if(expect==6)expectResponse=APIResponse.argError("??????????????????");
        else if(expect==0)expectResponse=APIResponse.success("??????????????????",update_uid);
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
                    .post("/api/user/updateUser")
                    .characterEncoding("UTF-8")
                    .param("uid",update_uid.toString())
                    .param("password",update_password)
                    .param("name",update_name)
                    .param("phone",update_phone.toString())
                    .param("department",update_department)
                    .param("permission",update_permission)
                    .param("register",register.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0){
                User u=new User(update_uid,update_name,update_phone,update_department,update_permission,null);
                Assert.assertTrue(u.equalsWithOutRegTime(userMapper.selectUserByUid(update_uid)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/user/userList_cases.csv")
    void userListTest(Integer uid,String password,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0){expectResponse=APIResponse.success("????????????????????????",userMapper.selectUsers());}
        else if(expect==1) expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
        else if(expect==-1) expectResponse=APIResponse.nLogin();
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/user/userList")
                    .characterEncoding("UTF-8")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/user/changePwd_cases.csv")
    void changePwdTest(Integer uid,String password,String newPassword,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("??????????????????",null);
        else if(expect==1)expectResponse=APIResponse.argError("??????????????????6-18??????");
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/changePwd")
                    .characterEncoding("UTF-8")
                    .param("password", newPassword)
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0) {
                Assert.assertEquals(DigestUtils.md5DigestAsHex(newPassword.getBytes()), userMapper.selectPasswordByUid(uid));
                Assert.assertNull(session.getAttribute("loginToken"));
                Assert.assertNull(userMapper.selectLoginTimeByUid(expect));
                Assert.assertNull(userMapper.selectTokenByUid(expect));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @ParameterizedTest
    @CsvFileSource(resources = "/user/delete_cases.csv")
    void deleteUserTest(Integer uid,String password,Integer delete_uid,Integer expect){
        session.removeAttribute("loginToken");
        if(expect==0)expectResponse=APIResponse.success("??????????????????",delete_uid);
        else if(expect==1)expectResponse=APIResponse.pDenied(new String[]{User.PERMISSION_EAS_ADMINISTRATOR});
        else if(expect==-1)expectResponse=APIResponse.nLogin();
        else if(expect==2)expectResponse=APIResponse.argError("??????????????????");
        try {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/login")
                    .characterEncoding("UTF-8")
                    .param("uid", uid.toString())
                    .param("password", DigestUtils.md5DigestAsHex(password.getBytes()))
                    .param("remember", "false")
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/api/user/delete")
                    .characterEncoding("UTF-8")
                    .param("uid",delete_uid.toString())
                    .session(session))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)));
            if(expect==0) {
                Assert.assertEquals(0,courseMapper.selectCoursesByUid(delete_uid).size());
                Assert.assertEquals(0,scMapper.selectSCByStudentId(delete_uid).size());
                Assert.assertNull(userMapper.selectUserByUid(delete_uid));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
