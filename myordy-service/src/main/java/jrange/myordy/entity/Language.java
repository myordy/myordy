package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="language")
public final class Language implements Serializable {
    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="language_id")
    private Integer languageId;

    @Column(name="name", nullable = false)
	private String name;

    @Column(name="code", nullable = false)
	private String code;

    @Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getLanguageId() {
		return languageId;
	}

	public Language setLanguageId(final Integer languageId) {
		this.languageId = languageId;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

}
