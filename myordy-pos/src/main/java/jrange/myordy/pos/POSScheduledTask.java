package jrange.myordy.pos;

import jrange.myordy.dao.OrdyDAO;
import jrange.myordy.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class POSScheduledTask {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private OrdyDAO ordyDAO;

	@Scheduled(fixedRate = 120000) // every two min check
	public void ordyPendingConfirmationNotificationTask() {
		for (Integer shopId : ordyDAO.getOrdyPendingConfirmationShopIdList()) {
			notificationService.sendPOSAdminOrderConfirmationRequiredNotification(shopId);
		}
    }

}
