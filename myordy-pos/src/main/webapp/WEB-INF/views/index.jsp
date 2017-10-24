<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html ng-app='MYORDY_POS_APP' ng-controller='MYORDY_POS_APP_CTRL'>
<head>
	<link rel='shortcut icon' type='image/x-icon' href='/myordy-resources/resources/myordy/layout-default/images/favicon.ico?v=<c:out value="${config.version}" />' />
	<meta charset='utf-8'>
	<meta http-equiv='X-UA-Compatible' content='IE=edge'>
	<meta name='viewport' content='width=device-width, initial-scale=1'>
	<title>myordy pos</title>

    <script src='/myordy-resources/resources/bootstrap/jquery-2.1.3.min.js?v=<c:out value="${config.version}" />'></script>
    <script src='/myordy-resources/resources/bootstrap/js/bootstrap.min.js?v=<c:out value="${config.version}" />'></script>

    <link href='/myordy-resources/resources/bootstrap/css/bootstrap.min.css?v=<c:out value="${config.version}" />' rel='stylesheet'>
    <link href='/myordy-resources/resources/css/main.css?v=<c:out value="${config.version}" />' rel='stylesheet'>

    <link href='/myordy-resources/resources/angular-google-places-autocomplete/dist/autocomplete.min.css?v=<c:out value="${config.version}" />' rel='stylesheet'>
    <link href='/myordy-resources/resources/intl-tel-input/build/css/intlTelInput.css' rel='stylesheet'>
    <link href='/myordy-resources/resources/angular-block-ui/angular-block-ui.min.css' rel='stylesheet'>


    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCqIR_HlqpC1QCP3FvFZ0-2bCHwUZ7Ul-M&signed_in=true&libraries=places"></script>
    <script src="/myordy-resources/resources/intl-tel-input/build/js/intlTelInput.js"></script>
	<c:if test = "${not empty STRIPE_MYORDY_publishableKey}"><script src="https://js.stripe.com/v3/"></script></c:if>

<!--	<script src='/myordy-resources/resources/angular-1.3.7/angular.min.js?v=<c:out value="${config.version}" />'></script>-->
	<script src='/myordy-resources/resources/angular-1.4.3/angular.min.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/angular-1.4.3/angular-cookies.min.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/angular-1.4.3/angular-sanitize.min.js?v=<c:out value="${config.version}" />'></script>


	<script src='/myordy-resources/resources/spinner/spin.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/spinner/angular-spinner.js?v=<c:out value="${config.version}" />'></script>

	<script src='/myordy-resources/resources/angular-google-places-autocomplete/dist/autocomplete.min.js?v=<c:out value="${config.version}" />'></script>

	<script src='/myordy-resources/resources/international-phone-number/releases/international-phone-number.js'></script>

    <script src="/myordy-resources/resources/angular-block-ui/angular-block-ui.min.js"></script>

	<script src='/myordy-resources/resources/myordy/user-locale.js?v=<c:out value="${config.version}" />'></script>

	<script src='/myordy-resources/resources/bootstrap/ui-bootstrap-tpls-0.12.0.min.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/myordy/myordy-common.js?v=<c:out value="${config.version}" />'></script>
	<script src='<c:url value="/resources/myordy-pos/pos-controller.js"/>?v=<c:out value="${config.version}" />'></script>
	<script type="text/javascript">
		var PAGE_LOAD_SERVER_TIME = <%= System.currentTimeMillis() %>;
		var CONTEXT_PATH = '${pageContext.request.contextPath}';
		var SHOP_ID = '${shopId}';
		var USER_IP_ADDR = '${userIPAddr}';
		var DUS = '${dus}';
		var STRIPE_MYORDY = {publishableKey:'${STRIPE_MYORDY_publishableKey}'};
		var PAYPAL_MYORDY = {clientToken:'${PAYPAL_MYORDY_clientToken}',env:'${PAYPAL_MYORDY_env}'};
	</script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src='<c:url value="/resources/bootstrap/html5shiv/3.7.2/html5shiv.min.js"/>'></script>
      <script src='<c:url value="/resources/bootstrap/respond/1.4.2/respond.min.js"/>'></script>
    <![endif]-->

</head>
<body ng-cloak>
	<div id='layout-default'>

		<div style="background:#333">
			<div class="container" style='padding:10px 0px;'>
				<div class=" col-xs-8" style='font-weight:bold;color:#fff;'>
					<h4 style='margin:5px 0px 2px 0px;'><img ng-src='${pageContext.request.contextPath}/shopImage/${shopId}/[LOGO-MAIN]' alt='{{posUI.shopLanguageTable[posUI.util.message.getShopNameCode(posUI.shop)]}}' title='{{posUI.shopLanguageTable[posUI.util.message.getShopNameCode(posUI.shop)]}}' /></h4>
				</div>
				<div class="col-xs-4 text-right">
					<div ng-show='!posUI.loginUI.loginResponse'><a style='color:#cccccc;' ng-click="posUI.loginUI.show(posUI)">Login</a></div>
					<div ng-show='posUI.loginUI.loginResponse'>
						<div style='color:#ffffff;'>{{posUI.loginUI.loginResponse.user.contact.name}}</div>
						<div style='margin-top:-1px;'><a style='color:#cccccc;' ng-click="posUI.loginUI.showLogout(posUI)">Logout</a></div>
					</div>
				</div>
			</div>
		</div>

<%--
		<div id="page-header">
			<div style="background:#000;">
				<div class="container" style='padding:10px 0px'>
					<div class="col-lg-12">
						<div class="col-xs-4 voffsetcenter3" style="width:105px"><img class="img-thumbnail imgthumb" src="<c:url value="/resources/shop/1/ordr1.jpg"/>"/></div>
						<div class="col-xs-4 voffsetcenter3" style="color:#fff">
							<h3>{{posUI.shopLanguageTable[posUI.util.message.getShopNameCode(posUI.shop)]}}</h3>
						</div>
						<div class="col-xs-4 text-right voffsetcenter4 pull-right">{{posUI.shopLanguageTable[posUI.util.message.getShopNameCode(posUI.shop)]}}</div>
					</div>
				</div>
			</div>
		</div>
--%>

		<div id="page-body" style='min-height:400px;'>
			<span us-spinner spinner-key="spinner-main"></span>
			<div id='body-header'></div>
			<div>
				<div class="container" style="background:#fff;padding-top:10px;">
					<%@ include file="/WEB-INF/views/FRAGMENTS/pos.jspf" %>
				</div>
			</div>
		</div>


		<div id='page-footer'>
			<div class="modal-footer" style="background: #efefef;color: #333;border-top:1px solid #aaa;">
<%--
				<div class='row'>
					<div class="col-lg-6 text-right"><img src="/myordy-resources/resources/myordy/layout-default/images/logo.png" style="width:80px;"></div>
					<div class="col-lg-6 text-left" style='padding-top:5px;'>
						<ul class="list-inline">
							<li><a ng-click="posUI.util.nav.openMyOrdyPopupContent('/myordy-resources/terms-of-service')">Terms & Conditions</a></li>
							<li><a ng-click="posUI.util.nav.openMyOrdyPopupContent('/myordy-resources/privacy-policy')">Privacy Policy</a></li>
							<li>&copy;{{pageLoadServerTime | date:'yyyy'}}</li>
						</ul>
					</div>
				</div>
--%>
			</div>
		</div>

	</div>

	<%@ include file="/WEB-INF/views/FRAGMENTS/posOrderPrint.jspf" %>
<%--
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src='/myordy-resources/resources/bootstrap/jquery-2.1.3.min.js?v=<c:out value="${config.version}" />'></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src='/myordy-resources/resources/bootstrap/js/bootstrap.min.js?v=<c:out value="${config.version}" />'></script>
--%>
</body>
</html>