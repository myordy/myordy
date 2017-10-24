package jrange.myordy.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.TagDAO;
import jrange.myordy.entity.Tag;

import org.hibernate.Criteria;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class TagDAOImpl extends HibernateDAOImpl implements TagDAO {

	@Override
	public Tag save(Tag tagArg) {
		Tag tag = tagArg;
		if (tag.getTagId() != null) {
			tag.setCode(tagArg.getCode());
			tag.setShopId(tagArg.getShopId());
		}
		getCurrentSession().saveOrUpdate(tag);
		return tag;
	}

	@Override
	public Tag get(final Integer id) {
		final Tag result = (Tag) getCurrentSession().get(Tag.class, id);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> list() {
		return getCurrentSession().createQuery("from Tag").list();
	}

	@Override
	public Tag getByCode(String code, Integer shopId) {
		final Criteria criteria = getCurrentSession().createCriteria(Tag.class)
				.add(Restrictions.eq("code", code))
				.add(Restrictions.eq("shopId", shopId));
		Tag result = null;
//		try {
			result = (Tag) criteria.uniqueResult();
//		} catch (ObjectNotFoundException e) {}
//		List<Tag> list = list();
//		if (null != list) {
//			for (Tag t : list) {
//				if (t.getCode().equalsIgnoreCase(code)) {
//					result = t;
//					break;
//				}
//			}
//		}
		return result;
	}

}
//  