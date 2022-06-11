package com.eas.easserver;

import com.eas.easserver.mapper.UserMapper;
import com.eas.easserver.service.UserService;
import com.eas.easserver.util.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import java.text.SimpleDateFormat;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    static MockHttpSession session=new MockHttpSession();
    static ObjectMapper objectMapper=new ObjectMapper();

    @BeforeAll
    static void setObjectMapper(){
        objectMapper.setDateFormat(new SimpleDateFormat("YYYY-MM-DD"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/login_cases.csv")
    void loginTest(Integer uid,String password,Boolean remember,Integer expect){
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/loginedUser").characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(APIResponse.success("登录状态获取成功", null))));
        }catch (Exception e){
            e.printStackTrace();
        }
        APIResponse expectResponse;
        if(expect==-1){
            expectResponse=APIResponse.argError("用户名或密码错误");
        }else{
            expectResponse=APIResponse.success("登录成功",userMapper.selectUserByUid(expect));
        }
        MvcResult result=null;
        try{
            result=mockMvc.perform(
                    MockMvcRequestBuilders.post("/api/user/login").characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON)
                    .param("uid",uid.toString())
                    .param("password",password)
                    .param("remember",remember.toString())
                    .session(session)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectResponse)))
                    .andReturn();
            if(expect!=-1) {
                Cookie cookie=result.getResponse().getCookie("JSESSIONID");
                if(remember)Assert.assertTrue(cookie.getPath().equals("/"));
                else Assert.assertNull(cookie);
                String longToken[]=(String[]) session.getAttribute("loginToken");
                Assert.assertEquals(expect.toString(),longToken[0]);
                Assert.assertEquals(userMapper.selectTokenByUid(expect),longToken[1]);
                Assert.assertEquals(userMapper.selectLoginTimeByUid(expect).toString(),longToken[2]);
            }
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/loginedUser").characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON)
                    .session(session)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(APIResponse.success("登录状态获取成功", userMapper.selectUserByUid(expect)))));
            mockMvc.perform(MockMvcRequestBuilders.get("/api/user/loginOut").characterEncoding("UTF-8")
                    .accept(MediaType.APPLICATION_JSON)
                    .session(session)
            ).andExpect(MockMvcResultMatchers.status().isOk());
            Assert.assertNull(session.getAttribute("loginToken"));
            Assert.assertNull(userMapper.selectLoginTimeByUid(expect));
            Assert.assertNull(userMapper.selectTokenByUid(expect));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
