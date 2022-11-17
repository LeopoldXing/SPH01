package com.hilda.user.api;

import com.hilda.common.result.Result;
import com.hilda.common.result.ResultCodeEnum;
import com.hilda.common.util.IpUtil;
import com.hilda.model.vo.user.LoginResponseVo;
import com.hilda.model.vo.user.LoginVo;
import com.hilda.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private UserService userService;

    @PostMapping("/passport/login")
    public Result<LoginResponseVo> userLogin(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        String ip = IpUtil.getIpAddress(request);
        LoginResponseVo loginResponseVo = userService.login(loginVo, ip);

        return ObjectUtils.isEmpty(loginResponseVo) ? Result.build(null, ResultCodeEnum.INVALID_PASSWORD_OR_ACCOUNT) : Result.ok(loginResponseVo);
    }

    @GetMapping("/passport/logout")
    public Result userLogout(@RequestHeader("token") String token) {
        userService.logout(token);
        return Result.ok();
    }
}
