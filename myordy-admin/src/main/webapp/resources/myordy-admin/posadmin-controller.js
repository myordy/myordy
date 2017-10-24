var MYORDY_POS_ADMIN_APP = makeMyOrdyAngularApp('MYORDY_POS_ADMIN_APP');

angular.module('MYORDY_POS_ADMIN_APP').factory('posAdminService', ['$http', function($http) {
	return {
		contextPath:CONTEXT_PATH,
		urlBase : '/posadmin-rest',
		login : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/login', dto, config);
		},
		restoreUserSession : function (config) {
		    return $http.get(this.contextPath + this.urlBase + '/restoreUserSession', config);
		},
		salesSummaryReport : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/salesSummaryReport', dto, config);
		},
		getShop : function (id, config) {
		    return $http.get(this.contextPath + this.urlBase + '/shop/' + id, config);
		}
	};
}]);



MYORDY_POS_ADMIN_APP.factory('loginUI', [function() {
	var result = {
		loginResponse : null,
		loginFormEmail : {email:null,password:null},
		container : {showLogin:false},
		errorMessageList : null,
		setupUser : function (posAdminUI) {
			var pltCookie = posAdminUI.cookies.get('plt');
			if (pltCookie) {
				this.errorMessageList = [];
				this.loginResponse = null;
				posAdminUI.posAdminService.restoreUserSession({posAdminUI:posAdminUI})
		    	.success(function (data, status, headers, config) {
		    		if (data) {
		    			posAdminUI.loginUI.loginResponse = data;
		    			posAdminUI.onLoginResponseChange(data);
		    		} else {
		    			posAdminUI.loginUI.errorMessageList.push('Session Restore Failed');
		    		}
		    	})
		    	.error(function (error) {alert('[restoreUserSession] Request Failed: ' + error.message);});
			}
    	},
		logout : function (posAdminUI) {
			if (confirm('Are you sure you wish to logout?')) {
				posAdminUI.cookies.remove('plt');
				posAdminUI.loginUI.loginResponse = null;
				posAdminUI.onLoginResponseChange(null);
			}
		},
		submitLoginFormEmail : function (posAdminUI) {
			this.errorMessageList = [];
			this.loginResponse = null;
			posAdminUI.posAdminService.login(
    			this.loginFormEmail, {posAdminUI:posAdminUI}
    		)
	    	.success(function (data, status, headers, config) {
	    		if (data) {
	    			posAdminUI.loginUI.loginResponse = data;
	    			posAdminUI.onLoginResponseChange(data);
	    			var now = new Date();
	    		    var exp = new Date(now.getFullYear()+1, now.getMonth(), now.getDate());
	    		    posAdminUI.cookies.put('plt', posAdminUI.loginUI.loginResponse.sessionId, {
		    		  expires: exp
		    		});
	    		    posAdminUI.loginUI.loginFormEmail.email = null;
	    		    posAdminUI.loginUI.loginFormEmail.password = null;
	    		} else {
	    			posAdminUI.loginUI.errorMessageList.push('Login Failed');
	    		}
	    	})
	    	.error(function (error) {alert('[login] Request Failed: ' + error.message);});
		}
	};
	return result;
}]);



MYORDY_POS_ADMIN_APP.factory('reportingUI', ['posAdminService',
function(utilArg, posAdminServiceArg) {
    return {
    	posAdminService : posAdminServiceArg,
		containers : {showSalesSummaryReport:false},
		userHasAccess : false,
		onLoginResponseChange : function (posAdminUI, loginResponse) {
    		this.userHasAccess = posAdminUI.util.security.isInStaffRoleList(loginResponse, "SHOP_MANAGER");
    	},
		salesSummaryReportForm : {
			request:null,
			response:null,
			resetForm : function (salesSummaryReportForm) {
				salesSummaryReportForm.response = null;
				salesSummaryReportForm.request = {
					orderDates : {startDate : new Date(), endDate : new Date()}
				};
			},
			submit : function (posAdminUI) {
				posAdminUI.posAdminService.salesSummaryReport(
        			this.request, {posAdminUI:posAdminUI}
        		)
    	    	.success(function (data, status, headers, config) {
    	    		posAdminUI.reportingUI.salesSummaryReportForm.response = data;
    	    	})
    	    	.error(function (error) {alert('[salesSummaryReport] Request Failed: ' + error.message);});
			}
		},
    	onSalesSummaryReportSelect : function (posAdminUI) {
    		posAdminUI.onReportsSelect(posAdminUI);
    		posAdminUI.util.nav.hideAllContainers(this.containers);
    		this.salesSummaryReportForm.resetForm(this.salesSummaryReportForm);
    		this.containers.showSalesSummaryReport = true;
    	}
    };
}]);


MYORDY_POS_ADMIN_APP.factory('posAdminUI', ['util', 'posAdminService', 'reportingUI', 'loginUI',
function(utilArg, posAdminServiceArg, reportingUIArg, loginUIArg) {
    return {
    	posAdminService : posAdminServiceArg,
    	reportingUI : reportingUIArg,
    	loginUI : loginUIArg,
		containers : {showReportingUI:false},
    	shop : null,
    	shopLanguageTable : null,
     	util : utilArg,
    	setupUser : function () {
			this.loginUI.setupUser(this);
    	},
    	onLoginResponseChange : function (loginResponse) {
    		this.reportingUI.onLoginResponseChange(this, loginResponse);
    	},
    	onReportsSelect : function (posAdminUI) {
    		posAdminUI.util.nav.hideAllContainers(posAdminUI.containers);
    		posAdminUI.containers.showReportingUI = true;
    	},
    	openShop : function (shopId) {
			var posAdminUI = this;
    		this.posAdminService.getShop(shopId, null)
    		.success(function (data, status, headers, config) {
    			posAdminUI.shop = data;
    			posAdminUI.shopLanguageTable = posAdminUI.shop.languageTables[0].languageTable;
    		})
    		.error(function (error) {alert('Request Failed: ' + error.message);});
    	}
    };
}]);




MYORDY_POS_ADMIN_APP.controller('MYORDY_POS_ADMIN_APP_CTRL', ['$scope', '$modal', '$window', '$cookies', '$timeout', 'posAdminUI',
function ($scope, $modal, $window, $cookies, $timeout, posAdminUI) {
	$scope.sys = SYS;
	$scope.pageLoadServerTime = PAGE_LOAD_SERVER_TIME;
	$scope.contextPath = CONTEXT_PATH;
	$scope.posAdminUI = posAdminUI;
	posAdminUI.cookies = $cookies;
	posAdminUI.getScope = function() {
		return $scope;
	}
	posAdminUI.getTimeout = function() {
		return $timeout;
	}
	$scope.posAdminUI.openShop(SHOP_ID);
	$scope.posAdminUI.setupUser();
}
]);
