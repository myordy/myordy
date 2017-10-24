package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.Tag;

public interface TagDAO {
    public Tag save(Tag tag);
	public Tag get(final Integer id);
	public Tag getByCode(final String code, Integer shopId);
    public List<Tag> list();
}
