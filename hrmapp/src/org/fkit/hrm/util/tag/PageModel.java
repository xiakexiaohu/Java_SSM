package org.fkit.hrm.util.tag;

import org.fkit.hrm.util.common.HrmConstants;

/**
 * 分页标签
 */
public class PageModel {
    //分页总的数据条数
    private int recordCount;
    //当前页面
    private int pageIndex;
    //每页分多少跳数据
    private int pageSize= HrmConstants.PAGE_DEFAULT_SIZE;

    //总页数
    private int totalSize;

    //获取记录条数
    public int getRecordCount(){
        this.recordCount=this.recordCount<=0?0:this.recordCount;
        return recordCount;
    }

    //设定记录条数
    public void setRecordCount(int recordCount){
        this.recordCount=recordCount;
    }


    //设定页面数
    public void setPageSize(int pageSize){
        this.pageSize=pageSize;
    }

    //获取每个页面条数
    public int getPageSize(){
        this.pageSize=this.pageSize<=HrmConstants.PAGE_DEFAULT_SIZE?HrmConstants.PAGE_DEFAULT_SIZE:this.pageSize;
        return pageSize;
    }

    //设定当前页面下表
    public void setPageIndex(int pageIndex){
        this.pageIndex=pageIndex;
    }

    //获取当前页码
    public int getPageIndex(){
        this.pageIndex=this.pageIndex<=0?1:this.pageIndex;
        //判断当前页面是否超过了总页数，如果超过了默认给最后一页为当前页
        this.pageIndex=this.pageIndex>=this.getTotalSize()?this.getTotalSize():this.pageIndex;
        return pageIndex;
    }

    //获取总的页面数
    public int getTotalSize(){
        if(this.getRecordCount()<=0){
            totalSize=0;
        }else{
            totalSize=(this.getRecordCount()-1)/this.getPageSize()+1;
        }
        return totalSize;
    }

    //获取当前第几条记录数
    public int getFirstLimitParam(){
        return (this.getPageIndex() - 1) * this.getPageSize();
    }



}
