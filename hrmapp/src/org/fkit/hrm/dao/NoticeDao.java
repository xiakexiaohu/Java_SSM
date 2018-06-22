package org.fkit.hrm.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.fkit.hrm.dao.provider.NoticeDynaSqlProvider;
import org.fkit.hrm.domain.Notice;

import java.util.List;
import java.util.Map;

import static org.fkit.hrm.util.common.HrmConstants.NOTICETABLE;

/**
 * 消息接口
 */
public interface NoticeDao {
    //动态查询
    @SelectProvider(type = NoticeDynaSqlProvider.class, method = "selectWhitParam")
    //因为消息和员工是一对多的关系,使用组合查询，并且延迟加载
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "CREATE_DATE", property = "createDate", javaType = java.util.Date.class),
            @Result(column = "USER_ID", property = "user",
                    one = @One(select = "org.fkit.hrm.dao.UserDao.selectById", fetchType = FetchType.EAGER)
            )
    })
    List<Notice> selectByPage(Map<String, Object> params);

    //统计总的消息数
    @SelectProvider(type = NoticeDynaSqlProvider.class, method = "count")
    Integer count(Map<String, Object> params);

    /*******************特别需要注意SQL语句拼接之间的空格符，一定需要添加空格，不然查询会报错********************/
    //根据id查询
    @Select("select * from " + NOTICETABLE + " where id=#{id}")
    Notice selectById(Integer id);

    //根据id删除公告
    @Delete("delete from "+NOTICETABLE+" where id=#{id}")
    void deleteById(Integer id);

    //动态插入公告
    @SelectProvider(type = NoticeDynaSqlProvider.class, method = "insertNotice")
    void save(Notice notice);

    //动态修改公告
    @SelectProvider(type = NoticeDynaSqlProvider.class, method = "updateNotice")
    void update(Notice notie);

}
