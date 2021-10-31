package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.AddressService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关",tags = {"地址相关的api接口"})
@RequestMapping("/address")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户id查询收货地址列表",notes = "根据用户id查询收货地址列表")
    @PostMapping("/list")
    public IMOOCJSONResult list(@RequestParam String userId){
        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        List<UserAddress> userAddressList = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(userAddressList);
    }
    @ApiOperation(value = "用户新增地址",notes = "用户新增地址")
    @PostMapping("/add")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO){
        if (checkAddess(addressBO).getStatus() != 200){
            return checkAddess(addressBO);
        }
        addressService.addAddress(addressBO);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户修改地址",notes = "用户修改地址")
    @PostMapping("/update")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO){
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("修改地址错误:addressId不能为空");
        }
        if (checkAddess(addressBO).getStatus() != 200){
            return checkAddess(addressBO);
        }
        addressService.updateAddress(addressBO);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户删除地址",notes = "用户删除地址")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(@RequestParam String userId,@RequestParam String addressId){
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("");
        }
        addressService.delete(userId,addressId);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址")
    @PostMapping("/setDefault")
    public IMOOCJSONResult setDefault(@RequestParam String userId,@RequestParam String addressId){
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("");
        }
        addressService.setDefaultAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }
    private IMOOCJSONResult checkAddess(AddressBO addressBO){
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)){
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12){
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }
        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("手机不能为空");
        }
        if (mobile.length()!=11) {
            return IMOOCJSONResult.errorMsg("手机长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk){
            return IMOOCJSONResult.errorMsg("手机格式不正确");
        }
        if (StringUtils.isBlank(addressBO.getProvince())||StringUtils.isBlank(addressBO.getCity())
                ||StringUtils.isBlank(addressBO.getDistrict())||StringUtils.isBlank(addressBO.getDetail())){
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }
        return IMOOCJSONResult.ok();
    }
}
