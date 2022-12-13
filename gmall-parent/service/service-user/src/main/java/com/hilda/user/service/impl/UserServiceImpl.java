package com.hilda.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hilda.common.constant.RedisConst;
import com.hilda.common.execption.GmallException;
import com.hilda.common.util.MD5;
import com.hilda.model.bean.user.UserAddress;
import com.hilda.model.bean.user.UserInfo;
import com.hilda.model.vo.user.LoginResponseVo;
import com.hilda.model.vo.user.LoginVo;
import com.hilda.user.mapper.UserAddressMapper;
import com.hilda.user.mapper.UserInfoMapper;
import com.hilda.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LoginResponseVo login(LoginVo loginVo, String ip) {
        LoginResponseVo res = null;
        // 从 loginVo 中取出数据
        String loginName = loginVo.getLoginName();
        String passwd = loginVo.getPasswd();

        //TODO 判定指定用户是否存在，并查询用户信息
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        String passwdMd5 = MD5.encrypt(passwd);
        queryWrapper.eq(UserInfo::getLoginName, loginName).eq(UserInfo::getPasswd, passwdMd5);
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);

        //TODO 登录
        if (!ObjectUtils.isEmpty(userInfo)) {
            userInfo.setLoginIp(ip);
            res = new LoginResponseVo();
            Long userId = userInfo.getId();

            // 登录成功
            res.setUserId(userId);
            res.setNickName(userInfo.getNickName());

            //生成token
            String token = UUID.randomUUID().toString().replace("-", "");
            res.setToken(token);

            //将 用户信息 和 token共享到redis中
            String userInfoKey = RedisConst.USERKEY_PREFIX + RedisConst.USERKEY_SUFFIX + token;
            redisTemplate.opsForValue().set(userInfoKey, JSON.toJSONString(userInfo), RedisConst.USER_LOGIN_TTL, TimeUnit.SECONDS);

        } else {
            // 登录失败

        }

        return res;
    }

    @Override
    public Boolean logout(String token) {
        Boolean res = redisTemplate.delete(token);
        return res;
    }

    @Override
    public List<UserAddress> getUserAddressById(Long userId) {
        if (userId == null || userId == 0L) throw new GmallException("用户id为空", 10);

        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId);

        List<UserAddress> userAddresses = userAddressMapper.selectList(queryWrapper);
        return userAddresses;
    }

}
