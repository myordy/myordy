package jrange.myordy.dao;

import java.util.List;
import java.util.Set;

import jrange.myordy.entity.Message;

public interface MessageDAO {
    public Message save(Message message);
	public Message get(final Integer id);
    public List<Message> list();
    public List<Message> updateMessageByCode(Integer businessId, Integer languageId, Set<Message> messages);
	public Message getByCode(final Integer businessId, final Integer languageId, String code);
}
