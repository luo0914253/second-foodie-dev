package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录",tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController {
    @Autowired
    private UserService userService;

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/userNameIsExist")
    public IMOOCJSONResult userNameIsExist(@RequestParam String username){
//      参数校验
        if (StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
//      判断用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
//      请求成功，用户名没有重复
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户注册",notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBO userBO){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
//      参数校验
        if (StringUtils.isBlank(username)||StringUtils.isBlank(password)||StringUtils.isBlank(confirmPassword)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
//      判断用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist){
            return IMOOCJSONResult.errorMsg("用户名已经存在");
        }
//      判断密码长度
//        if (password.length() < 6){
//            return IMOOCJSONResult.errorMsg("密码长度不能小于6");
//        }
//      密码校验
        if (!password.equals(confirmPassword)){
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }
//      创建用户
        Users user = userService.createUser(userBO);
        user = setNullProperty(user);
        CookieUtils.setCookie(request,response,"user",JsonUtils.objectToJson(user),true);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "GET")
    @PostMapping("/login")
    public IMOOCJSONResult login(HttpServletRequest request, HttpServletResponse response, @RequestBody UserBO userBO){
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        if (StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        Users userResult = userService.login(userBO);
        if (userResult == null){
            return IMOOCJSONResult.errorMsg("用户或密码不正确");
        }
        userResult = setNullProperty(userResult);
//      设置cookie
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(userResult),true);
        return IMOOCJSONResult.ok(userResult);
    }
    @ApiOperation(value = "退出登录",notes = "退出登录",httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,HttpServletRequest request,HttpServletResponse response){
        CookieUtils.deleteCookie(request,response,"user");
        return IMOOCJSONResult.ok();
    }
    private Users setNullProperty(Users userResult){
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
