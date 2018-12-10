package com.ssm.demo.entity.system;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "sys_user")
@Data
public class UserInfo extends DataEntity {


	@Column(name = "username",unique = true,length = 50)
	private String username;//帐号

	@Column(name = "name", length = 50)
	private String name;//名称（昵称或者真实姓名，不同系统不同定义）

	@Column(name = "password", length = 50)
	private String password; //密码;

	@Column(name = "salt", length = 255)
	private String salt;//加密密码的盐

	@Column(name = "state", length = 50)
	private byte state;//用户状态,0:创建未认证（比如没有激活，没有输入验证码等等）--等待验证的用户 , 1:正常状态,2：用户被锁定.

	@ManyToMany(fetch= FetchType.LAZY)
	@JoinTable(name = "SysUserRole", joinColumns = { @JoinColumn(name = "uid") }, inverseJoinColumns ={@JoinColumn(name = "roleId") })
	private Set<SysRole> roleList;// 一个用户具有一个角色


}