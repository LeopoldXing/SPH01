package com.hilda.user.service;

import com.hilda.model.vo.user.LoginResponseVo;
import com.hilda.model.vo.user.LoginVo;

public interface UserService {

    LoginResponseVo login(LoginVo loginVo);

    Boolean logout(String token);

}
