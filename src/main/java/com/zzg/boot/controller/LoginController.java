package com.zzg.boot.controller;

import com.zzg.boot.result.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author ：zzg
 * @description：
 * @date ：2022/5/1 1:59
 */
@Api(tags = "登录入口")
@RestController
@RequestMapping("emp")
public class LoginController {


    @ApiOperation(value = "员工登录")
    public AjaxResult empLogin() {
        return null;

    }

}
