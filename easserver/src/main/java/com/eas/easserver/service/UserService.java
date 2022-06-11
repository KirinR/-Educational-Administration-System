package com.eas.easserver.service;

import com.eas.easserver.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    //登录服务,返回登录用户,登录失败返回null.
    User login(Integer uid, String password, boolean remember, HttpSession session, HttpServletResponse response);
    //检查登录状态服务,返回当前登录用户,未登录返回null
    User isLogin(HttpSession session);
    //获取用户服务,返回相应uid用户
    User get(Integer uid);
    //更新/注册用户服务.register参数表示是注册还是更新用户.返回注册/更新后的用户id
    Integer update(Integer uid, String password, String name, Long phone, String department, String permission, boolean register);
    //获取全部用户服务,返回全部用户
    List<User> getAll();
    //检查电话号码是否已经被uid之外的其他用户使用
    boolean isPhoneUsed(Integer uid,Long phone);
    //修改密码服务,修改当前登录的用户的密码,并登出用户
    void changePwd(HttpSession session,String password);
    //登出服务
    void loginOut(HttpSession session);
    //删除用户服务,删除相应uid的用户,同时删除相应的选课记录(学生)和课程(教师)
    void deleteUser(Integer uid);
}
