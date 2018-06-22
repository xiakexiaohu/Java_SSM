package org.fkit.hrm.service.impl;

import org.fkit.hrm.dao.*;
import org.fkit.hrm.domain.*;
import org.fkit.hrm.service.HrmService;
import org.fkit.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ejb.TransactionManagement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认识管理系统服务层具体实现类
 */
    //使用@Service将其标识为业务层实现类,Spring将该类配置成bean，标识符hrmService
    //事务的包含五个方面的：传播行为、回滚操作、隔离级别、是否只读、事务超时

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    @Service("hrmService")
public class HrmServiceImpl implements HrmService{
    /**
     *自动注入持久层Dao对象
     */
    @Autowired
    private UserDao userDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private JobDao jobDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private DocumentDao documentDao;


    /*****************用户服务接口实现*******************/
    //设置事务管理
    @Transactional(readOnly = true)
    @Override
    public User login(String loginname, String password) {
        return userDao.SelectByLoginnameAndPassword(loginname, password);
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(Integer id) {
        return userDao.selectById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findUser(User user, PageModel pageModel) {
        /*当前需要分页的总数据条数*/
        Map<String,Object> params=new HashMap<>();
        params.put("user",user);
        Integer recordCount = userDao.count(params);
        pageModel.setRecordCount(recordCount);

        //开始分页查询数据：查询第几页的数据
        if(recordCount>0){
            params.put("pageModel", pageModel);
        }
        //根据页数查询对应的数据
        List<User> users=userDao.selectByPage(params);
        return users;
    }

    @Override
    public void removeUserById(Integer id) {
        userDao.deleteById(id);
    }

    @Override
    public void modifyUser(User user) {
        userDao.update(user);
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
    }

    /**********************员工接口实现**********************/
    @Transactional(readOnly = true)
    @Override
    public List<Employee> findEmployee(Employee employee, PageModel pageModel) {
        //当前需要分页的总数据条数
        Map<String,Object> params=new HashMap<>();
        params.put("employee",employee);

        Integer recordCount = employeeDao.count(params);
        pageModel.setRecordCount(recordCount);
        if(recordCount>0){
            //开始分页查询数据，查询第几页的数据
            params.put("pageModel",pageModel);
        }
        List<Employee> employees = employeeDao.selectByPage(params);

        return employees;
    }

    @Override
    public void removeEmployeeById(Integer id) {
        employeeDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Employee findEmployeeById(Integer id) {
        return employeeDao.selectById(id);
    }

    @Override
    public void addEmployee(Employee employee) {
        employeeDao.save(employee);
    }

    @Override
    public void modifyEmployee(Employee employee) {
        employeeDao.update(employee);
    }

    /**********************部门接口实现**********************/
    @Transactional(readOnly = true)
    @Override
    public List<Dept> findDept(Dept dept, PageModel pageModel) {
        //当前需要分页的总数据条数
        Map<String, Object> params = new HashMap<>();
        params.put("dept",dept);
        int recordCount = deptDao.count(params);
        pageModel.setRecordCount(recordCount);
        if(recordCount>0){
            //开始分页查询数据，查询第几页的数据
            params.put("pageModel", pageModel);
        }
        List<Dept> depts = deptDao.selectByPage(params);
        return depts;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Dept> findAllDept() {
        return deptDao.selectAllDept();
    }

    @Override
    public void removeDeptById(Integer id) {
        deptDao.deleteById(id);
    }

    @Override
    public void addDept(Dept dept) {
        deptDao.save(dept);
    }

    @Override
    public Dept findDeptById(Integer id) {
        return deptDao.selectById(id);
    }

    @Override
    public void modifyDept(Dept dept) {
        deptDao.update(dept);
    }

    /*********************职位接口实现**********************/
    @Transactional(readOnly = true)
    @Override
    public List<Job> findAllJob() {
        return jobDao.selectAllJob();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Job> findJob(Job job, PageModel pageModel) {
        //当前需要分页的总数据条数
        Map<String,Object> params=new HashMap<>();
        params.put("job",job);
        int recordCount = jobDao.count(params);
        pageModel.setRecordCount(recordCount);
        if(recordCount>0){
            //分页查询
            params.put("pageModel",pageModel);
        }
        List<Job> jobs = jobDao.selectByPage(params);
        return jobs;
    }

    @Override
    public void removeJobById(Integer id) {
        jobDao.deleteById(id);
    }

    @Override
    public void addJob(Job job) {
        jobDao.save(job);
    }

    @Override
    public Job findJobById(Integer id) {
        return jobDao.selectById(id);
    }

    @Override
    public void modifyJob(Job job) {
        jobDao.update(job);
    }

    /**********************公告接口实现**********************/
    @Transactional(readOnly = true)
    @Override
    public List<Notice> findNotice(Notice notice, PageModel pageModel) {
        //当前需要分页的总数据条数
        Map<String, Object> params = new HashMap<>();
        params.put("notice", notice);
        int recordCount = noticeDao.count(params);
        //放入model
        pageModel.setRecordCount(recordCount);
        if(recordCount>0){
            //开始分页查询数据，查询第几天的数据
            params.put("pageModel", pageModel);
        }
        List<Notice> notices = noticeDao.selectByPage(params);
        return notices;

    }

    @Transactional(readOnly = true)
    @Override
    public Notice findNoticeById(Integer id) {
        return noticeDao.selectById(id);
    }

    @Override
    public void removeNoticeById(Integer id) {
        noticeDao.deleteById(id);
    }

    @Override
    public void addNotice(Notice notice) {
        noticeDao.save(notice);
    }

    @Override
    public void modifyNotice(Notice notice) {
        noticeDao.update(notice);
    }

    /**********************文件接口实现**********************/
    @Transactional(readOnly = true)
    @Override
    public List<Document> findDocument(Document document, PageModel pageModel) {
        //当前需要分页的总数据条数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("docuemnt",document);
        int recordCount = documentDao.count(params);
        //放入model中
        pageModel.setRecordCount(recordCount);
        if(recordCount>0){
            //开始分页查询数据：查询第几页的数据
            params.put("pageModel", pageModel);
        }
        List<Document> documents = documentDao.selectBypage(params);
        return documents;
    }

    @Override
    public void addDocument(Document document) {
        documentDao.save(document);
    }

    @Transactional(readOnly = true)
    @Override
    public Document findDocumentById(Integer id) {
        return documentDao.selectById(id);
    }

    @Override
    public void removeDocumentById(Integer id) {
        documentDao.deleteById(id);
    }

    @Override
    public void modifyDocument(Document document) {
        documentDao.update(document);
    }
}
