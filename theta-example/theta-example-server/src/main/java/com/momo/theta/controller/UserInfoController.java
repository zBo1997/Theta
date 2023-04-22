package com.momo.theta.controller;

import com.alibaba.fastjson.JSONObject;
import com.momo.theta.api.UserInfoService;
import com.momo.theta.biz.UserBusiness;
import com.momo.theta.condition.UserCondition;
import com.momo.theta.dto.UserInfoDTO;
import com.momo.theta.entity.User;
import com.momo.theta.form.UserForm;
import com.momo.theta.service.AsynExcelExportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class UserInfoController implements UserInfoService {

    @Autowired
    private UserBusiness userBusiness;

    @Autowired
    private AsynExcelExportUtil asynExcelExportUtil;


    /**
     * 分页查询
     */
    @GetMapping("query")
    public String query(UserCondition userCondition) {
        return JSONObject.toJSONString(userBusiness.query(userCondition));
    }

    @RequestMapping("/exportUserInfo")
    @ResponseBody
    public void exportUserInfo(HttpServletResponse response) throws InterruptedException {
        asynExcelExportUtil.threadExcel(response);
    }

    @Override
    public UserInfoDTO create(UserForm userForm) {
        userBusiness.createUser(userForm);
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
        User byId = userBusiness.getById(userId);
        userInfoDTO.setUserName(byId.getUserName());
        userInfoDTO.setPhone(byId.getPhone());
        userInfoDTO.setRegionId(byId.getRegionId());
        userInfoDTO.setLanId(byId.getLanId());
        return userInfoDTO;
    }

    @Override
    public UserInfoDTO update(UserForm userForm) {
        log.info("更新参数：{}", JSONObject.toJSONString(userForm));
        return userBusiness.update(userForm);
    }
}
