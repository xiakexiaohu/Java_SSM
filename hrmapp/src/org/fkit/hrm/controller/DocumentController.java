package org.fkit.hrm.controller;

import org.apache.commons.io.FileUtils;
import org.fkit.hrm.domain.Document;
import org.fkit.hrm.domain.User;
import org.fkit.hrm.service.HrmService;
import org.fkit.hrm.util.common.HrmConstants;
import org.fkit.hrm.util.tag.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 文件处理器
 */
@Controller
public class DocumentController {
    @Autowired
    @Qualifier("hrmService")
    private HrmService hrmService;

    /**
     * 查找文件
     * @param model
     * @param pageIndex
     * @param document
     * @return
     */
    @RequestMapping(value="/document/selectDocument")
    public String selectDocument(Model model, Integer pageIndex, @ModelAttribute Document document) {
        PageModel pageModel = new PageModel();
        if(pageIndex!=null){
            pageModel.setPageIndex(pageIndex);
        }

        //查询文件
        List<Document> documents = hrmService.findDocument(document, pageModel);
        model.addAttribute("documents",documents);
        model.addAttribute("pageModel", pageModel);
        //返回
        return "/document/document";

    }

    /**
     * 添加文件
     * @param flag
     * @param document
     * @param mv
     * @param session
     * @return
     */
    @RequestMapping(value="/document/addDocument")
    public ModelAndView addDocument(String flag,@ModelAttribute Document document, ModelAndView mv, HttpSession session) throws IOException {
        if(flag.equals("1")){
            //跳转到上传页面
            mv.setViewName("document/showAddDocument");
        }else{
            //2表示执行上传操作
            String path = session.getServletContext().getRealPath("/upload/");

            System.out.println(path);
            //上传文件名
            String filename = document.getFile().getOriginalFilename();
            //将上传文件保存到一个目标文件中
            //File.separator与系统有关的默认名称分隔符window为\ unix为/
            document.getFile().transferTo(new File(path + File.separator + filename));

            //将文件插入到数据库中
            document.setFileName(filename);
            //设置关联的User对象(因为文件的创建由指定的用户创建)
            User user = (User) session.getAttribute(HrmConstants.USER_SESSION);
            document.setUser(user);
            //插入到数据库中
            hrmService.addDocument(document);
            //返回
            mv.setViewName("redirect:/document/selectDocument");

        }
        return mv;
    }

    /**
     * 删除文件
     *
     * @param ids 要删除的文件id编号
     * @param mv
     * @return
     */
    @RequestMapping(value = "/document/removeDocument")
    public ModelAndView removeDocument(String ids, ModelAndView mv) {
        //分解字符串
        String[] idArray = ids.split("1");
        for (String id : idArray) {
            //依次删除
            hrmService.removeDocumentById(Integer.parseInt(id));
        }

        //设置客户端跳转到查询请求
        mv.setViewName("redirect:/document/selectDocument");
        //返回ModelAndView
        return mv;
    }

    /**
     * 处理修改文档
     * @param flag
     * @param document
     * @param mv
     * @return
     */
    @RequestMapping(value="/document/updateDocument")
    public ModelAndView updateDocument(String flag,@ModelAttribute  Document document,ModelAndView mv){
        if(flag.equals("1")){
            //1,跳转到修改页面
            Document target = hrmService.findDocumentById(document.getId());
            //填充数据
            mv.addObject("document", target);
            //返回modelAndView
            mv.setViewName("document/showUpdateDocument");
        }else{
            //执行修改操作
            hrmService.modifyDocument(document);
            //设置客户端跳转到查询请求
            mv.setViewName("redirect:/document/selectDocument");
        }
        //返回
        return mv;
    }

    //处理下载文档

    /**
     * 处理下载文档
     * @param id 文档编号
     * @param session
     * @return
     */
    @RequestMapping(value="/document/downLoad")
    public ResponseEntity<byte[]> downLoad(Integer id,HttpSession session) throws Exception {
        //因为是下载文档，所以需要用字节数组
        // ResponseEntity:该类实现响应头、文件数据（以字节存储）、状态封装在一起交给浏览器处理以实现浏览器的文件下载。
        //根据id查询文档编号
        Document target = hrmService.findDocumentById(id);
        //获取文件名
        String fileName = target.getFileName();

        //获取文件路径
        String path = session.getServletContext().getRealPath("/upload/");
        //获得要下载的File对象
        File file = new File(path + File.separator + fileName);
        //创建springframework的HttpHeaders对象
        HttpHeaders headers = new HttpHeaders();
        //下载显示的文件名,解决文件名称乱码问题
        String downLoadFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        //通知浏览器以attachment（下载方式）打开文件
        headers.setContentDispositionFormData("attachment", downLoadFileName);
        //application/octet-stream:二进制流数据(最常见的文件下载)
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //返回状态码：201 HttpStatus.CREATED :请求已经被实现，而且有一个新的资源已经依据请求的需要而建立，且其 URI 已经随Location 头信息返回
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);

    }
}
