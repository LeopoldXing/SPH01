package com.hilda.user.service;

import com.hilda.model.bean.user.UserAddress;
import com.hilda.model.vo.user.LoginResponseVo;
import com.hilda.model.vo.user.LoginVo;

import java.util.List;

public interface UserService {

    LoginResponseVo login(LoginVo loginVo, String ip);

    Boolean logout(String token);

    List<UserAddress> getUserAddressById(Long userId);

}
