package jrange.myordy.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.Shop;
import jrange.myordy.util.MyOrdyHomeUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

	@Autowired
	ShopDAO shopDAO;

	@Async
	public void sendCustomerOrderReceivedNotification(final Ordy ordy) {
		final Properties shopProperties = MyOrdyHomeUtil.getShopProperties(ordy.getShopId());
		if (StringUtils.isNotEmpty(shopProperties.getProperty("email.customer.order.received.subject"))) {
			final Shop shop = shopDAO.getLite(ordy.getShopId());
			
			final EmailMessage em = new EmailMessage();
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setTimeZone(TimeZone.getTimeZone(shop.getTimezone()));
			
			em.senderEmailAddress = shopProperties.getProperty("email.customer.order.received.from");
			em.toEmailAddress = ordy.getCustomerEmail();
			em.subject = shopProperties.getProperty("email.customer.order.received.subject") + " [" + ordy.getOrdyNumber() + "] [" + df.format(ordy.getOrderTime()) + "]";
			em.content = shopProperties.getProperty("email.customer.order.received.content");
			em.contentType = "text/plain";
			
			sendEmailNotification(shopProperties, em);
		}
	}

	@Async
	public void sendPOSAdminOrderConfirmationRequiredNotification(final Integer shopId) {
		final Properties shopProperties = MyOrdyHomeUtil.getShopProperties(shopId);
		if (StringUtils.isNotEmpty(shopProperties.getProperty("email.posmanager.order.confirmationrequired.subject"))) {
			final Shop shop = shopDAO.getLite(shopId);

			final EmailMessage em = new EmailMessage();
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setTimeZone(TimeZone.getTimeZone(shop.getTimezone()));
			
			em.senderEmailAddress = shopProperties.getProperty("email.posmanager.order.confirmationrequired.from");
			em.toEmailAddress = shopProperties.getProperty("email.posmanager.order.confirmationrequired.to");
			em.subject = shopProperties.getProperty("email.posmanager.order.confirmationrequired.subject") + " [" + df.format(Calendar.getInstance().getTime()) + "]";
			em.content = shopProperties.getProperty("email.posmanager.order.confirmationrequired.content");
			em.contentType = "text/plain";
			
			sendEmailNotification(shopProperties, em);
		}
	}

	@Async
	public void sendCustomerOrderConfirmedNotification(final Ordy ordy) {
		final Properties shopProperties = MyOrdyHomeUtil.getShopProperties(ordy.getShopId());
		if (StringUtils.isNotEmpty(shopProperties.getProperty("email.customer.order.confirmed.subject"))) {
			final Shop shop = shopDAO.getLite(ordy.getShopId());
			
			final EmailMessage em = new EmailMessage();
			
			final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			df.setTimeZone(TimeZone.getTimeZone(shop.getTimezone()));
			
			em.senderEmailAddress = shopProperties.getProperty("email.customer.order.confirmed.from");
			em.toEmailAddress = ordy.getCustomerEmail();
			em.subject = shopProperties.getProperty("email.customer.order.confirmed.subject") + " [" + ordy.getOrdyNumber() + "] [" + df.format(ordy.getOrderTime()) + "]";
			em.content = shopProperties.getProperty("email.customer.order.confirmed.content");
			em.contentType = "text/plain";
			
			sendEmailNotification(shopProperties, em);
		}
	}

	private static final void sendEmailNotification(final Properties shopProperties, final EmailMessage em) {
		if (StringUtils.isNotEmpty(shopProperties.getProperty("mail.smtp.host"))) {
			final Properties mailProperties = new Properties();
			mailProperties.put("mail.smtp.auth", "true");
			mailProperties.setProperty("mail.transport.protocol", "smtp");
			mailProperties.put("mail.smtp.host", shopProperties.getProperty("mail.smtp.host"));
			mailProperties.setProperty("mail.user", shopProperties.getProperty("mail.user"));
			mailProperties.setProperty("mail.password", shopProperties.getProperty("mail.password"));
			mailProperties.put("mail.smtp.port", shopProperties.getProperty("mail.smtp.port"));
			
			final javax.mail.Authenticator authenticator = new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(
						mailProperties.getProperty("mail.user"), mailProperties.getProperty("mail.password")
					);
				}
			};
			
			final Session session = Session.getDefaultInstance(mailProperties, authenticator);
			final Message msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress(em.senderEmailAddress));
				msg.setRecipients(Message.RecipientType.TO, new InternetAddress[] {new InternetAddress(em.toEmailAddress)});
				msg.setSubject(em.subject);
				msg.setContent(em.content, em.contentType);
				Transport.send(msg);
			} catch (final Throwable e) {
				LOGGER.log(Level.WARNING, "" + e.getLocalizedMessage(), e);
			}
		}
	}

	private static final class EmailMessage {
        private String senderEmailAddress;
        private String toEmailAddress;
        private String subject;
        private String content;
        private String contentType;
	}

}
