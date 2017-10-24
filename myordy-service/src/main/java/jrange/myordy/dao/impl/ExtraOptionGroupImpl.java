package jrange.myordy.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.ExtraOptionGroupDAO;
import jrange.myordy.entity.menuitemoption.ExtraOptionGroup;
import jrange.myordy.entity.menuitemoption.ExtraOptionItem;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class ExtraOptionGroupImpl extends HibernateDAOImpl implements ExtraOptionGroupDAO {

	@Override
	public ExtraOptionGroup save(ExtraOptionGroup extraOptionGroupArg) {
		ExtraOptionGroup extraOptionGroup = extraOptionGroupArg;
		if (extraOptionGroup.getExtraOptionGroupId() != null) {
//			tag.setCode(tagArg.getCode());
//			tag.setShopId(tagArg.getShopId());
		}
		getCurrentSession().saveOrUpdate(extraOptionGroup);
		return extraOptionGroup;
	}

	@Override
	public ExtraOptionGroup get(Integer id) {
		final ExtraOptionGroup result = (ExtraOptionGroup) getCurrentSession().get(ExtraOptionGroup.class, id);
		return result;
	}

	@Override
	public List<ExtraOptionGroup> list(final Integer businessId) {
		final Criteria criteria = getCurrentSession().createCriteria(ExtraOptionGroup.class);
		Criteria businessCriteria = criteria.createCriteria("business");
		businessCriteria.add(Restrictions.eq("businessId", businessId));
		return criteria.list();
	}

	@Override
	public ExtraOptionGroup getByCode(String code, Integer businessId) {
		final Criteria criteria = getCurrentSession().createCriteria(ExtraOptionGroup.class)
				.add(Restrictions.eq("code", code));

		Criteria businessCriteria = criteria.createCriteria("business");
		businessCriteria.add(Restrictions.eq("businessId", businessId));

		ExtraOptionGroup result = (ExtraOptionGroup) criteria.uniqueResult();;
		return result;
	}

	@Override
	public ExtraOptionItem saveExtraOptionItem(final ExtraOptionItem extraOptionItemArg) {
		ExtraOptionItem extraOptionItem = extraOptionItemArg;
		if (extraOptionItem.getExtraOptionItemId() != null) {
//			tag.setCode(tagArg.getCode());
//			tag.setShopId(tagArg.getShopId());
		}
		getCurrentSession().saveOrUpdate(extraOptionItem);
		return extraOptionItem;
	}

}
