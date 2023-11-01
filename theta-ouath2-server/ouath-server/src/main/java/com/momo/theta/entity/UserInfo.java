package com.momo.theta.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfo {

    private String userId;

    private String password;

    private List<String> permissions;

    private String userName;

}
