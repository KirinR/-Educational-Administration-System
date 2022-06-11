package com.eas.easserver.interceptor;

import com.eas.easserver.annonation.RequestPermission;
import com.eas.easserver.bean.User;
import com.eas.easserver.service.UserService;
import com.eas.easserver.util.APIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class RequestInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    public void render(HttpServletResponse response,Object data) throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        OutputStream os=response.getOutputStream();
        os.write(mapper.writeValueAsString(data).getBytes());
        os.flush();
        os.close();
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            HandlerMethod api=(HandlerMethod)handler;
            RequestPermission permission=api.getMethodAnnotation(RequestPermission.class);
            if(permission!=null){
                User u=userService.isLogin(request.getSession());
                if(u!=null){
                    String p=u.getPermission();
                    if(permission.permissions().length==0)return true;
                    for(String s:permission.permissions()){
                        if(p.equals(s))return true;
                    }
                    render(response,APIResponse.pDenied(permission.permissions()));
                    return false;
                }else{
                    render(response,APIResponse.nLogin());
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
