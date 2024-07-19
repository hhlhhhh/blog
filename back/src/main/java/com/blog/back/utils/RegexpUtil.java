package com.blog.back.utils;

public class RegexpUtil {

    //都为数字，10位，第一位不为0
    public static final String userAccountReg = "^[1-9]\\d{9}$";

    public static final String userPasswordReg = "^.{6,16}$";

    public static final String emailReg = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    //可以包含字母、数字、下划线、点号、连字符、汉字等字符，长度不超过20个字符
    public static final String nicknameReg = "^[\\w\\u4e00-\\u9fa5-]{1,20}$";

    public static final String resetPasswordEmailCodeReg = "^\\d{6}$";

    public static final String loginEmailCodeReg = "^\\d{6}$";

    public static final String registerEmailReg = "^\\d{6}$";

    public static final String userInfoProfileReg = "^.{0,200}$";

    public static final String userInfoNicknameReg = "^.{3,20}$";
}
