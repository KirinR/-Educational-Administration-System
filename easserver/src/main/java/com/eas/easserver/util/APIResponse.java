package com.eas.easserver.util;

public class APIResponse {

    Integer code;
    String message;
    Object data;

    private final static Integer STATUS[]=new Integer[]{
            0,//success
            -1,//need login
            -2,//permission denied
            -3,//argument error
            -4,//request limit
    };

    private APIResponse(Integer code, String message, Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }

    public static APIResponse success(String message,Object data){return new APIResponse(STATUS[0],message,data);}

    public static APIResponse nLogin(){return new APIResponse(STATUS[1],"请先登录",null);}

    public static APIResponse pDenied(String []permissions){return new APIResponse(STATUS[2],"无权访问",permissions); }

    public static APIResponse argError(String message){return new APIResponse(STATUS[3],message,null);}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
