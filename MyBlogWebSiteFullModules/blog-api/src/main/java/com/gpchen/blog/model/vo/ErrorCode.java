package com.gpchen.blog.model.vo;

//store all the possible error codes
public enum ErrorCode {
    PARAMS_ERROR(10001,"parameters error"),
    ACCOUNT_PWD_NOT_EXIST(10002,"password/account not exist"),
    TOKEN_INVALID(10003, "TOKEN INVALID"),
    ACCOUNT_EXIST(10004,"Account already exist"),
    NO_PERMISSION(70001,"no permission"),
    SESSION_TIME_OUT(90001,"session expired"),
    NO_LOGIN(90002,"not login"),
    SYSTEM_ERROR(-99999,"System Exception"),
    UPLOAD_FAIL(20001, "upload file failed");

    private int code;
    private String msg;

    ErrorCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
