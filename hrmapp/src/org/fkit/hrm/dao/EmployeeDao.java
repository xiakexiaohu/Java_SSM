package org.fkit.hrm.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.fkit.hrm.dao.provider.EmployeeDynaSqlProvider;
import org.fkit.hrm.domain.Employee;

import java.util.List;
import java.util.Map;

import static org.fkit.hrm.util.common.HrmConstants.EMPLOYEETABLE;

/**
 * 员工接口
 */
public interface EmployeeDao {
    //根据参数查询员工总数
    @SelectProvider(type = EmployeeDynaSqlProvider.class, method = "count")
    Integer count(Map<String, Object> params);

    //根据参数动态查询员工,使用@Results组合条件查询
    @SelectProvider(type = EmployeeDynaSqlProvider.class, method = "selectWhitParam")
    //组合条件查询
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "CARD_ID", property = "cardId"),
            @Result(column = "POST_CODE", property = "postCode"),
            @Result(column = "QQ_NUM", property = "qqNum"),
            @Result(column = "BIRTHDAY", property = "birthday", javaType = java.util.Date.class),
            @Result(column = "CREATE_DATE", property = "createDate", javaType = java.util.Date.class),
            @Result(column = "DEPT_ID", property = "dept",
                    one = @One(select = "org.fkit.hrm.dao.DeptDao.selectById",
                            fetchType = FetchType.EAGER
                    )
            ),
            @Result(column = "JOB_ID", property = "job",
                    one = @One(select = "org.fkit.hrm.dao.JobDao.selectById", fetchType = FetchType.EAGER
                    )
            )
    })
    List<Employee> selectByPage(Map<String, Object>params);

    //动态插入员工
    @SelectProvider(type = EmployeeDynaSqlProvider.class, method = "insertEmployee")
    void save(Employee employee);

    /*******************特别需要注意SQL语句拼接之间的空格符，一定需要添加空格，不然查询会报错********************/
    //根据id删除员工
    @Delete("delete from "+EMPLOYEETABLE+" where id=#{id}")
    void deleteById(Integer id);

    //根据id查询员工
    @Select("select * from " + EMPLOYEETABLE + " where id=#{id}")
    //因为员工跟职位和部门是一对多的关系，所以需要使用组合查询,同时使用延迟加载（即部门和工作信息不需要全部加载进来）
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "CARD_ID",property = "cardId'"),
            @Result(column = "POST_CODE",property = "postCode'"),
            @Result(column = "QQ_NUM",property = "qqNum"),
            @Result(column = "BIRTHDAY",property = "birthday",javaType = java.util.Date.class),
            @Result(column = "CREATE_DATE",property = "createDate",javaType = java.util.Date.class),
            @Result(column = "DEPT_ID",property = "dept",
                one=@One(select = "org.fkit.hrm.dao.DeptDao.selectById",fetchType = FetchType.EAGER)
            ),
            @Result(column = "JOB_ID",property = "job",
                one=@One(select = "org.fkit.hrm.dao.JobDao.selectById",fetchType = FetchType.EAGER)
            ),
    })
    Employee selectById(Integer id);

    //动态修改员工
    @SelectProvider(type = EmployeeDynaSqlProvider.class,method = "updateEmployee")
    void update(Employee employee);

}
