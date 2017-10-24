package jrange.myordy.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import jrange.myordy.dao.MessageDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Message;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class MessageDAOImpl extends HibernateDAOImpl implements MessageDAO {

	@Override
	public Message save(final Message m) {
		Message message = m;
		if (m.getMessageId() != null) {
			message = get(m.getMessageId());
			message.setLanguage(m.getLanguage());
			message.setMessage(m.getMessage());
			message.setBusiness(m.getBusiness());
		}
		getCurrentSession().saveOrUpdate(message);
		return message;
	}

	@Override
	public Message get(final Integer id) {
		final Message result = (Message) getCurrentSession().get(Message.class, id);
		Hibernate.initialize(result.getLanguage());
		Hibernate.initialize(result.getBusiness());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> list() {
		return getCurrentSession().createQuery("from Message").list();
	}

	@Override
	public List<Message> updateMessageByCode(final Integer businessId, final Integer languageId, final Set<Message> messages) {

		final Language language = (Language) getCurrentSession().get(Language.class, languageId);
		final Business business = (Business) getCurrentSession().get(Business.class, businessId);

		final List<Message> result = new ArrayList<Message>();
		for (Message m : messages) {
			Message message = (Message) getCurrentSession().createCriteria(Message.class)
				.add(Restrictions.eq("business.businessId", business.getBusinessId()))
				.add(Restrictions.eq("language.languageId", language.getLanguageId()))
				.add(Restrictions.eq("code", m.getCode())).uniqueResult();

			if (null == message) {
				message = new Message();
				message.setLanguage(language);
				message.setMessage(m.getMessage());
				message.setCode(m.getCode());
				message.setBusiness(business);
				save(message);
			} else {
				message.setMessage(m.getMessage());
				getCurrentSession().update(message);
			}
			result.add(message);
		}

		return result;
	}

	@Override
	public Message getByCode(final Integer businessId, final Integer languageId, final String code) {
		Criteria messageCriteria = getCurrentSession().createCriteria(Message.class);
		messageCriteria.add(Restrictions.eq("language.languageId", languageId));
		messageCriteria.add(Restrictions.eq("code", code)).uniqueResult();

		if (null == businessId) {
			messageCriteria.add(Restrictions.isNull("business.businessId"));
		} else {
			messageCriteria.add(Restrictions.eq("business.businessId", businessId));
		}
		Message message = (Message) messageCriteria.uniqueResult();
		return message;
	}

}
