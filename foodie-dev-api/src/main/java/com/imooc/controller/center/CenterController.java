package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.CenterUserService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "center-用户中心",tags = {"用户中心展示的相关接口"})
@RestController
@RequestMapping("/center")
public class CenterController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "GET")
    @GetMapping("/userInfo")
    public IMOOCJSONResult userInfo(@RequestParam String userId){
        Users users = centerUserService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(users);
    }
    @ApiOperation(value = "上传用户头像",notes = "上传用户头像",httpMethod = "POST")
    @GetMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(@RequestParam String userId, MultipartFile file,
                                      HttpServletRequest request,HttpServletResponse response){
//      自定义头像保存的地址
        String fileSpace = IMAGE_USER_FACE_LOCATION;
//      在路径上为每一个用户增加一个userId,用于区分不同用户上传
        String uploadPathPrefix = File.separator+userId;
        if (file!=null) {
            FileOutputStream fileOutputStream = null;
            try {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
//              文件重命名
                    String[] fileNameArr = filename.split("\\.");
                    String suffix = fileNameArr[fileNameArr.length - 1];
//              重组文件名
                    String newFileName = "face-"+userId+"."+suffix;
//              上传文件件的最终保存位置
                    String finalFacePath = fileSpace + uploadPathPrefix+File.separator+newFileName;
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile()!=null){
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    if (fileOutputStream != null){
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

            }

        }else {
            return IMOOCJSONResult.errorMsg("文件不能为空");
        }
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "修改用户信息",notes = "修改用户信息",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestParam String userId, @RequestBody @Valid CenterUserBO centerUserBO,
                                  BindingResult result, HttpServletRequest request, HttpServletResponse response){
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return IMOOCJSONResult.errorMap(errors);
        }
        Users users = centerUserService.updateUserInfo(userId,centerUserBO);
        users = setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
//      TODO 后续要改，增加令牌token，会整合进redis，分布式会话
        return IMOOCJSONResult.ok(users);
    }
    private Map<String,String> getErrors(BindingResult result){
        Map<String,String> map = new HashMap<>();
        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError error : fieldErrors) {
            String field = error.getField();
            String message = error.getDefaultMessage();
            map.put(field,message);
        }
        return map;
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
