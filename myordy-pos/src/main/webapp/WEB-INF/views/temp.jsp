<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<html>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

</head>

<body>

<div id="paypal-button"></div>
<c:if test = "${not empty STRIPE_MYORDY_publishableKey}">
<h1>XXX</h1>
</c:if>
<c:if test = "${not empty PAYPAL_clientToken}">
<h1>YYY</h1>
</c:if>

<script src="https://www.paypalobjects.com/api/checkout.js"></script>
<!-- Load the required components. -->
<script src="https://js.braintreegateway.com/web/3.15.0/js/client.min.js"></script>
<script src="https://js.braintreegateway.com/web/3.15.0/js/paypal-checkout.min.js"></script>

<!-- Use the components. We'll see usage instructions next. -->
<script>

braintree.client.create({
  authorization: '${PAYPAL_clientToken}'
}, function (clientErr, clientInstance) {

  if (clientErr) {
    console.error('Error creating client:', clientErr);
    return;
  }

  // Create a PayPal Checkout component.
  braintree.paypalCheckout.create({
    client: clientInstance
  }, function (paypalCheckoutErr, paypalCheckoutInstance) {

    if (paypalCheckoutErr) {
      console.error('Error creating PayPal Checkout:', paypalCheckoutErr);
      return;
    }

    // Set up PayPal with the checkout.js library
    paypal.Button.render({
      env: 'sandbox', // 'production', // or 'sandbox'

      payment: function () {
        return paypalCheckoutInstance.createPayment({
          flow: 'checkout', // Required
          amount: 10.00, // Required
          currency: 'AUD' // Required
        });
      },

      onAuthorize: function (data, actions) {
        return paypalCheckoutInstance.tokenizePayment(data)
          .then(function (payload) {
            // Submit `payload.nonce` to your server
            console.log(payload.nonce);
          });
      },

      onCancel: function (data) {
        console.log('checkout.js payment cancelled', JSON.stringify(data, 0, 2));
      },

      onError: function (err) {
        console.error('checkout.js error', err);
      }
    }, '#paypal-button').then(function () {
    });

  });

});

</script>





<%--

<div id="paypal-button"></div>

<script src="https://www.paypalobjects.com/api/checkout.js"></script>
<script>
    paypal.Button.render({

        env: 'sandbox', //'production', // Or 'sandbox'

        client: {
            sandbox:    'AdAo6aTAfNvJkdtdYpUg3iKMQS25pcpiUCrOXRSCvo35iWkkB6DHuEd549-563WCd_LTpjV0N7Rj-4U5',
            production: 'Ab7SxyPPV6l1ogHlP2yPASwYA8v0vIF2Czgd02jGwFRNKoFWwrQPdxtvLph-czm5jwFWh4Wv-jd6xylg'
        },

        commit: true, // Show a 'Pay Now' button

        payment: function() {
			var env = this.props.env;
			var client = this.props.client;
			return paypal.rest.payment.create(env, client, {
			    intent: "sale",
			    payer: { payment_method: "paypal" },
			    transactions: [
			        {
			            amount: { total: '14.00', currency: 'AUD' },
			            description: "This is a payment description"
			        },
			    ],
			    redirect_urls: {
			        return_url: "http://somesite.com/success",
			        cancel_url: "http://somesite.com/cancel"
			    }
			});
        },

        onAuthorize: function(data, actions) {
            return actions.payment.execute().then(function(payment) {
                // The payment is complete!
                // You can now show a confirmation message to the customer
            });
        }

    }, '#paypal-button');
</script>
--%>


<%--
<div id="paypal-button"></div>

<script src="https://www.paypalobjects.com/api/checkout.js"></script>

<script>
    var CREATE_PAYMENT_URL  = '/myordy-pos/create-payment';
    var EXECUTE_PAYMENT_URL = 'https://my-store.com/paypal/execute-payment';

    paypal.Button.render({

        env: 'sandbox', //'production', // Or 'sandbox'

        commit: true, // Show a 'Pay Now' button

        payment: function() {
            return paypal.request.post(CREATE_PAYMENT_URL).then(function(data) {
                return data.id;
            });
        },

        onAuthorize: function(data) {
            return paypal.request.post(EXECUTE_PAYMENT_URL, {
                paymentID: data.paymentID,
                payerID:   data.payerID
            }).then(function() {
                // The payment is complete!
                // You can now show a confirmation message to the customer
            });
        }
    }, '#paypal-button');
</script>
--%>


</body>
</html>