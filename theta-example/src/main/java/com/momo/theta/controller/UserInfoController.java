package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.condition.UserCondition;
import com.momo.theta.form.UserForm;
import com.momo.theta.service.AsynExcelExportUtil;
import com.momo.theta.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("userInfo")
public class UserInfoController {

    @Autowired
    private IUserService userService;

    @Autowired
    private AsynExcelExportUtil asynExcelExportUtil;


    /**
     * 分页查询
     */
    @GetMapping("query")
    public String query(UserCondition userCondition) {
        return JSONObject.toJSONString(userService.query(userCondition));
    }

    @RequestMapping("/exportUserInfo")
    @ResponseBody
    public void exportUserInfo(HttpServletResponse response) throws InterruptedException {
        asynExcelExportUtil.threadExcel(response);
    }



    @RequestMapping("/create")
    @ResponseBody
    public String creat(@RequestBody UserForm userForm) {
        userService.createUser(userForm);
        return "success";
    }


}
