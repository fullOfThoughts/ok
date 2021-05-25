package com.bmd.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmd.core.pojo.po.BmdMsg;
import com.bmd.core.pojo.po.BmdUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ZS
 * @since 2021-04-22
 */
public interface BmdMsgMapper extends BaseMapper<BmdMsg> {

    //  chats
    @Select("select msg_from, count(*) from bmd_msg where msg_to = #{name} and is_read = 0 GROUP BY msg_from")
    List<Map<String,Object>> getChats(String name);
    @Select("select msg_from from bmd_msg where msg_to = #{name} and is_read = 1 GROUP BY msg_from")
    List<String> getChats2(String name);
}
