package jrange.myordy.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import jrange.myordy.dao.LanguageDAO;
import jrange.myordy.entity.Language;

import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class LanguageDAOImpl extends HibernateDAOImpl implements LanguageDAO {

	@Override
	public Language save(Language languageArg) {
		Language language = languageArg;
		if (language.getLanguageId() != null) {
			language = get(language.getLanguageId());
			language.setCode(languageArg.getCode());
			language.setName(languageArg.getName());
		}
		getCurrentSession().saveOrUpdate(language);
		return language;
	}

	@Override
	public Language get(final Integer id) {
		final Language result = (Language) getCurrentSession().get(Language.class, id);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Language> list() {
		return getCurrentSession().createQuery("from language").list();
	}

}
