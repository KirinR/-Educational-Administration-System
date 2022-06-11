package com.eas.easserver.service;

import com.eas.easserver.bean.Course;
import com.eas.easserver.bean.User;
import com.eas.easserver.mapper.SCMapper;
import com.eas.easserver.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImp implements UserService {

    public static final Integer LoginSessionTime=1*24*60*60;

    @Autowired
    UserMapper userMapper;
    @Autowired
    SCMapper scMapper;
    @Autowired
    CourseService courseService;

    String genUserToken(){
        Random r=new Random();
        String prefix=Integer.toString(r.nextInt(),36);
        return DigestUtils.md5DigestAsHex((prefix+System.currentTimeMillis()).getBytes());
    }

    @Override
    public User login(Integer uid, String password,boolean remember,HttpSession session,HttpServletResponse response) {
        User u=userMapper.selectUserByUidAndPassword(uid,password);
        if(u!=null) {
            String token=genUserToken();
            long loginTime=System.currentTimeMillis();
            userMapper.updateLoginTokenByUid(uid,token,loginTime);
            if(remember) {
                Cookie c = new Cookie("JSESSIONID", session.getId());
                c.setMaxAge(LoginSessionTime);
                c.setPath("/");
                response.addCookie(c);
            }
            session.setMaxInactiveInterval(LoginSessionTime);
            session.setAttribute("loginToken",new String[]{uid.toString(),token,String.valueOf(loginTime)});
        }
        return u;
    }

    @Override
    public User isLogin(HttpSession session) {
        Object attr=session.getAttribute("loginToken");
        if(attr==null){
            return null;
        }
        String[] loginToken=(String[])attr;
        if(loginToken.length<3)return null;
        try {
            int uid=Integer.valueOf(loginToken[0]);
            String token=loginToken[1];
            Long loginTime=Long.valueOf(loginToken[2]);
            Long serverLoginTime=userMapper.selectLoginTimeByUid(uid);
            if(loginTime==null||serverLoginTime==null)return null;
            if(loginTime.longValue()!=serverLoginTime.longValue()){
                return null;
            }
            return userMapper.selectUserByUidAndToken(uid,token);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User get(Integer uid) {
        return userMapper.selectUserByUid(uid);
    }

    @Override
    public Integer update(Integer uid, String password, String name, Long phone, String department, String permission, boolean register) {
        if(!register){
            userMapper.updateUser(uid,name,phone,department,permission);
            if(password!=null){
                userMapper.updatePasswordByUid(uid,password);
            }
            return uid;
        }else{
            if(password==null)return -1;
            User u=new User(uid,name,phone,department,permission,null);
            int success=userMapper.insertUser(password,u);
            if(success==1)return u.getUid();
            else return -1;
        }
    }

    @Override
    public List<User> getAll() {
        return userMapper.selectUsers();
    }

    @Override
    public boolean isPhoneUsed(Integer uid, Long phone) {
        Integer u=userMapper.selectUidByPhone(phone);
        if(u!=null)return u!=uid;
        else return false;
    }

    @Override
    public void changePwd(HttpSession session, String password) {
         User u=isLogin(session);
         if(u!=null){
             userMapper.updatePasswordByUid(u.getUid(),password);
             loginOut(session);
         }
    }

    @Override
    public void loginOut(HttpSession session) {
        User u=isLogin(session);
        if(u!=null)userMapper.updateLoginTokenByUid(u.getUid(),null,null);
        session.removeAttribute("loginToken");
    }

    @Override
    public void deleteUser(Integer uid) {
        scMapper.deleteSCByStudentId(uid);
        List<Course> courses=courseService.getByTeacher(uid);
        for(Course c:courses){courseService.delete(c.getCid());}
        userMapper.deleteUser(uid);
    }
}
