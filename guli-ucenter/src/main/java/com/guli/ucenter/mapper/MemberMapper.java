package com.guli.ucenter.mapper;

import com.guli.ucenter.entity.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author chen
 * @since 2020-01-03
 */
public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 获取进入注册数
     * @param day
     * @return
     */
    Integer getRegsiterNumByDay(String day);
}
