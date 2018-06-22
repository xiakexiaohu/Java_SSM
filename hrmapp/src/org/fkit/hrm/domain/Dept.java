package org.fkit.hrm.domain;

import java.io.Serializable;

public class Dept implements Serializable {
    //pojo类
    private Integer id;
    //部门名称
    private String name;
    //部门详细描述
    private String remark;
    public Dept(){
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
