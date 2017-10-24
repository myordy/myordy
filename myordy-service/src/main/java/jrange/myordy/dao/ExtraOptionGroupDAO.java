package jrange.myordy.dao;

import java.util.List;

import jrange.myordy.entity.menuitemoption.ExtraOptionGroup;
import jrange.myordy.entity.menuitemoption.ExtraOptionItem;

public interface ExtraOptionGroupDAO {

	public ExtraOptionGroup save(ExtraOptionGroup extraOptionGroup);

	public ExtraOptionItem saveExtraOptionItem(ExtraOptionItem extraOptionItem);

	public ExtraOptionGroup get(final Integer id);

    public List<ExtraOptionGroup> list(final Integer businessId);

	public ExtraOptionGroup getByCode(final String code, Integer businessId);

}
