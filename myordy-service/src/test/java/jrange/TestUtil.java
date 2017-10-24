package jrange;

import java.util.HashSet;
import java.util.Set;

import jrange.myordy.dao.MessageDAO;
import jrange.myordy.dao.TagDAO;
import jrange.myordy.entity.Business;
import jrange.myordy.entity.EntityStatus;
import jrange.myordy.entity.Language;
import jrange.myordy.entity.Message;
import jrange.myordy.entity.Shop;
import jrange.myordy.entity.Tag;

public class TestUtil {

	public static void insertMessage(MessageDAO messageDAO, String code, String theMessage, Business sylvesters, Language language) {
		Message message = new Message();
		message.setLanguage(language);
		message.setMessage(theMessage);
		message.setCode(code);
		message.setBusiness(sylvesters);
		message.setStatus(EntityStatus.ACTIVE);
		messageDAO.save(message);
	}

	public static Set<Tag> saveTags(TagDAO tagDAO, Set<String> tags, Shop shop) {
		Set<Tag> result = new HashSet<Tag>();
		Tag tag = null;
		for (String t : tags) {
			tag = tagDAO.getByCode(t, shop.getShopId());
			if (null == tag) {
				tag = new Tag();
				tag.setCode(t);
				tag.setShopId(shop.getShopId());
				tag.setStatus(EntityStatus.ACTIVE);
				tag = tagDAO.save(tag);
			}
			result.add(tag);
		}
		return result;
	}


}
