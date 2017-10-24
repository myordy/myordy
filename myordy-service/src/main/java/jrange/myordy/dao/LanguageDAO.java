package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Language;

public interface LanguageDAO {
    public Language save(Language language);
	public Language get(final Integer id);
    public List<Language> list();
}
