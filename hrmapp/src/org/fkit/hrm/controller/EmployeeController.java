package org.fkit.hrm.controller;

import org.fkit.hrm.domain.Dept;
import org.fkit.hrm.domain.Employee;
import org.fkit.hrm.domain.Job;
import org.fkit.hrm.service.HrmService;
import org.fkit.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 员工管理处理器
 */
@Controller
public class EmployeeController {
    @Autowired
    @Qualifier("hrmService")
    private HrmService hrmService;

    /**
     * 处理查询请求
     *
     * @param pageIndex 请求的是第几页
     * @param job_id    职位编号
     * @param dept_id   部门编号
     * @param employee  模糊查询参数
     * @param model
     * @return
     */
    @RequestMapping(value = "employee/selectEmployee")
    public String selectEmployee(Integer pageIndex, Integer job_id, Integer dept_id, @ModelAttribute Employee employee, Model model) {
        //模糊查询判断是否有关联对象，如果有，创建并封装关联对象
        this.genericAssociation(job_id, dept_id, employee);

        //创建分页对象
        PageModel pageModel = new PageModel();
        //如果pageIndex不为空的话，设置pageIndex，即显示第几页
        if (pageIndex != null) {
            pageModel.setPageIndex(pageIndex);
        }

        //查询职位信息，用户模糊查询
        List<Job> jobs = hrmService.findAllJob();
        //查询部门信息，用户模糊查询
        List<Dept> depts = hrmService.findAllDept();
        //查询员工信息
        List<Employee> employees = hrmService.findEmployee(employee, pageModel);
        //设置Model数据
        model.addAttribute("employees", employees);
        model.addAttribute("jobs", jobs);
        model.addAttribute("depts", depts);
        model.addAttribute("pageModel", pageModel);
        //返回员工信息
        return "employee/employee";
    }


    /**
     * 添加员工信息
     *
     * @param flag     标志位 1表示跳转到添加页面 2表示执行添加操作
     * @param dept_id  职位编号
     * @param job_id   部门编号
     * @param employee 接收添加参数
     * @param mv
     * @return
     */
    @RequestMapping(value = "/employee/addEmployee")
    public ModelAndView addEmployee(String flag, Integer dept_id, Integer job_id, @ModelAttribute Employee employee, ModelAndView mv) {
        if (flag.equals("1")) {
            //1表示要跳转到添加页面

            //查询职位信息
            List<Job> jobs = hrmService.findAllJob();
            //查询部门信息
            List<Dept> depts = hrmService.findAllDept();
            //设置model数据
            mv.addObject("jobs", jobs);
            mv.addObject("depts", depts);
            //返回添加员工页面
            mv.setViewName("employee/showAddEmployee");
        } else {
            //判断是否有关联对象传递，如果有，创建关联对象
            this.genericAssociation(job_id, dept_id, employee);
            //添加操作
            hrmService.addEmployee(employee);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/employee/selectEmployee");

        }
        //返回
        return mv;
    }

    /**
     * 删除员工信息
     *
     * @param ids
     * @param mv
     * @return
     */
    @RequestMapping(value = "/employee/removeEmployee")
    public ModelAndView removeEmployee(String ids, ModelAndView mv) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            //依次删除
            hrmService.removeEmployeeById(Integer.parseInt(id));

        }
        //设置客户端跳转到查询请求
        mv.setViewName("redirect:/employee/selectEmployee");
        //返回ModelAndView
        return mv;
    }

    /**
     * 处理修改员工信息
     *
     * @param flag     1表示跳转到修改的页面，2 表示执行修改操作
     * @param job_id   职位编号
     * @param dept_id  部门编号
     * @param employee 要修改员工的对象
     * @param mv
     * @return
     */
    @RequestMapping(value = "/employee/updateEmployee")
    public ModelAndView updateEmployee(String flag, Integer job_id, Integer dept_id, @ModelAttribute Employee employee, ModelAndView mv) {
        //判断标志位
        if (flag.equals("1")) {
            //根据id查询员工
            Employee target = hrmService.findEmployeeById(employee.getId());
            //需要查询的职位信息
            List<Job> jobs = hrmService.findAllJob();
            //需要查询部门信息
            List<Dept> depts = hrmService.findAllDept();
            //设置Model数据
            mv.addObject("jobs", jobs);
            mv.addObject("depts", depts);
            mv.addObject("employee", employee);
            //返回修改员工页面
            mv.setViewName("employee/showUpdateEmployee");

        } else {
            //创建并封装关联对象
            this.genericAssociation(job_id, dept_id, employee);
            System.out.println("updateEmployee:" + employee);
            //执行修改操作
            hrmService.modifyEmployee(employee);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/employee/selectEmployee");
        }
        //返回
        return mv;
    }


    //关联设置
    private void genericAssociation(Integer jod_id, Integer dept_id, Employee employee) {
        //由于部门和职位在Employee中是对象关联映射
        //所以不能直接接收参数，需要创建job对象和dept对象
        if (jod_id != null) {
            Job job = new Job();
            job.setId(jod_id);
            employee.setJob(job);
        }
        if (dept_id != null) {
            Dept dept = new Dept();
            dept.setId(dept_id);
            employee.setDept(dept);
        }
    }
}