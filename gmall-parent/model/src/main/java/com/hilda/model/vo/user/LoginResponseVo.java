package com.hilda.model.vo.user;

import com.hilda.model.bean.base.BaseEntity;

import java.util.Objects;

public class LoginResponseVo extends BaseEntity {

    private String token;
    private String nickName;
    private Long userId;

    public LoginResponseVo() {
    }

    public LoginResponseVo(String token, String nickName, Long userId) {
        this.token = token;
        this.nickName = nickName;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "LoginResponseVo{" +
                "token='" + token + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userId=" + userId +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LoginResponseVo that = (LoginResponseVo) o;
        return Objects.equals(token, that.token) && Objects.equals(nickName, that.nickName) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), token, nickName, userId);
    }
}
