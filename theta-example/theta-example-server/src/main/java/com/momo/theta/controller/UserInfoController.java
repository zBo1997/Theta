package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.api.UserInfoService;
import com.momo.theta.condition.UserCondition;
import com.momo.theta.dto.UserInfoDTO;
import com.momo.theta.entity.User;
import com.momo.theta.form.UserForm;
import com.momo.theta.service.AsynExcelExportUtil;
import com.momo.theta.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class UserInfoController implements UserInfoService {

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

    @Override
    public UserInfoDTO create(@RequestBody UserForm userForm) {
        userService.createUser(userForm);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(userForm.getUserName());
        userInfoDTO.setPhone(userForm.getPhone());
        userInfoDTO.setLanId(userForm.getLanId());
        userInfoDTO.setRegionId(userForm.getRegionId());
        return userInfoDTO;
    }

    @Override
    public UserInfoDTO query(String userId) {
        log.info("查询id：{}", userId);
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        User byId = userService.getById(userId);
        userInfoDTO.setUserName(byId.getUserName());
        userInfoDTO.setPhone(byId.getPhone());
        userInfoDTO.setLanId(byId.getLanId());
        userInfoDTO.setRegionId(byId.getRegionId());
        return userInfoDTO;
    }
}
