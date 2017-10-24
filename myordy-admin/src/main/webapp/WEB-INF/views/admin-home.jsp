<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html ng-app='MYORDY_ADMIN_APP' ng-controller='MYORDY_ADMIN_APP_CTRL'>
<head>
	<link rel='shortcut icon' type='image/x-icon' href='/myordy-resources/resources/myordy/layout-default/images/favicon.ico?v=<c:out value="${config.version}" />' />
	<meta charset='utf-8'>
	<meta http-equiv='X-UA-Compatible' content='IE=edge'>
	<meta name='viewport' content='width=device-width, initial-scale=1'>
	<title>myordy admin</title>
    <link href='/myordy-resources/resources/bootstrap/css/bootstrap.min.css?v=<c:out value="${config.version}" />' rel='stylesheet'>
<%--	<script src='/myordy-resources/resources/angular-1.3.7/angular.min.js?v=<c:out value="${config.version}" />'></script> --%>
	<script src='/myordy-resources/resources/angular-1.4.3/angular.min.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/angular-1.4.3/angular-cookies.min.js?v=<c:out value="${config.version}" />'></script>

	<script src='/myordy-resources/resources/spinner/spin.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/spinner/angular-spinner.js?v=<c:out value="${config.version}" />'></script>

	<script src='/myordy-resources/resources/myordy/user-locale.js?v=<c:out value="${config.version}" />'></script>

	<script src='/myordy-resources/resources/bootstrap/ui-bootstrap-tpls-0.12.0.min.js?v=<c:out value="${config.version}" />'></script>
	<script src='/myordy-resources/resources/myordy/myordy-common.js?v=<c:out value="${config.version}" />'></script>
	<script src='<c:url value="/resources/myordy-admin/admin-controller.js"/>?v=<c:out value="${config.version}" />'></script>
	<script type="text/javascript">
		var PAGE_LOAD_SERVER_TIME = <%= System.currentTimeMillis() %>;
		var CONTEXT_PATH = '${pageContext.request.contextPath}';
	</script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src='<c:url value="/resources/bootstrap/html5shiv/3.7.2/html5shiv.min.js"/>'></script>
      <script src='<c:url value="/resources/bootstrap/respond/1.4.2/respond.min.js"/>'></script>
    <![endif]-->
</head>
<body>
	<div id='layout-default'>

		<div style="background:#fff">
			<div class="container" style='padding:5px 0px;'>
				<div class=" col-lg-3 col-md-3 col-sm-3 col-xs-4"><img src="/myordy-resources/resources/myordy/layout-default/images/logo.png" style="width:80px;"></div>
				<div class="col-lg-2 pull-right text-right"></div>
			</div>
		</div>
		<div id="page-header">
			<div style="background:#000;">
				<div class="container" style='padding:10px 0px'>
					<div class="col-lg-12">
						<div class="col-xs-4" style="color:#fff;padding-left:5px;">
							<h3 style='margin:0px;padding-top:5px;'>Admin</h3>
						</div>
						<div class="col-xs-6 text-right voffsetcenter4 pull-right">
							<button ng-click='shopUI.showShopList()' class="btn btn-warning btn-md " type="button"><span class="glyphicon glyphicon-list-alt"></span>&nbsp;Manage Shops</button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="page-body" style='min-height:400px;'>
			<span us-spinner spinner-key="spinner-main"></span>
			<div id='body-header'></div>
			<div>
				<div class="container" style="background:#fff;padding-top:10px;">
					<%@ include file="/WEB-INF/views/FRAGMENTS/shop.jspf" %>
				</div>
			</div>
		</div>

		<div id='page-footer'>
			<div class="modal-footer" style="background: #000;color: #fff;">
				<div class="col-sm-7 pull-right">
					<ul class="list-inline">
						<li><a ng-click="shopUI.util.nav.openMyOrdyPopupContent('/myordy-resources/terms-of-service')">Terms & Conditions</a></li>
						<li><a ng-click="shopUI.util.nav.openMyOrdyPopupContent('/myordy-resources/privacy-policy')">Privacy Policy</a></li>
						<li>&copy;{{pageLoadServerTime | date:'yyyy'}}</li>
					</ul>
				</div>
			</div>
		</div>

	</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src='/myordy-resources/resources/bootstrap/jquery-2.1.3.min.js?v=<c:out value="${config.version}" />'></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src='/myordy-resources/resources/bootstrap/js/bootstrap.min.js?v=<c:out value="${config.version}" />'></script>
</body>
</html>