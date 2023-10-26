package com.momo.theta.service;


import com.momo.theta.entity.Account;
import com.momo.theta.entity.Info;

import java.math.BigInteger;
import java.util.List;

/**
 * Function:
 *
 * @author Shaowei Xu Date: 2019/11/20 13:37
 */
public interface IAccountService {

    /**
     * 通过userCode查询账号列表
     *
     * @param userCode
     * @return
     */
    List<Account> findByUserCode(String userCode);

    /**
     * 通过账号Id获取账号信息
     *
     * @param accountId 账号Id
     * @author: ZhiWei.Chen
     * @date: 2022/7/14 13:44
     * @return:
     */
    Info getByAccountId(BigInteger accountId);

}
