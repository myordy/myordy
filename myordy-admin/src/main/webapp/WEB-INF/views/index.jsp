<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html ng-app='MYORDY_POS_ADMIN_APP' ng-controller='MYORDY_POS_ADMIN_APP_CTRL'>
<head>
	<link rel='shortcut icon' type='image/x-icon' href='/myordy-resources/resources/myordy/layout-default/images/favicon.ico?v=<c:out value="${config.version}" />' />
	<meta charset='utf-8'>
	<meta http-equiv='X-UA-Compatible' content='IE=edge'>
	<meta name='viewport' content='width=device-width, initial-scale=1'>
	<title>myordy pos admin</title>

    <script src='/myordy-resources/resources/bootstrap/jquery-2.1.3.min.js?v=<c:out value="${config.version}" />'></script>
    <script src='/myordy-resources/resources/bootstrap/js/bootstrap.min.js?v=<c:out value="${config.version}" />'></script>

    <link href='/myordy-resources/resources/bootstrap/css/bootstrap.min.css?v=<c:out value="${config.version}" />' rel='stylesheet'>
    <link href='/myordy-resources/resources/css/main.css?v=<c:out value="${config.version}" />' rel='stylesheet'>

    <link href='/myordy-resources/resources/angular-google-places-autocomplete/dist/autocomplete.min.css?v=<c:out value="${config.version}" />' rel='stylesheet'>
    <link href='/myordy-resources/resources/intl-tel-input/build/css/intlTelInput.css' rel='stylesheet'>
    <link href='/myordy-resources/resources/angular-block-ui/angular-block-ui.min.css' rel='stylesheet'>


    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCqIR_HlqpC1QCP3FvFZ0-2bCHwUZ7Ul-M&signed_in=true&libraries=places"></script>
    <script src="/myordy-resources/resources/intl-tel-input/build/js/intlTelInput.js"></script>
    <script src="https://js.stripe.com/v3/"></script>

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
	<script src='<c:url value="/resources/myordy-admin/posadmin-controller.js"/>?v=<c:out value="${config.version}" />'></script>
	<script type="text/javascript">
		var PAGE_LOAD_SERVER_TIME = <%= System.currentTimeMillis() %>;
		var CONTEXT_PATH = '${pageContext.request.contextPath}';
		var SHOP_ID = '${shopId}';
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
					<h4 style='margin:5px 0px 2px 0px;'><img ng-src='${pageContext.request.contextPath}/shopImage/${shopId}/[LOGO-MAIN]' alt='{{posAdminUI.shopLanguageTable[posAdminUI.util.message.getShopNameCode(posUI.shop)]}}' title='{{posAdminUI.shopLanguageTable[posAdminUI.util.message.getShopNameCode(posAdminUI.shop)]}}' /></h4>
				</div>
				<div class="col-xs-4 text-right">
					<div ng-show='posAdminUI.loginUI.loginResponse'>
						<div style='color:#ffffff;'>{{posAdminUI.loginUI.loginResponse.user.contact.name}}</div>
						<div style='margin-top:-1px;'><a style='color:#cccccc;' ng-click="posAdminUI.loginUI.logout(posAdminUI)">Logout</a></div>
					</div>
				</div>
			</div>
		</div>


		<div id="page-body" style='min-height:400px;'>
			<span us-spinner spinner-key="spinner-main"></span>
			<div id='body-header'></div>
			<div>
				<div class="container" style="background:#fff;padding-top:10px;">
					<%@ include file="/WEB-INF/views/FRAGMENTS/posAdminUI.jspf" %>

					<div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 voffset2" ng-show='!posAdminUI.loginUI.loginResponse'>
						<form name='loginFormEmail' ng-submit='posAdminUI.loginUI.submitLoginFormEmail(posAdminUI);'>
							<h4>Admin Login</h4>
						    <div class="form-group" ng-class="{'has-error':loginForm.loginFormEmail.$invalid}" style='margin-top:20px;'>
						        <label for="loginFormEmail" class="control-label">Email</label>
						        <input id='loginFormEmail' type="email" name='loginFormEmail' class="form-control" ng-model="posAdminUI.loginUI.loginFormEmail.email" required />
						    </div>
						    <div class="form-group" ng-class="{'has-error':loginForm.password.$invalid}">
						        <label for="loginFormPassword" class="control-label">Password</label>
						        <input id='loginFormPassword' type="password" name='loginFormPassword' class="form-control" ng-model="posAdminUI.loginUI.loginFormEmail.password" required />
						    </div>
							<div class='row'>
								<div class="col-xs-6">
								</div>
								<div class="col-xs-6 text-right">
									<button ng-disabled='loginFormEmail.$invalid' type='submit' class="btn btn-primary" style='margin-right:10px;'>Login</button>
								</div>
							</div>
							<div class="row" style='margin:15px 0px;'>
								<div class="col-xs-12">
									<div class="alert alert-danger" role="alert" ng-repeat='errorCode in posAdminUI.loginUI.errorMessageList'>
									  <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
									  <span class="sr-only">Error:</span> {{errorCode}}
									</div>
								</div>
							</div>
						</form>
					</div>

				</div>
			</div>
		</div>


		<div id='page-footer'>
			<div class="modal-footer" style="background: #efefef;color: #333;border-top:1px solid #aaa;">
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
			</div>
		</div>

	</div>

</body>
</html>