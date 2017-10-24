package jrange.myordy.util;

import java.util.Collection;

import jrange.myordy.entity.Message;

public class MessageUtil {

	public static Message findMessage(final String code, final Collection<Message> messageList) {
		Message result = new Message();
		for (Message message : messageList) {
			if (message.getCode().equalsIgnoreCase(code)) {
				result = message;
				break;
			}
		}
		return result;
	}

}
