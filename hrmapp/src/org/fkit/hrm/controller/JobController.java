package org.fkit.hrm.controller;

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
 * 职位管理控制器
 */
@Controller
public class JobController {
    //自动注入
    @Autowired
    @Qualifier("hrmService")
    private HrmService hrmService;

    /**
     * 根据id查询职位
     * @param model
     * @param pageIndex
     * @param job
     * @return
     */
    @RequestMapping(value = "/job/selectJob")
    public String selectJob(Model model, Integer pageIndex, @ModelAttribute Job job) {

        System.out.println("selectJob:" + job);
        PageModel pageModel = new PageModel();

        if (pageIndex != null) {
            //说明选中了
            pageModel.setPageIndex(pageIndex);
        }
        //查询用户信息
        List<Job> jobs = hrmService.findJob(job, pageModel);
        model.addAttribute("jobs", jobs);
        model.addAttribute("pageModel", pageModel);

        return "job/job";
    }

    /**
     * 根据id删除职位信息
     * @param ids
     * @param mv
     * @return
     */
    @RequestMapping(value="/job/removeJob")
    public ModelAndView removeJob(String ids,ModelAndView mv){
        //分解id字符串
        String[] idArray = ids.split(",");
        for(String id:idArray){
            //依次删除
            hrmService.removeJobById(Integer.parseInt(id));

        }
        //设置客户端跳转到查询请求
        mv.setViewName("redirect:/job/selectJob");
        //返回mv
        return mv;
    }

    /**
     * 添加职位信息
     * @param flag 标志位，1表示要跳转到要修改的页面， 2表示执行修改操作
     * @param job
     * @param mv
     * @return
     */
    @RequestMapping(value="job/addJob")
    public ModelAndView addJob(String flag, @ModelAttribute Job job, ModelAndView mv) {
        if(flag.equals("1")){
            //1表示跳转到添加页面，2表示执行添加操作
            mv.setViewName("job/showAddJob");
        }else{
            //添加操作
            hrmService.addJob(job);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/job/selectJob");

        }
        //返回
        return mv;
    }

    public ModelAndView updateJob(String flag,@ModelAttribute Job job,ModelAndView mv){
        //根据id查询部门
        if(flag.equals("1")){
            Job target = hrmService.findJobById(job.getId());
            //设置model数据
            mv.addObject("job", target);
            //设置跳转到修改页面
            mv.setViewName("job/showUpdateJob");
        }else{
            //执行修改操作
            hrmService.modifyJob(job);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/job/selectJob");

        }
        return mv;
    }
}
