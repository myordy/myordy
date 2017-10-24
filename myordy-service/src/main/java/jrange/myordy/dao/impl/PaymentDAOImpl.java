package jrange.myordy.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import jrange.myordy.dao.PaymentDAO;
import jrange.myordy.dao.ShopDAO;
import jrange.myordy.entity.CustomerPayment;
import jrange.myordy.entity.Ordy;
import jrange.myordy.entity.PaymentType;
import jrange.myordy.entity.Shop;
import jrange.myordy.util.MyOrdyHomeUtil;
import jrange.myordy.util.OrdyUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.stripe.Stripe;
import com.stripe.model.Charge;

@Transactional
@Repository
public class PaymentDAOImpl extends HibernateDAOImpl implements PaymentDAO {

    private static final Logger LOGGER = Logger.getLogger(PaymentDAOImpl.class.getName());

	@Autowired
	ShopDAO shopDAO;

	@Override
	public CustomerPayment onSubmitOrder(final Ordy ordy) {
		CustomerPayment result = null;
		if (StringUtils.isNotBlank(ordy.getOnlinePaymentStripeToken())) {
			result = processPaymentStripe(ordy);
		} else if (StringUtils.isNotBlank(ordy.getOnlinePaymentPaypalNonce())) {
			result = processPaymentPaypal(ordy);
		}
		return result;
	}

	@Override
	public CustomerPayment save(final CustomerPayment customerPaymentArg) {
		if (null != customerPaymentArg) {
			getCurrentSession().saveOrUpdate(customerPaymentArg);
		}
		return customerPaymentArg;
	}

	private CustomerPayment processPaymentPaypal(final Ordy ordy) {
		final Shop shop = shopDAO.getLite(ordy.getShopId());
		CustomerPayment result = new CustomerPayment();
		result.setOrdy(ordy);
		result.setAmount(new Long(OrdyUtil.getFinalTotalAmount(ordy)));
		result.setCurrencyCode(shop.getCurrencyCode());
		result.setCustomer(ordy.getCustomer());
		result.setPaymentType(PaymentType.PAYPAL);
		result.setShop(shop);

		final BraintreeGateway gateway = new BraintreeGateway(MyOrdyHomeUtil.getShopProperties(ordy.getShopId()).getProperty("PAYPAL_MYORDY.clientToken"));

		TransactionRequest request = new TransactionRequest()
			.amount(new BigDecimal(result.getAmount() / 100))
			.paymentMethodNonce(ordy.getOnlinePaymentPaypalNonce())
			.options().submitForSettlement(true)
			.done();

		Result<Transaction> saleResult = gateway.transaction().sale(request);
		if (!saleResult.isSuccess()) {
			result = null;
			LOGGER.logp(Level.INFO, LOGGER.getName(), "processPaymentPaypal", saleResult.getMessage());
		}

		return result;
	}

	private CustomerPayment processPaymentStripe(final Ordy ordy) {
		final Shop shop = shopDAO.getLite(ordy.getShopId());
		CustomerPayment result = new CustomerPayment();
		result.setOrdy(ordy);
		result.setAmount(new Long(OrdyUtil.getFinalTotalAmount(ordy)));
		result.setCurrencyCode(shop.getCurrencyCode());
		result.setCustomer(ordy.getCustomer());
		result.setPaymentType(PaymentType.STRIPE);
		result.setShop(shop);

		final Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", new Integer(result.getAmount().intValue()));
		chargeParams.put("currency", shop.getCurrencyCode());
		chargeParams.put("description", "Charge for " + ordy.getCustomerEmail());
		chargeParams.put("source", ordy.getOnlinePaymentStripeToken());

		try {
			synchronized (PaymentDAOImpl.class) {
				Stripe.apiKey = MyOrdyHomeUtil.getShopProperties(ordy.getShopId()).getProperty("STRIPE_MYORDY.secretKey");
				final Charge charge = Charge.create(chargeParams);
				result.setNote(charge.getStatus());
				result.setTransactionRef(charge.getId());
			}
		} catch (final Throwable e) {
			result = null;
		}

		return result;
	}

/*
	@Override
	public CreateTransactionResponse ewayCreateTransaction(final EwayPaymentTransaction request) {

		final RapidClient client = RapidSDK.newRapidClient(
				request.getRapidClientConfig().getApiKey(),
				request.getRapidClientConfig().getPassword(),
				request.getRapidClientConfig().getRapidEndPoint());

		final Transaction transaction = new Transaction();

		final Customer customer = new Customer();
		customer.setFirstName(request.getCustomerName());
		customer.setPhone(request.getCustomerPhoneNumber());
		customer.setCustomerDeviceIP(request.getCustomerDeviceIP());
		customer.setEmail(request.getCustomerEmail());

		transaction.setCustomer(customer);

		final PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setTotalAmount(request.getPaymentTotalAmount());

		transaction.setPaymentDetails(paymentDetails);
		transaction.setRedirectURL("http://www.eway.com.au");
		transaction.setCancelURL("http://www.eway.com.au");
		transaction.setTransactionType(TransactionType.Purchase);
		

		final CreateTransactionResponse response = client.create(PaymentMethod.ResponsiveShared, transaction);

		return response;
	}

	@Override
	public void ewayCustomerPaymentComplete(final RapidClientConfig config, final Ordy ordy, final Integer transactionID) {

		final RapidClient client = RapidSDK.newRapidClient(config.getApiKey(), config.getPassword(), config.getRapidEndPoint());

		final QueryTransactionResponse qt = client.queryTransaction(transactionID);
		final TransactionStatus status = qt.getTransactionStatus();
//		Transaction transaction = qt.getTransaction();
//		transaction.getPaymentDetails().getInvoiceNumber();
//		transaction.getPaymentDetails().getInvoiceReference();
//		transaction.getCustomer().getEmail();
//		System.out.println(transaction.getCustomer().getEmail());
//		System.out.println(qt.getTransaction().getOriginalTransactionId());
//		System.out.println(qt.getTransaction().getPaymentDetails().getInvoiceReference());
//		System.out.println(status.getVerificationResult().getEmail());

		if (status.isStatus()) {
			final Transaction transaction = qt.getTransaction();
			final CustomerPayment cp = new CustomerPayment();
			transaction.getCurrencyCode();
			//transaction.getTransactionDateTime();
			transaction.getTransactionType();
			transaction.getPaymentDetails().getCurrencyCode();
			
			final PaymentDetails paymentDetails = transaction.getPaymentDetails();
			if (null != paymentDetails) {

				final String invoiceReference = OrdyUtil.getInvoiceReference(ordy);
				if (StringUtils.isNotEmpty(invoiceReference) && invoiceReference.equals(paymentDetails.getInvoiceReference())) {

					cp.setCurrencyCode(transaction.getCurrencyCode());
					cp.setAmount(new Long(paymentDetails.getTotalAmount()));
					cp.setCurrencyCode(paymentDetails.getCurrencyCode());
					cp.s
					
					
					cp.setCustomer(ordy.getCustomer());
					//cp.setNote(note);
					cp.setOrdy(ordy);
					cp.setPaymentType(PaymentType.EWAY);
					
					final Shop shop = new Shop();
					shop.setShopId(ordy.getShopId());
					cp.setShop(shop);
					
					cp.setTransactionRef(OrdyUtil.getEwayTransactionRef(transactionID));
				}
			}
		}
	}
*/

}
