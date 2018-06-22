package org.fkit.hrm.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.fkit.hrm.dao.provider.DeptDynaSqlProvider;
import org.fkit.hrm.domain.Dept;
import static org.fkit.hrm.util.common.HrmConstants.DEPTTABLE;

/**
 * 部门接口
 */
public interface DeptDao {
    //动态查询
    @SelectProvider(type= DeptDynaSqlProvider.class,method="selectWhitParam")
    List<Dept> selectByPage(Map<String,Object> params);


    //统计总的部门
    @SelectProvider(type = DeptDynaSqlProvider.class, method = "count")
    Integer count(Map<String, Object> params);

    /*******************特别需要注意SQL语句拼接之间的空格符，一定需要添加空格，不然查询会报错********************/
    //查询所有部门
    @Select("select * from " + DEPTTABLE + " ")
    List<Dept> selectAllDept();

    //根据id查询
    @Select("select * from "+DEPTTABLE+" where id=#{id}")
    Dept selectById(Integer id);

    //根据id删除部门
    @Delete("delete from " + DEPTTABLE + " where id =#{id}")
    void deleteById(Integer id);

    //动态插入部门
    @SelectProvider(type = DeptDynaSqlProvider.class, method = "insertDept")
    void save(Dept dept);

    //动态修改用户
    @SelectProvider(type = DeptDynaSqlProvider.class,method = "updateDept")
    void update(Dept dept);

}
