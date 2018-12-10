package com.ssm.demo.entity.system;

import com.ssm.demo.util.IdGen;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class DataEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 40, updatable = false)
	private String id;

	/** 逻辑删除标识 */
	@org.hibernate.annotations.Type(type = "yes_no")
	private Boolean delFlag;

	/** 启用标识 */
	@org.hibernate.annotations.Type(type = "yes_no")
	private Boolean enableFlag;

	/** 备注 */
	@Size(max = 2000)
	@Column(length = 2000)
	private String remark;

	/** 创建人 */
	@Column(name = "creater", updatable = false, length = 40)
	private String creater;

	/** 创建时间 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", updatable = false)
	private Date createDate;

	/** 修改人 */
	@Column(length = 40)
	private String updater;

	/** 修改时间 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	@PrePersist
	public void preDataPersist() {
		if (StringUtils.isBlank(this.id)) {
			this.id = IdGen.uuid();
		}
		this.updateDate = new Date();
		this.createDate = new Date();
	}

	@PreUpdate
	public void preDataUpdate() {
		this.updateDate = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public Boolean getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Boolean enableFlag) {
		this.enableFlag = enableFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
