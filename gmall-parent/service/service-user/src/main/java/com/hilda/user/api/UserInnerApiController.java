package com.hilda.user.api;

import com.hilda.common.result.Result;
import com.hilda.common.util.RequestUtil;
import com.hilda.model.bean.user.UserAddress;
import com.hilda.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/inner")
public class UserInnerApiController {

    @Autowired
    private UserService userService;

    @GetMapping("/address/list")
    Result<List<UserAddress>> getUserAddress(){
        Long uid = RequestUtil.getUID();

        List<UserAddress> userAddressList = userService.getUserAddressById(uid);

        return userAddressList == null ? Result.fail() : Result.ok(userAddressList);
    }

}
