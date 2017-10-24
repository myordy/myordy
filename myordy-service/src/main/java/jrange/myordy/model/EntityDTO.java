package jrange.myordy.model;

import java.util.Map;
import java.util.Set;

import jrange.myordy.entity.Message;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class EntityDTO <T> {
	private T entity;
	private Set<Message> messages;
	private Integer languageId;
	private Map<String, Integer> relatedEntityRef;

	public T getEntity() {
		return entity;
	}
	public void setEntity(final T entity) {
		this.entity = entity;
	}
	public Set<Message> getMessages() {
		return messages;
	}
	public void setMessages(final Set<Message> messages) {
		this.messages = messages;
	}
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(final Integer languageId) {
		this.languageId = languageId;
	}
	public Map<String, Integer> getRelatedEntityRef() {
		return relatedEntityRef;
	}
	public void setRelatedEntityRef(final Map<String, Integer> relatedEntityRef) {
		this.relatedEntityRef = relatedEntityRef;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
