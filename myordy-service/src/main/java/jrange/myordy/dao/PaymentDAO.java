package jrange.myordy.dao;

import jrange.myordy.entity.CustomerPayment;
import jrange.myordy.entity.Ordy;


public interface PaymentDAO {
	public CustomerPayment onSubmitOrder(final Ordy ordy);
	public CustomerPayment save(final CustomerPayment customerPaymentArg);
//	public void createChargeStripe();

//	public CreateTransactionResponse ewayCreateTransaction(final EwayPaymentTransaction ewayPaymentTransaction);

//    public void ewayCustomerPaymentComplete(final RapidClientConfig config, final Ordy ordy, final Integer transactionID);

}
