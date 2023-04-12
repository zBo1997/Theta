package com.momo.theta.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("test_user")
@ExcelTarget("user")
public class User {

    @Excel(name = "主键")
    @TableId
    private String id;
    @Excel(name = "用户编号")
    private String userId;
    @Excel(name = "客户姓名")
    private String userName;
    @Excel(name = "客户电话")
    private String phone;
    @Excel(name = "lanId")
    private String lanId;
    @Excel(name = "区域id")
    private String regionId;
    @Excel(name = "创建时间")
    private Date createTime;

}
