package com.ssm.demo.entity.system;

import com.ssm.demo.util.IdGen;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.io.Serializable;

@MappedSuperclass
public class IdEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(length = 40, updatable = false)
	private String id;

	@PrePersist
	public void preIdPersist() {
		if (StringUtils.isBlank(this.id)) {
			this.id = IdGen.uuid();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
