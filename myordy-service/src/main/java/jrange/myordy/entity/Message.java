package jrange.myordy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="message")
public final class Message implements Serializable {

    private static final long serialVersionUID = 1L;

	@GeneratedValue
    @Id
    @Column(name="message_id")
    private Integer messageId;

    @Column(name="code", nullable = false)
	private String code;

    @Column(name="message", nullable = false)
	private String message;

	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = true, updatable = false)
    private Business business;

	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id", nullable = false, updatable = false)
    private Language language;

	@Column(name="status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EntityStatus status;

	public Integer getMessageId() {
		return messageId;
	}

	public Message setMessageId(final Integer messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Message setMessage(final String message) {
		this.message = message;
		return this;
	}

	public Language getLanguage() {
		return language;
	}

	public Message setLanguage(final Language language) {
		this.language = language;
		return this;
	}

	public String getCode() {
		return code;
	}

	public Message setCode(final String code) {
		this.code = code;
		return this;
	}

	public EntityStatus getStatus() {
		return status;
	}

	public void setStatus(final EntityStatus status) {
		this.status = status;
	}

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(final Business business) {
		this.business = business;
	}

}
