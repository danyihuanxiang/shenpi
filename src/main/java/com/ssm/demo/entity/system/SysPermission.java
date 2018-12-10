package com.ssm.demo.entity.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by lp on 2018/10/30.
 */
@Entity
@Table(name = "sys_permission")
@Data
public class SysPermission extends DataEntity  {

    @Column(name = "name", length = 50)
    private String name;//名称.

    @Column(columnDefinition="enum('menu','button')")
    private String resourceType;//资源类型，[menu|button]

    @Column(name = "url", length = 50)
    private String url;//资源路径.

    @Column(name = "permission", length = 50)
    private String permission; //权限字符串,menu例子：role:*，button例子：role:create,role:update,role:delete,role:view

    @Column(name = "parentId", length = 50)
    private Long parentId; //父编号

    @Column(name = "parentIds", length = 50)
    private String parentIds; //父编号列表

    @Column(name = "available", length = 50)
    private Boolean available = Boolean.FALSE;

}
