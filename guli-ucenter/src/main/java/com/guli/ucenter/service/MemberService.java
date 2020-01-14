package com.guli.ucenter.service;

import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author chen
 * @since 2020-01-03
 */
public interface MemberService extends IService<Member> {

    /**
     * 获取今日注册数
     * @param day
     * @return
     */
    Integer getRegsiterNumByDay(String day);

    /**
     * 根据token获取用户信息
     * @param openid
     * @return
     */
    Member getMemberByOpenId(String openid);
}
