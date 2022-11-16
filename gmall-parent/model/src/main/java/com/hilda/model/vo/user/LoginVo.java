package com.hilda.model.vo.user;

import com.hilda.model.bean.base.BaseEntity;

import java.util.Objects;

public class LoginVo extends BaseEntity {

    private String loginName;
    private String passwd;

    public LoginVo() {
    }

    public LoginVo(String loginName, String passwd) {
        this.loginName = loginName;
        this.passwd = passwd;
    }

    @Override
    public String toString() {
        return "LoginVo{" +
                "loginName='" + loginName + '\'' +
                ", passwd='" + passwd + '\'' +
                '}';
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoginVo loginVo = (LoginVo) o;
        return Objects.equals(loginName, loginVo.loginName) && Objects.equals(passwd, loginVo.passwd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loginName, passwd);
    }
}
