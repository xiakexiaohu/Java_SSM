package org.fkit.hrm.controller;

import org.fkit.hrm.domain.User;
import org.fkit.hrm.service.HrmService;
import org.fkit.hrm.util.common.HrmConstants;
import org.fkit.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 处理用户请求控制器
 */
@Controller
public class UserController {

    /**
     * 自动注入UserSerivce
     */
    @Autowired
    @Qualifier("hrmService")   //与Inject最大的不同在，Inject所标注的联系必须存在，否在抛出异常
    private HrmService hrmService;


    //返回ModelAndView

    /**
     * 处理登录请求
     * @param loginnam
     * @param password
     * @param session
     * @param mv
     * @return
     */
    @RequestMapping(value="/login")
    public ModelAndView login(@RequestParam("loginname") String loginnam,
                              @RequestParam("password") String password, HttpSession session,ModelAndView mv){
        //调用业务逻辑组件判断用户是否可以登录
        User user = hrmService.login(loginnam, password);
        if(user!=null){
            //用户不为空,将用户信息保存到session中
            session.setAttribute(HrmConstants.USER_SESSION, user);
            //客户端跳转到Main页面
            mv.setViewName("redirect:/main");
        }else{
            //用户为空，则设置登录失败提示信息
            mv.addObject("message", "登录名或密码错误！请重新输入");
            //服务器内部跳转到登录页面
            mv.setViewName("forward:/loginForm");
        }
        return mv;
    }


    /**
     * 处理查询请求
     * @param pageIndex 请求的第几页
     * @param user
     * @param model
     * @return
     */
    @RequestMapping(value="/user/selectUser")
    //ModelAttribute用户绑定参数
    public String selectUser(Integer pageIndex, @ModelAttribute User user, Model model){
        System.out.println("user="+user);
        PageModel pageModel = new PageModel();
        if(pageIndex!=null){
            pageModel.setPageIndex(pageIndex);
        }
        //查询用户信息
        List<User> users = hrmService.findUser(user, pageModel);
        model.addAttribute("users", users);
        model.addAttribute("pageModel", pageModel);
        return "user/user";
    }


    /**
     * 处理删除用户请求
     * @param ids 需要删除的id字符串
     * @param modelAndView
     * @return
     */
    @RequestMapping(value="/user/removeUser")
    public ModelAndView removeUser(String ids, ModelAndView mv) {
        //分解id字符串
        String[] idArrays = ids.split(",");
        for(String id:idArrays){
            //分别删除
            hrmService.removeUserById(Integer.parseInt(id));
        }
        //设置客户端跳转到查询请求
        mv.setViewName("redirect:/user/selectUser");
        //返回modelAndView
        return mv;
    }

    /**
     * 处理修改用户请求
     * @param flag 标志位，1表示要跳转到修改页面，2表示执行修改操作
     * @param user
     * @param mv
     * @return
     */
    @RequestMapping(value="/user/updateUser")
    public ModelAndView updateUser(String flag,@ModelAttribute User user,ModelAndView mv){
        if(flag.equals("1")){
            //要跳转到修改页面
            User target = hrmService.findUserById(user.getId());
            //设置model数据
            mv.addObject("user", target);
            //返回修改员工页面
            mv.setViewName("user/showUpdateUser");

        }else{
            //表示执行修改操作
            hrmService.modifyUser(user);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/user/selectUser");

        }
        //返回modelAndView
        return mv;
    }

    /**
     * 处理添加请求
     * @param flag 标志位，1表示跳转到添加页面，2表示执行添加操作
     * @param user 要添加用户的对象
     * @param mv
     * @return
     */
    @RequestMapping(value="/user/addUser")
    public ModelAndView addUser(String flag,@ModelAttribute User user, ModelAndView mv) {
        if(flag.equals("1")){
            //设置跳转到添加页面
            mv.setViewName("user/showAddUser");
        }else{
            //执行添加操作
            hrmService.addUser(user);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/user/selectUser");
        }
        //返回
        return mv;
    }

    /**
     * 处理注销退出请求
     * @param mv
     * @param session
     * @return
     */
    @RequestMapping(value="/logout")
    public ModelAndView logout(ModelAndView mv,HttpSession session){
        //注销session
        session.invalidate();
        //跳转到登录页面
        mv.setViewName("redirect:/loginForm");
        return mv;
    }

}
