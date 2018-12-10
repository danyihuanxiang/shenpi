package com.ssm.demo.entity.system;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "sys_role")
@Data
public class SysRole extends DataEntity  {


	@Column(name = "role", length = 50)
	private String role; // 角色标识程序中判断使用,如"admin",这个是唯一的:

	@Column(name = "description", length = 50)
	private String description; // 角色描述,UI界面显示使用

	@Column(name = "available", length = 50)
	@org.hibernate.annotations.Type(type = "yes_no")
	private Boolean available; // 是否可用

	//角色 -- 权限关系：多对多关系;
	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(name="SysRolePermission",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="permissionId")})
	private Set<SysPermission> permissions;

	// 用户 - 角色关系定义;
	/*@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(name="SysUserRole",joinColumns={@JoinColumn(name="roleId")},inverseJoinColumns={@JoinColumn(name="uid")})
	private Set<UserInfo> userInfos;// 一个角色对应多个用户*/


}
