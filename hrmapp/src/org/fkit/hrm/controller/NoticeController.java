package org.fkit.hrm.controller;

import org.fkit.hrm.domain.Notice;
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
import org.springframework.web.servlet.ModelAndView;

import javax.jms.Session;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 消息处理器
 */
@Controller
public class NoticeController {
    @Autowired
    @Qualifier("hrmService")
    private HrmService hrmService;

    /**
     * 查询所有Notice信息
     * @param model
     * @param pageIndex
     * @param notice
     * @return
     */
    @RequestMapping(value="/notice/selectNotice")
    public String selectNotice(Model model, Integer pageIndex, @ModelAttribute Notice notice) {
        PageModel pageModel = new PageModel();
        //判断pageIndex是否为空
        if(pageIndex!=null){
            //不为空，填充数据
            pageModel.setPageIndex(pageIndex);
        }
        //查询公告
        List<Notice> notices = hrmService.findNotice(notice, pageModel);
        //将数据放入model
        model.addAttribute("notices", notices);
        model.addAttribute("pageModel", pageModel);
        return "notice/notice";
    }

    /**
     * 查看单条信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="/notice/previewNotice")
    public String previewNotice(Integer id, Model model) {
        Notice notice = hrmService.findNoticeById(id);
        //填充数据
        model.addAttribute("notice", notice);
        //返回
        return "notice/previewNotice";
    }

    /**
     * 删除消息
     * @param ids
     * @param mv
     * @return
     */
    @RequestMapping(value="/notice/removeNotice")
    public ModelAndView removeNotice(String ids, ModelAndView mv) {
        String[] idArray = ids.split(",");
        for(String id:idArray){
            //依次删除
            hrmService.removeNoticeById(Integer.parseInt(id));
        }
        //设置客户端跳转到查询请求
        mv.setViewName("redirect:/notice/selectNotice");
        //返回
        return mv;
    }

    /**
     * 处理添加信息
     * @param flag 1表示跳转到添加页面，2表示执行添加操作
     * @param notice 要添加的公告对象
     * @param mv
     * @param session
     * @return
     */
    @RequestMapping(value="/notice/addNotice")
    public ModelAndView addNotice(String flag, @ModelAttribute  Notice notice, ModelAndView mv, HttpSession session){
        if(flag.equals("1")){
            mv.setViewName("notice/showAddNotice");
        }else{
            //创建session，即创建公告为该用户创建的
            User user =(User) session.getAttribute(HrmConstants.USER_SESSION);
            //将用户信息添加入公告中
            notice.setUser(user);
            //添加notice
            hrmService.addNotice(notice);
            //设置客户端返回
            mv.setViewName("redirect:/notice/selectNotice");
        }
        //返回
        return mv;
    }


    public ModelAndView updateNotice(String flag, @ModelAttribute  Notice notice, ModelAndView mv, HttpSession session){
        if(flag.equals("1")){
            //表示要跳转的页面
            Notice target = hrmService.findNoticeById(notice.getId());
            //填充
            mv.addObject("notice", target);
            //返回地址
            mv.setViewName("notice/showUpdateNotice");
        }else{
            //执行修改操作
            hrmService.modifyNotice(notice);
            //设置客户端地址
            mv.setViewName("redirect:/notice/selectNotice");
        }
        //返回
        return mv;
    }

}
