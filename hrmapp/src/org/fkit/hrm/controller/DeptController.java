package org.fkit.hrm.controller;

import org.fkit.hrm.domain.Dept;
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
 * 部门处理器
 */
@Controller
public class DeptController {
    //自动注入
    @Autowired
    @Qualifier("hrmService")
    private HrmService hrmService;

    /**
     *查询部门
     * @param model
     * @param pageIndex
     * @param dept
     * @return
     */
    @RequestMapping(value="/dept/selectDept")
    public String selectDept(Model model, Integer pageIndex,@ModelAttribute  Dept dept){
        System.out.println("selectDept->>");
        System.out.println("pageIndex:"+pageIndex);
        System.out.println("dept=" + dept);

        PageModel pageModel = new PageModel();
        System.out.println("getPageIndex:" + pageModel.getPageIndex());
        System.out.println("getPageSize:" + pageModel.getPageSize());
        System.out.println("getRecordCount:" + pageModel.getRecordCount());

        if(pageIndex!=null){
            pageModel.setPageIndex(pageIndex);
        }

        //查询部门信息
        List<Dept> depts = hrmService.findDept(dept, pageModel);
        model.addAttribute("depts", depts);
        model.addAttribute("pageModel", pageModel);
        return "dept/dept";
    }

    /**
     * 根据ids删除部门信息
     * @param ids
     * @param mv
     * @return
     */
    @RequestMapping(value="/dept/removeDept")
    public ModelAndView removeDept(String ids,ModelAndView mv){
        //分解id字符串
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            //依次删除部门信息
            hrmService.removeDeptById(Integer.parseInt(id));
        }
        //设置客户端跳转到查询请求
        mv.setViewName("redirect:/dept/selectDept");
        return mv;
    }

    /**
     * 执行添加部门信息
     * @param flag
     * @param dept
     * @param mv
     * @return
     */
    @RequestMapping(value="/dept/addDept")
    public ModelAndView addDept(String flag,@ModelAttribute Dept dept,ModelAndView mv){
        if(flag.equals("1")){
            //设置跳转到添加页面
            mv.setViewName("dept/showAddDept");

        }else{
            //执行添加操作
            hrmService.addDept(dept);
            //设置客户端跳转到到查询请求
            mv.setViewName("redirect:/dept/selectDept");
        }
        //返回
        return mv;
    }

    /**
     * 更新部门信息
     * @param flag
     * @param dept
     * @param mv
     * @return
     */
    @RequestMapping(value="/dept/updateDept")
    public ModelAndView updateDept(String flag,@ModelAttribute Dept dept,ModelAndView mv) {
        if(flag.equals("1")){
            //根据id查询部门信息
            Dept target = hrmService.findDeptById(dept.getId());
            //设置model数据
            mv.addObject("dept", target);
            //设置跳转到修改页面
            mv.setViewName("dept/showUpdateDept");
        }else{
            //执行修改操作
            hrmService.modifyDept(dept);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/dept/selectDept");
        }
        //返回
        return mv;
    }

}
