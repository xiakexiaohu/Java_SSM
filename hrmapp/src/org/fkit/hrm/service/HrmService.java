package org.fkit.hrm.service;

import org.fkit.hrm.dao.NoticeDao;
import org.fkit.hrm.domain.*;
import org.fkit.hrm.util.tag.PageModel;
import org.springframework.ui.Model;

import java.util.List;

/**
 * hrmService接口，人事管理服务层接口
 * 将所有实体的POJO接口都定义在HrmSrevice中
 */

public interface HrmService {
    /******************用户接口实现********************/
    /**
     * 用户登录
     *
     * @param loginname
     * @param password
     * @return
     */
    User login(String loginname, String password);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    User findUserById(Integer id);

    /**
     * 获得所有用户
     *
     * @param user
     * @param pageModel
     * @return
     */
    List<User> findUser(User user, PageModel pageModel);

    /**
     * 根据id删除用户
     *
     * @param id
     */
    void removeUserById(Integer id);

    /**
     * 修改用户
     *
     * @param user
     */
    void modifyUser(User user);

    /**
     * 添加用户
     * @param user
     */
    void addUser(User user);

    /******************员工接口实现********************/
    /**
     * 获取所有员工
     *
     * @param employee  查询条件
     * @param pageModel 分页对象
     * @return 返回List集合
     */
    List<Employee> findEmployee(Employee employee, PageModel pageModel);

    /**
     * 根据id删除员工
     *
     * @param id
     */
    void removeEmployeeById(Integer id);

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    Employee findEmployeeById(Integer id);


    /**
     * 添加员工
     *
     * @param employee
     */
    void addEmployee(Employee employee);

    /**
     * 修改员工
     *
     * @param employee
     */
    void modifyEmployee(Employee employee);

    /******************部门接口实现********************/
    /**
     * 获取所有部门，分页查询
     *
     * @param dept
     * @param pageModel
     * @return
     */
    List<Dept> findDept(Dept dept, PageModel pageModel);

    /**
     * 获得所有部门
     *
     * @return
     */
    List<Dept> findAllDept();

    /**
     * 根据id删除部门
     *
     * @param id
     */
    void removeDeptById(Integer id);

    /**
     * 添加部门
     * @param dept
     */
    void addDept(Dept dept);

    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    Dept findDeptById(Integer id);

    /**
     * 修改部门
     * @param dept
     */
    void modifyDept(Dept dept);

    /******************职位接口实现********************/
    /**
     * 获取所有的职位
     *
     * @return
     */
    List<Job> findAllJob();

    List<Job> findJob(Job job,PageModel pageModel);

    /**
     * 根据id删除职位
     * @param id
     */
     void removeJobById(Integer id);

    /**
     * 添加job
     * @param job
     */
    void addJob(Job job);

    /**
     * 根据id查询职位
     * @param id
     * @return
     */
    Job findJobById(Integer id);

    /**
     * 修改职位
     * @param job
     */
    void modifyJob(Job job);

    /******************公告接口实现********************/
    /**
     * 获取所有公告
     * @param notice
     * @param pageModel
     * @return
     */

    List<Notice> findNotice(Notice notice, PageModel pageModel);

    /**
     * 根据id查询公告
     * @param id
     * @return
     */
    Notice findNoticeById(Integer id);

    /**
     * 根据id删除公告
     * @param id
     */
    void removeNoticeById(Integer id);

    /**
     * 添加公告
     * @param notice
     */
    void addNotice(Notice notice);

    /**
     * 修改公告
     * @param notice
     */
    void modifyNotice(Notice notice);


    /******************文件接口实现********************/
    /**
     * 获得所有文档
     * @param document
     * @param pageModel
     * @return
     */
    List<Document> findDocument(Document document,PageModel pageModel);


    /**
     * 添加文档
     * @param document
     */
    void addDocument(Document document);

    /**
     * 根据id查询文档
     * @param id
     * @return
     */
    Document findDocumentById(Integer id);

    /**
     * 根据id删除文档
     * @param id
     */
    void removeDocumentById(Integer id);

    /**
     * 修改文档
     * @param document
     */
    void modifyDocument(Document document);


}
