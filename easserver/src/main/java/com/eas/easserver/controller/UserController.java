package com.eas.easserver.controller;

import com.eas.easserver.annonation.RequestPermission;
import com.eas.easserver.bean.User;
import com.eas.easserver.service.UserService;
import com.eas.easserver.util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/api/user/login")
    public APIResponse login(
            @NonNull @RequestParam(name = "uid")Integer uid,
            @NonNull @RequestParam(name = "password")String password,
            @NonNull @RequestParam(name = "remember")Boolean remember,
            HttpSession session,
            HttpServletResponse response){
        User u=userService.login(uid,password,remember,session,response);
        if(u==null){
            return APIResponse.argError("用户名或密码错误");
        }else{
            return APIResponse.success("登录成功",u);
        }
    }
    @GetMapping("/api/user/loginedUser")
    public APIResponse loginedUser(HttpSession session){
        User u=userService.isLogin(session);
        return APIResponse.success("登录状态获取成功",u);
    }

    @PostMapping("/api/user/changePwd")
    @RequestPermission()
    public APIResponse changePwd(@NonNull @RequestParam(name = "password") String password, HttpSession session){
        if(password.length()<6||password.length()>18)return APIResponse.argError("密码长度需在6-18之间");
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        userService.changePwd(session,password);
        return APIResponse.success("密码修改成功",null);
    }

    @PostMapping("/api/user/updateUser")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    public APIResponse updateUser(
            @NonNull @RequestParam(name = "uid")Integer uid,
            @Nullable @RequestParam(name = "password")String password,
            @NonNull @RequestParam(name = "name")String name,
            @NonNull @RequestParam(name = "phone")Long phone,
            @NonNull @RequestParam(name = "department")String department,
            @NonNull @RequestParam(name = "permission")String permission,
            @NonNull @RequestParam(name = "register")Boolean register){
        if(name.length()>8||name.length()<2)return APIResponse.argError("名字长度需在2-8之间");
        if(password!=null&&(password.length()<6||password.length()>18))return APIResponse.argError("密码长度需在6-18之间");
        if(password!=null)password=DigestUtils.md5DigestAsHex(password.getBytes());
        if(!register&&userService.get(uid)==null)return APIResponse.argError("不存在的用户");
        if(permission.equals(User.PERMISSION_EAS_STUDENT)||permission.equals(User.PERMISSION_EAS_TEACHER)) {
            if(userService.isPhoneUsed(uid,phone))return APIResponse.argError("该电话号码已经被使用过了");
            return APIResponse.success("用户更新成功",userService.update(uid,password,name,phone,department,permission,register));
        }else{
            return APIResponse.argError("无法将用户设置为该权限组");
        }
    }

    @GetMapping("/api/user/loginOut")
    @RequestPermission()
    public APIResponse loginOut(HttpSession session){
        userService.loginOut(session);
        return APIResponse.success("登出成功",null);
    }

    @GetMapping("/api/user/userList")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    public APIResponse getUserList(){
        return APIResponse.success("用户列表获取成功",userService.getAll());
    }

    @PostMapping("/api/user/delete")
    @RequestPermission(permissions = {User.PERMISSION_EAS_ADMINISTRATOR})
    public APIResponse deleteUser(@NonNull @RequestParam(name = "uid")Integer uid){
        if(userService.get(uid)==null)return APIResponse.argError("不存在的用户");
        userService.deleteUser(uid);
        return APIResponse.success("删除用户成功",uid);
    }
}
