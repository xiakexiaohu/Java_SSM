package org.fkit.hrm.dao;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.fkit.hrm.dao.provider.DocumentDynaSqlProvider;
import org.fkit.hrm.domain.Document;

import java.util.List;
import java.util.Map;

import static org.fkit.hrm.util.common.HrmConstants.DOCUMENTTABLE;

/**
 * 文件接口
 */
public interface DocumentDao {
    @SelectProvider(type = DocumentDynaSqlProvider.class, method = "selectWhitParam")
    //因为文件和用户之间存在一对多
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "CREATE_DATE", property = "createDate", javaType = java.util.Date.class),
            @Result(column = "USER_ID", property = "user",
                    one = @One(select = "org.fkit.hrm.dao.UserDao.selectById", fetchType = FetchType.EAGER)
            )

    })
    List<Document> selectBypage(Map<String, Object> params);

    //统计文件总数
    @SelectProvider(type = DocumentDynaSqlProvider.class, method = "count")
    Integer count(Map<String, Object> params);

    //动态插入文档
    @SelectProvider(type = DocumentDynaSqlProvider.class, method = "insertDocument")
    void save(Document document);

    /*******************特别需要注意SQL语句拼接之间的空格符，一定需要添加空格，不然查询会报错********************/
    //根据id查询
    @Select("select * from " + DOCUMENTTABLE + " where id=#{id}")
    Document selectById(Integer id);

    //根据id删除文档
    @Delete("delete from " + DOCUMENTTABLE + " where id=#{id}")
    void deleteById(Integer id);


    //动态修改文档
    @SelectProvider(type = DocumentDynaSqlProvider.class,method = "updateDocument")
    void update(Document document);
}
