package org.fkit.hrm.dao;

import org.apache.ibatis.annotations.*;
import org.fkit.hrm.dao.provider.UserDynaSqlProvider;
import org.fkit.hrm.domain.User;

import java.util.List;
import java.util.Map;

import static org.fkit.hrm.util.common.HrmConstants.USERTABLE;

/**
 * 用户接口
 */
public interface UserDao {
    //根据登录名和密码查询员工
    @Select("select * from " + USERTABLE + " where loginname=#{loginname} and password=#{password}")
    User SelectByLoginnameAndPassword(@Param("loginname") String loginname, @Param("password") String password);

    /*******************特别需要注意SQL语句拼接之间的空格符，一定需要添加空格，不然查询会报错********************/
    //根据id查询用户
    @Select("select * from " + USERTABLE + " where id=#{id}")
    User selectById(Integer id);

    //根据id删除用户
    @Delete("delete from " + USERTABLE + " where id=#{id}")
    void deleteById(Integer id);

    //动态修改用户
    @SelectProvider(type = UserDynaSqlProvider.class, method = "updateUser")
    void update(User user);

    //动态查询
    @SelectProvider(type = UserDynaSqlProvider.class, method = "selectWhitParam")
    List<User> selectByPage(Map<String, Object> params);

    //根据参数查询用户总数
    @SelectProvider(type = UserDynaSqlProvider.class, method = "count")
    Integer count(Map<String, Object> params);

    //动态插入用户
    @SelectProvider(type = UserDynaSqlProvider.class, method = "insertUser")
    void save(User user);

}
