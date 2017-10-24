var MYORDY_POS_APP = makeMyOrdyAngularApp('MYORDY_POS_APP');

var ModalInstanceCtrl = MYORDY_POS_APP.controller('POSMenuItemModalInstanceCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		if ($scope.posUI.posMenuItemUI.addOrderItemToCart) {
			$scope.posUI.cartUI.addToCart($scope.posUI);
		}
		$scope.posUI.posMenuItemUI.showHome($scope.posUI);
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$scope.posUI.posMenuItemUI.showHome($scope.posUI);
		$modalInstance.dismiss();
	};
});

var ModalInstanceCtrl = MYORDY_POS_APP.controller('POSOrderModalInstanceCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});

var POSMenuModalInstanceCtrl = MYORDY_POS_APP.controller('POSMenuModalInstanceCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});

var POSSuburbSelectModalInstanceCtrl = MYORDY_POS_APP.controller('POSSuburbSelectModalInstanceCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});

var POSCartTotalModelModalInstanceCtrl = MYORDY_POS_APP.controller('POSCartTotalModelModalInstanceCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});

var LoginModalCtrl = MYORDY_POS_APP.controller('LoginModalCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});

var OrderCustomerDetailsModalCtrl = MYORDY_POS_APP.controller('OrderCustomerDetailsModalCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});

var OrderPaymentModalCtrl = MYORDY_POS_APP.controller('OrderPaymentModalCtrl', function ($scope, $modalInstance, request) {
	$scope.posUI = request.posUI;
	$scope.ordyId = request.ordyId;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
});


MYORDY_POS_APP.factory('posService', ['$http', function($http) {
	return {
		contextPath:CONTEXT_PATH,
		urlBase : '/pos-rest',
		login : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/login', dto, config);
		},
		restoreUserSession : function (config) {
		    return $http.get(this.contextPath + this.urlBase + '/restoreUserSession', config);
		},
		getBusinessServiceSuburbList : function (config) {
			// TODO add business id param
		    return $http.get(this.contextPath + this.urlBase + '/businessServiceSuburbList', config);
		},
		getMenuServiceSuburbList : function (menuId, config) {
		    return $http.get(this.contextPath + this.urlBase + '/menuServiceSuburbList/' + menuId, config);
		},
		suburbSearch : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/suburbSearch', dto, config);
		},
		customerSearch : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/customerSearch', dto, config);
		},
		ordySearch : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/ordySearch', dto, config);
		},
		ordySearchDUS : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/ordySearchDUS', dto, config);
		},
		ordyInbox : function (config, lastSearchStartTime) {
		    return $http.get(this.contextPath + this.urlBase + '/ordyInbox/' + SHOP_ID + '/' + lastSearchStartTime, config);
		},
		confirmOrdy : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/confirmOrdy', dto, config);
		},
		getOrdyDUS : function (id, config) {
		    return $http.get(this.contextPath + this.urlBase + '/ordyDUS/' + id + '/' + SHOP_ID, config);
		},
		getOrdy : function (id, config) {
		    return $http.get(this.contextPath + this.urlBase + '/ordy/' + id + '/' + SHOP_ID, config);
		},
		getCustomerLastOrdy : function (customerId, config) {
		    return $http.get(this.contextPath + this.urlBase + '/customerLastOrdy/' + customerId + '/' + SHOP_ID, config);
		},
		customerDelete : function (id, config) {
		    return $http.get(this.contextPath + this.urlBase + '/customerDelete/' + id + '/' + SHOP_ID, config);
		},
		updateCustomer : function (dto, config) {
		    return $http.put(this.contextPath + this.urlBase + '/customerUpdate', dto, config)
		},
		customerCreate : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/customerCreate', dto, config);
		},
		submitOrdy : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/submitOrdy', dto, config);
		},
//		payOnlineEway : function (ordyId, config) {
//		    return $http.get(this.contextPath + this.urlBase + '/payOnlineEway/' + ordyId + '/' + SHOP_ID, config);
//		},
		getShop : function (id, caller) {
		    return $http.get(this.contextPath + this.urlBase + '/shop/' + id, {caller:caller});
		},
		getReferenceData : function (config) {
		    return $http.get(this.contextPath + this.urlBase + '/referenceData', config)
		}
	};
}]);


MYORDY_POS_APP.factory('ordyInboxUI', [function() {
	var result = {
		container : {showInbox:true},
		inboxMonitorInterval : undefined,
		inboxOrders : {items:[]},
		lastSearchStartTime : 0,
		inboxOrdersRefreshCounter : 0,
		glowNewOrderReceived:false,
		openInboxOrder : function (posUI, order) {
			posUI.posOrderSearchUI.showSingleOrderDetails(posUI, order);
		},
		getInboxItems : function (posUI) {
			posUI.ordyInboxUI.inboxOrdersRefreshCounter++;
			if (posUI.ordyInboxUI.inboxOrdersRefreshCounter > 10) {
				// this will make sure the list is refreshed
		    	posUI.ordyInboxUI.lastSearchStartTime = 0;
		    	posUI.ordyInboxUI.inboxOrders.items = [];
			}
			posUI.posService.ordyInbox({posUI:posUI}, posUI.ordyInboxUI.lastSearchStartTime)
	    	.success(function (data, status, headers, config) {
	    		if (data.items && data.items.length > 0) {
	    			posUI.ordyInboxUI.inboxOrdersRefreshCounter = 0;
	    			posUI.ordyInboxUI.inboxOrders.items = data.items.concat(posUI.ordyInboxUI.inboxOrders.items);
	    			document.getElementById('ordyInboxUI_NEW_ORDY_ALERT').play();
	    			posUI.ordyInboxUI.glowNewOrderReceived = true;
	    		}
	    		posUI.ordyInboxUI.lastSearchStartTime = data.searchStartTime;
	    	})
	    	.error(function (error) {
	    			var errorMessage = '';
	    			if (error) {
	    				errorMessage = error.message;
	    			}
    				alert('[getInboxItems] Request Failed: ' + errorMessage);
	    		}
	    	);
		},
		stopInboxMonitor : function (posUI) {
			if (angular.isDefined(posUI.ordyInboxUI.inboxMonitorInterval)) {
				posUI.getInterval().cancel(posUI.ordyInboxUI.inboxMonitorInterval);
				posUI.ordyInboxUI.inboxMonitorInterval = undefined;
			}
		},
		startInboxMonitor : function (posUI) {
			if (!angular.isDefined(posUI.ordyInboxUI.inboxMonitorInterval)) {
				posUI.ordyInboxUI.inboxMonitorInterval = posUI.getInterval()(function() {posUI.ordyInboxUI.getInboxItems(posUI)}, 5000);
				posUI.getScope().$on("ORDER_CONFIRMED" ,function(event,posUI) {
					// this will make sure the confirmed order is removed and the list is refreshed
					posUI.ordyInboxUI.inboxOrdersRefreshCounter = 100;
					posUI.ordyInboxUI.glowNewOrderReceived = false;
				});
			}
		}
	};
	return result;
}]);

MYORDY_POS_APP.factory('previousOrdersUI', [function() {
	var result = {
		container : {showSearch:true},
		previousOrders : null,
		showCallShopErrorMessage : false,
		continueAutoRefresh : false,
		showMyOrderLogTab : false,
		monitorInterval : null,
    	onLoginResponseChange : function (posUI, loginResponse) {
    		//this.userHasAccess = posUI.util.security.isInStaffRoleList(loginResponse, "SHOP_POS_OPERATOR");
		},
		initMonitorStatus : function (posUI) {
			var timeLapseMilMin = Number.MAX_SAFE_INTEGER;
			var timeLapseMilMax = 0;
			var now = new Date();
			if (null == posUI.previousOrdersUI.previousOrders) {
				posUI.previousOrdersUI.continueAutoRefresh = true; // previousOrders will be null after page load
				posUI.previousOrdersUI.showCallShopErrorMessage = false;
			} else {
				for (var i = 0; i < posUI.previousOrdersUI.previousOrders.items.length; i++) {
					if ('NEWORDY' == posUI.previousOrdersUI.previousOrders.items[i].ordyStatus) {
						var timeLapseMil = (now - posUI.previousOrdersUI.previousOrders.items[i].orderTime);
						if (timeLapseMil < timeLapseMilMin) {
							timeLapseMilMin = timeLapseMil;
						}
						if (timeLapseMil > timeLapseMilMax) {
							timeLapseMilMax = timeLapseMil;
						}
					}
				}
				posUI.previousOrdersUI.continueAutoRefresh = (timeLapseMilMin < (1000 * 60 * 10)); // if there is a new order submitted less than 10 mins back
				posUI.previousOrdersUI.showCallShopErrorMessage = (timeLapseMilMax > (1000 * 60 * 10)); // if there is a order waiting for confirmation longer than 10 mins
			}
		},
		openOrdersDUS : function (posUI, order) {
			posUI.posOrderSearchUI.showSingleOrderDetailsDUS(posUI, order);
		},
		getPreviousOrdersDUS : function (posUI) {
			var monitorStatus = posUI.previousOrdersUI.initMonitorStatus(posUI);
			if (posUI.previousOrdersUI.continueAutoRefresh) {
				posUI.posService.ordySearchDUS(
					{currentPageNumber:1,maxResults:10,shopId:SHOP_ID}, {posUI:posUI}
				)
				.success(function (data, status, headers, config) {
					posUI.previousOrdersUI.previousOrders = data;
					posUI.cartUI.setDiscountPercent(posUI, posUI.cartUI.cart);
				})
				.error(function (error) {alert('[ordySearchDUS] Request Failed: ' + error.message);});
			} else {
				if (posUI.previousOrdersUI.monitorInterval) {
					posUI.getInterval().cancel(posUI.previousOrdersUI.monitorInterval);
					posUI.previousOrdersUI.monitorInterval = undefined;
				}
			}
		},
		startPreviousOrdersMonitor : function (posUI) {
			if (!posUI.previousOrdersUI.monitorInterval) {
				posUI.previousOrdersUI.monitorInterval = posUI.getInterval()(function() {posUI.previousOrdersUI.getPreviousOrdersDUS(posUI);}, 5000);
			} 
    	}
	};
	return result;
}]);


MYORDY_POS_APP.factory('loginUI', ['$modal', function($modal) {
	var result = {
		loginResponse : null,
		modalInstance : null,
		loginFormEmail : {email:null,password:null},
		container : {showLogin:false,showLogout:false},
		errorMessageList : null,
		setupUser : function (posUI) {
			var pltCookie = posUI.cookies.get('plt');
			var dusCookie = posUI.cookies.get('dus');
			if (pltCookie) {
				this.errorMessageList = [];
				this.loginResponse = null;
				posUI.posService.restoreUserSession({posUI:posUI})
		    	.success(function (data, status, headers, config) {
		    		if (data) {
		    			posUI.loginUI.loginResponse = data;
		    			posUI.onLoginResponseChange(data);
		    		} else {
		    			posUI.loginUI.errorMessageList.push('Session Restore Failed');
		    		}
		    	})
		    	.error(function (error) {alert('[restoreUserSession] Request Failed: ' + error.message);});
			} else if (dusCookie) {
				posUI.previousOrdersUI.startPreviousOrdersMonitor(posUI);
			}
    	},
		logout : function (posUI) {
			posUI.cookies.remove('plt');
			posUI.loginUI.loginResponse = null;
			posUI.onLoginResponseChange(null);
			posUI.loginUI.modalInstance.close();
		},
		submitLoginFormEmail : function (posUI) {
			this.errorMessageList = [];
			this.loginResponse = null;
			posUI.posService.login(
    			this.loginFormEmail, {posUI:posUI}
    		)
	    	.success(function (data, status, headers, config) {
	    		if (data) {
	    			posUI.loginUI.loginResponse = data;
	    			posUI.onLoginResponseChange(data);
	    			var now = new Date();
	    		    var exp = new Date(now.getFullYear()+1, now.getMonth(), now.getDate());
	    			posUI.cookies.put('plt', posUI.loginUI.loginResponse.sessionId, {
		    		  expires: exp
		    		});
	    			posUI.loginUI.modalInstance.close();
	    			posUI.loginUI.loginFormEmail.email = null;
	    			posUI.loginUI.loginFormEmail.password = null;
	    		} else {
	    			posUI.loginUI.errorMessageList.push('Login Failed');
	    		}
	    	})
	    	.error(function (error) {alert('[login] Request Failed: ' + error.message);});
		},
		showLogout : function (posUI) {
			posUI.util.nav.hideAllContainers(this.container);
			posUI.loginUI.container.showLogout = true;
			this.openModalInstance(posUI);
		},
		show : function (posUI) {
			posUI.util.nav.hideAllContainers(this.container);
			posUI.loginUI.container.showLogin = true;
			this.openModalInstance(posUI);
		},
		openModalInstance : function (posUI) {
	    	this.modalInstance = $modal.open({
				templateUrl: 'loginUI.jspf',
				controller: 'LoginModalCtrl',
				resolve: {
					request : function () {
						return {
							posUI : posUI
						};
					}
				}
			});
			this.modalInstance.result.then(function () {
			}, function () {
			});
	    }
	};
	return result;
}]);

MYORDY_POS_APP.factory('posOrderSearchUI', ['posService', '$modal', function(posServiceArg, $modal) {
	var result = {
		posService : posServiceArg,
		container : {showDetail:false,showSearch:true},
		userHasAccess : false,
		singleOrderView : false,
		order : null,
		orderIndex : null,
		searchRequest : null,
		searchResult : null,
		modalInstance : null,
		view : {title:'Order Search'},
		confirmOrdy : function (posUI, order) {
			if (confirm('Are you sure you wish to confirm this order?')) {
				posUI.posService.confirmOrdy(
						{shopId:SHOP_ID,ordyId:order.ordyId}, {posUI:posUI}
				)
				.success(function (data, status, headers, config) {
					posUI.getScope().$broadcast("ORDER_CONFIRMED", posUI);
					posUI.getTimeout()(function (){
						posUI.posOrderSearchUI.printOrder(posUI);
						posUI.posOrderSearchUI.order = data;
					}, 1000);
				})
				.error(function (error) {alert('[confirmOrdy] Request Failed: ' + error.message);});
			}
    	},
/*
    	xxx : function (posUI) {
    		// TODO
//    		posUI.posCustomerSearchUI.showCreateCustomer(posUI);
//    		posUI.posCustomerSearchUI.customer.contact.name = "Chuchu";
    		posUI.posCustomerSearchUI.reset(posUI);

    		posUI.posCustomerSearchUI.searchRequest.name = posUI.posOrderSearchUI.order.customerName;
    		posUI.posCustomerSearchUI.searchRequest.phone = posUI.posOrderSearchUI.order.customerMobileNumber;
    		posUI.posCustomerSearchUI.searchRequest.address = posUI.posOrderSearchUI.order.deliveryAddress;

    		posUI.posCustomerSearchUI.search(posUI);

//			posUI.posOrderSearchUI.order.customerName
//			posUI.posOrderSearchUI.order.deliveryAddress
//			posUI.posOrderSearchUI.order.customerMobileNumber

    		posUI.util.nav.hideAllContainers(posUI.tabsetTabsContainer);
    		posUI.tabsetTabsContainer.showCustomer = true;

			this.modalInstance.close();
    	},
*/
    	repeatOrder : function (posUI, order) {
    		var result = true;
    		var menu = null;
    		if (order.menuId) {
    			menu = posUI.util.menu.findMenuById(posUI.shop.menuList, order.menuId);
    		}
    		if (menu) {
    			posUI.posMenuUI.onMenuSelect(posUI, menu);
    			posUI.cartUI.cart.note = order.note;    			

    			if (order.customer && order.customer.customerId) {
					posUI.posCustomerSearchUI.resetSearch();
					posUI.posCustomerSearchUI.searchResult = {items:[order.customer],totalItems:1};
					posUI.posCustomerSearchUI.showCustomerDetails(posUI, order.customer, 0);
				}
				result = this.copyOrdyItemListToCart(posUI, order.ordyItemList, 1);
				if (result && order.comboOrdyItemList) {
					result = this.copyComboOrdyItemListToCart(posUI, order.comboOrdyItemList);
				}
    		} else {
    			result = false;
    		}
    		if (result) {
        		this.modalInstance.close();
    			posUI.posMenuItemUI.showHome(posUI);

    			posUI.util.nav.hideAllContainers(posUI.tabsetTabsContainer);
    			posUI.tabsetTabsContainer.showOrder = true;    			
    		} else {
    			alert("Sorry, this order can't be repeated because some items have changed or not available.");
    		}
    	},
    	copyComboOrdyItemListToCart : function (posUI, comboOrdyItemList) {
    		var result = true;
			for (var i = 0; i < comboOrdyItemList.length; i++) {
				var comboMenuItem = posUI.util.menuItem.findComboMenuItem(posUI.posMenuUI.menuItemMap, comboOrdyItemList[i].menuItemComboId);
				if (!comboMenuItem) {
					result = false;
					break;
				}
				if (posUI.util.tag.isTagInListByCode(comboMenuItem.tagList, '[-SPECIAL-]')) {
    				this.copyOrdyItemListToCart(posUI, comboOrdyItemList[i].orderItems, comboOrdyItemList[i].qty);
    			} else {
					posUI.posMenuItemUI.posMenuComboItemUI.setupNewComboOrderItem(posUI, comboMenuItem, comboOrdyItemList[i].qty);
					if (comboOrdyItemList[i].orderItems) {
						for (var j = 0; j < comboOrdyItemList[i].orderItems.length; j++) {
							var menuItem = posUI.util.menuItem.findMenuItem(posUI.posMenuUI.menuItemMap, comboOrdyItemList[i].orderItems[j].menuItemId);
							if (!menuItem) {
								result = false;
								break;
							}
							comboOrdyItemList[i].orderItems[j].menuItem = menuItem;
							var orderItemTemp = {orderItem:posUI.cartUI.makeNewOrderItem(posUI, menuItem, null),qty:comboOrdyItemList[i].orderItems[j].qty};
							if (comboOrdyItemList[i].orderItems[j].ordyItemsComboSub) {
								// TODO
								result = false;
								break;
							}
							this.repeatExtraOptions(posUI, comboOrdyItemList[i].orderItems[j], orderItemTemp.orderItem);
							posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.orderItems.push(orderItemTemp);
							posUI.cartUI.setComboOrderItemAmount(posUI, posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem);
						}
					}
					var comboOrderItemValidationResult = posUI.posMenuItemUI.posMenuComboItemUI.validateComboOrderItem(
							posUI, posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem);
					if (comboOrderItemValidationResult.valid) {
						posUI.cartUI.addComboItemToCart(posUI);
					}
    			}
			}
			return result;
    	},
    	copyOrdyItemListToCart : function (posUI, ordyItemList, comboQtyMultiplier) {
    		var result = true;
    		if (ordyItemList) {
    			for (var i = 0; i < ordyItemList.length; i++) {
    				if (!this.copyOrdyItemToCart(posUI, ordyItemList[i], comboQtyMultiplier)) {
    					result = false;
    					break;
    				}
    			}
    		}
    		return result;
    	},
    	copyOrdyItemToCart : function (posUI, ordyItem, comboQtyMultiplier) {
    		var result = true;
			var menuItem = posUI.util.menuItem.findMenuItem(posUI.posMenuUI.menuItemMap, ordyItem.menuItemId);
			var parentMenuItem = null;
			if (menuItem) {
				posUI.posMenuItemUI.orderItem = posUI.cartUI.makeNewOrderItem(posUI, menuItem, parentMenuItem);
				posUI.posMenuItemUI.orderItem.qty = ordyItem.qty * comboQtyMultiplier;
				posUI.posMenuItemUI.onOrderItemMenuItemChange(posUI, menuItem);
				posUI.posMenuItemUI.orderItem.note = ordyItem.note;

				if (ordyItem.ordyItemsComboSub) {
					// TODO
					result = false;
				}

				if (ordyItem.extraOptions) {
					if (ordyItem.extraOptions.addOptions) {
						for (var j = 0; j < ordyItem.extraOptions.addOptions.length; j++) {
							var optionItem = posUI.util.orderItemOption.findExtraOptionItem(
    							posUI.posMenuItemUI.posOrderItemOptionsUI.options.extraItems.addOptions.extraOptionItemList,
    							ordyItem.extraOptions.addOptions[j].option.extraOptionItemId
    						);
							if (optionItem) {
								posUI.posMenuItemUI.posOrderItemOptionsUI.orderOptionAdd(posUI, optionItem);
							} else {
								result = false;
								break;
							}
						}
					}
					if (result && ordyItem.extraOptions.removeOptions) {
						for (var j = 0; j < ordyItem.extraOptions.removeOptions.length; j++) {
							var optionItem = posUI.util.orderItemOption.findExtraOptionItem(
								posUI.posMenuItemUI.posOrderItemOptionsUI.options.extraItems.addOptions.extraOptionItemList,
								ordyItem.extraOptions.removeOptions[j].option.extraOptionItemId
							);
							if (optionItem) {
								posUI.posMenuItemUI.posOrderItemOptionsUI.orderOptionRemove(posUI, optionItem);
							} else {
								result = false;
								break;
							}
						}
					}
				}
				if (result) {
					posUI.cartUI.addToCart(posUI);
				}
			} else {
				result = false;
			}
			return result;
    	},
    	repeatExtraOptions : function (posUI, ordyItem, newOrderItem) {
			posUI.posMenuItemUI.posOrderItemOptionsUI.initAddOptions(posUI, ordyItem.menuItem);
			if (ordyItem.extraOptions) {
				if (ordyItem.extraOptions.addOptions) {
					for (var j = 0; j < ordyItem.extraOptions.addOptions.length; j++) {
						var optionItem = posUI.util.orderItemOption.findExtraOptionItem(
							posUI.posMenuItemUI.posOrderItemOptionsUI.options.extraItems.addOptions.extraOptionItemList,
							ordyItem.extraOptions.addOptions[j].option.extraOptionItemId
						);
						if (optionItem) {
							newOrderItem.extraOptions.addOptions.push({option:optionItem,qty:ordyItem.extraOptions.addOptions[j].qty,amount:0});
						} else {
							result = false;
							break;
						}
					}
				}
				if (result && ordyItem.extraOptions.removeOptions) {
					for (var j = 0; j < ordyItem.extraOptions.removeOptions.length; j++) {
						var optionItem = posUI.util.orderItemOption.findExtraOptionItem(
							posUI.posMenuItemUI.posOrderItemOptionsUI.options.extraItems.addOptions.extraOptionItemList,
							ordyItem.extraOptions.removeOptions[j].option.extraOptionItemId
						);
						if (optionItem) {
							newOrderItem.extraOptions.removeOptions.push({option:optionItem,qty:ordyItem.extraOptions.removeOptions[j].qty,amount:0,price:optionItem.price});
						} else {
							result = false;
							break;
						}
					}
				}
			}
    	},
    	showCustomerOrderLog : function (posUI, customer) {
			if (null != customer && null != customer.customerNumber) {
				this.show(posUI);
				this.resetSearch();
				this.searchRequest.orderTimeFrom = new Date(0);
				this.searchRequest.orderByLatestOrderFirst = true;
				this.searchRequest.customerNumber = customer.customerNumber;
				this.search(posUI);
			}
    	},
		showSingleOrderDetails : function (posUI, order) {
			posUI.posOrderSearchUI._setupShowSingleOrderDetails(posUI, order);
			posUI.posOrderSearchUI.showOrderDetails(posUI, order.ordyId, 0);
    	},
		showSingleOrderDetailsDUS : function (posUI, order) {
			posUI.posOrderSearchUI._setupShowSingleOrderDetails(posUI, order);
			posUI.posOrderSearchUI.showOrderDetailsDUS(posUI, order.ordyId, 0);
    	},
		_setupShowSingleOrderDetails : function (posUI, order) {
			posUI.posOrderSearchUI.singleOrderView = true;
			posUI.posOrderSearchUI.show(posUI);
			posUI.posOrderSearchUI.searchResult.items = [order];
    	},
		onLoginResponseChange : function (posUI, loginResponse) {
    		this.userHasAccess = posUI.util.security.isInStaffRoleList(loginResponse, "SHOP_POS_OPERATOR");
    	},
		resetSearch : function () {
			this.searchRequest = {orderTimeFrom:new Date(),orderTimeTo:null,customerNumber:null,orderByLatestOrderFirst:null,currentPageNumber:1,maxResults:10,shopId:SHOP_ID};
			this.searchResult = {items:[],totalItems:0};
	    	this.backToSearch();
		},
		search : function (posUI) {
			posUI.posOrderSearchUI.singleOrderView = false;
    		this.posService.ordySearch(
    			this.searchRequest, {posUI:posUI}
    		)
	    	.success(function (data, status, headers, config) {
	    		config.posUI.posOrderSearchUI.searchResult = data;
	    	})
	    	.error(function (error) {alert('[ordySearch] Request Failed: ' + error.message);});
		},
		printOrder : function (posUI) {
			var cartOld = posUI.cartUI.cart;
			var menuOld = posUI.posMenuUI.menu;
			var customerOld = posUI.posCustomerSearchUI.customer;
			var menuDiscountCouponOld = posUI.cartUI.menuDiscountCoupon.resetAndGetOldState(posUI);

			if (posUI.shop && posUI.shop.menuList) {
    			for (var i = 0; i < posUI.shop.menuList.length; i++) {
    				if (posUI.shop.menuList[i].menuId == posUI.posOrderSearchUI.order.menuId) {
    			   		posUI.posMenuUI.onMenuSelect(posUI, posUI.shop.menuList[i]);
    			   		break;
    				}
    			}
    		}
    		if (posUI.posOrderSearchUI.order.comboOrdyItemList) {
				for (var i = 0; i < posUI.posOrderSearchUI.order.comboOrdyItemList.length; i++) {
					var ordyItemCombo = posUI.posOrderSearchUI.order.comboOrdyItemList[i];
					ordyItemCombo.comboMenuItem = posUI.util.menuItem.findComboMenuItem(posUI.posMenuUI.menuItemMap, ordyItemCombo.menuItemComboId);
					if (ordyItemCombo.orderItems) {
						for (var j = 0; j < ordyItemCombo.orderItems.length; j++) {
							var ordyItem = ordyItemCombo.orderItems[j];
							ordyItemCombo.orderItems[j].orderItem = ordyItem; 
							ordyItemCombo.orderItems[j].orderItem.menuItem = posUI.util.menuItem.findMenuItem(posUI.posMenuUI.menuItemMap, ordyItem.menuItemId);
						}
					}
					if (ordyItemCombo.ordyItemsComboSub) {
						for (var j = 0; j < ordyItemCombo.ordyItemsComboSub.length; j++) {
							ordyItemCombo.ordyItemsComboSub[j].orderItemComboSub = {
								comboMenuItem : posUI.util.menuItem.findComboMenuItem(posUI.posMenuUI.menuItemMap, ordyItemCombo.ordyItemsComboSub[j].menuItemComboId),
								orderItems : []
							};

							if (ordyItemCombo.ordyItemsComboSub[j].orderItems) {
								for (var k = 0; k < ordyItemCombo.ordyItemsComboSub[j].orderItems.length; k++) {
									var ordyItem = {orderItem:ordyItemCombo.ordyItemsComboSub[j].orderItems[k]};
									ordyItem.orderItem.menuItem = posUI.util.menuItem.findMenuItem(posUI.posMenuUI.menuItemMap, ordyItem.orderItem.menuItemId);
									ordyItemCombo.ordyItemsComboSub[j].orderItemComboSub.orderItems.push(ordyItem);
								}
							}
						}
					}
				}
    		}
			if (posUI.posOrderSearchUI.order.ordyItemList) {
				for (var i = 0; i < posUI.posOrderSearchUI.order.ordyItemList.length; i++) {
					var ordyItem = posUI.posOrderSearchUI.order.ordyItemList[i];
					ordyItem.menuItem = posUI.util.menuItem.findMenuItem(posUI.posMenuUI.menuItemMap, ordyItem.menuItemId);
				}
			}
			posUI.cartUI.cart = posUI.posOrderSearchUI.order;
			posUI.cartUI.menuDiscountCoupon.coupon = posUI.cartUI.cart.menuDiscountCoupon;
			posUI.cartUI.resetShowDiscountPercent(posUI);


			posUI.posCustomerSearchUI.customer = posUI.posOrderSearchUI.order.customer;
			var phoneList = (posUI.posCustomerSearchUI.customer) ? posUI.posCustomerSearchUI.customer.contact.phoneList : null;
			posUI.cartUI.customerPhoneNumbers = posUI.util.theUserPhone.getCustomerPhoneNumbers(phoneList, posUI.cartUI.cart.customerMobileNumber);

			if (null == posUI.posMenuUI.menu.menuOrdyConfig.printCopiesCount) {
        		posUI.posMenuUI.menu.menuOrdyConfig.printCopiesCount = 2;
        	}
			posUI.getTimeout()(function (){
				window.print();
				posUI.posMenuUI.onMenuSelect(posUI, menuOld);
				posUI.cartUI.cart = cartOld;
				posUI.posCustomerSearchUI.customer = customerOld;
				posUI.cartUI.resetShowDiscountPercent(posUI);
				posUI.cartUI.menuDiscountCoupon.resetAndGetOldState(posUI, menuDiscountCouponOld);
			}, 1000);
		},
		backToSearch : function (posUI) {
			this.view.title = 'Order Search';
			this.order = null;
			this.hideAllContainers();
			this.container.showSearch = true;
		},
		showOrderDetailsDUS : function (posUI, ordyId, index) {
        	this.posService.getOrdyDUS(ordyId, {posUI:posUI})
    		.success(function (data, status, headers, config) {
    			posUI.posOrderSearchUI._showOrderDetails(posUI, index, data);
    		})
    		.error(function (error) {alert('Request Failed: ' + error.message);});
		},
		showOrderDetails : function (posUI, ordyId, index) {
        	this.posService.getOrdy(ordyId, {posUI:posUI})
    		.success(function (data, status, headers, config) {
    			posUI.posOrderSearchUI._showOrderDetails(posUI, index, data);
    		})
    		.error(function (error) {alert('Request Failed: ' + error.message);});

/*
			this.view.title = 'Order Details';
			this.orderIndex = index;
        	this.posService.getOrdy(ordyId, {posUI:posUI})
    		.success(function (data, status, headers, config) {
           		var posUI = config.posUI;
           		posUI.posOrderSearchUI.hideAllContainers();
           		posUI.posOrderSearchUI.container.showDetail = true;
           		posUI.posOrderSearchUI.order = data;
    		})
    		.error(function (error) {alert('Request Failed: ' + error.message);});
*/
		},
		_showOrderDetails : function (posUI, index, order) {
			posUI.posOrderSearchUI.view.title = 'Order Details';
			posUI.posOrderSearchUI.orderIndex = index;
       		posUI.posOrderSearchUI.hideAllContainers();
       		posUI.posOrderSearchUI.container.showDetail = true;
       		posUI.posOrderSearchUI.order = order;
		},
	    hideAllContainers : function () {
			for(var key in this.container) {
			    this.container[key] = false;
			}
	    },
	    show : function (posUI) {
	    	this.resetSearch();
	    	this.modalInstance = $modal.open({
				templateUrl: 'posOrderSearchModel.jspf',
				controller: 'POSOrderModalInstanceCtrl',
				resolve: {
					request : function () {
						return {
							posUI : posUI
						};
					}
				}
			});
			this.modalInstance.result.then(function () {
			}, function () {
			});
	    }
	};
	return result;
}]);

MYORDY_POS_APP.factory('posCustomerSearchUI', ['posService', '$modal', function(posServiceArg, $modal) {
	var result = {
		userHasAccess : false,
		modalInstance : null,
		posService : posServiceArg,
		container : {showDetail:false,showSearch:true,addressDetail:false,phoneDetail:false},
		customer : null,
		address : null,
		phone : null,
		customerIndex : null,
		addressIndex : null,
		phoneIndex : null,
		searchRequest : null,
		searchResult : null,
		suburbOptions : null,
		customerAddressesWithinServicedSuburbs : null,

    	onLoginResponseChange : function (posUI, loginResponse) {
    		this.userHasAccess = posUI.util.security.isInStaffRoleList(loginResponse, "SHOP_POS_OPERATOR");
    	},
		reset : function (posUI) {
			posUI.posCustomerSearchUI.resetSearch();
			posUI.posCustomerSearchUI.backToSearch(posUI);
		},
		resetSearch : function () {
			this.searchRequest = {name:null,phone:null,address:null,customerNumber:null,currentPageNumber:1,maxResults:10,shopId:SHOP_ID};
			this.searchResult = {items:[],totalItems:0};
		},
		search : function (posUI) {
			if (posUI.util.tools.areAllObjectPropertiesBlank(this.searchRequest, ['address','customerNumber','name','phone'])) {
				alert('Please enter atleast one search criteria.');
			} else {
	    		this.posService.customerSearch(
        			this.searchRequest, {posUI:posUI}
        		)
    	    	.success(function (data, status, headers, config) {
    	    		config.posUI.posCustomerSearchUI.searchResult = data;
    	    	})
    	    	.error(function (error) {alert('[customerSearch] Request Failed: ' + error.message);});
			}
		},
		backToCustomerDetails : function () {
			this.hideAllContainers();
			this.container.showDetail = true;
		},
		showCustomerDetails : function (posUI, customer, index) {
			this.hideAllContainers();
			this.container.showDetail = true;
			this.customerIndex = index;
			this.customer = angular.copy(customer);
			posUI.posCustomerSearchUI.onCustomerUpdate(posUI);
		},
		openLastOrder : function (posUI) {
			this.posService.getCustomerLastOrdy(this.customer.customerId, {posUI:posUI})
    		.success(function (data, status, headers, config) {
    			if (null != data && '' != data) {
    				posUI.posOrderSearchUI.showSingleOrderDetails(posUI, data);
    			}
    		})
    		.error(function (error) {alert('Request Failed: ' + error.message);});
		},
		openOrderLog : function (posUI) {
			posUI.posOrderSearchUI.showCustomerOrderLog(posUI, this.customer);
		},
		onAddressSuburbSelect : function (posUI, suburb) {
			posUI.posCustomerSearchUI.address.suburb = suburb;
			posUI.posCustomerSearchUI.address.suburbId = suburb.suburbId;
		},
		onCustomerUpdate : function (posUI) {
			if (posUI.cartUI.cart) {
				posUI.cartUI.cart.customerName = null;
				posUI.cartUI.cart.deliveryAddress = null;
				posUI.cartUI.cart.deliverySuburb = null;
				posUI.cartUI.cart.customerMobileNumber = null;

				if (posUI.posCustomerSearchUI.customer && posUI.posMenuUI.menu) {
					posUI.posCustomerSearchUI.customerAddressesWithinServicedSuburbs = posUI.util.menu.findCustomerAddressesWithinServicedSuburbs(posUI.posMenuUI.menu, posUI.posCustomerSearchUI.customer);
					if (posUI.posCustomerSearchUI.customerAddressesWithinServicedSuburbs.length == 1) {
						posUI.posCustomerSearchUI.onCustomerAddressesWithinServicedSuburbsSelect(posUI, this.customerAddressesWithinServicedSuburbs[0]);
					}
					if (posUI.posCustomerSearchUI.customer.contact) {
						posUI.cartUI.cart.customerName = posUI.posCustomerSearchUI.customer.contact.name;
						posUI.cartUI.cart.customerMobileNumber = posUI.util.theUserPhone.findMobileNumber(posUI.posCustomerSearchUI.customer.contact.phoneList);
					}
				}
			}
		},
		onCustomerAddressesWithinServicedSuburbsSelect : function (posUI, address) {
			posUI.cartUI.cart.deliveryAddress = address.address;
			posUI.cartUI.cart.deliverySuburb = {suburbId:address.suburbId};
			posUI.cartUI.cart.deliveryCharge = 0;

			// TODO min order amount check "Minimum Order Amount Difference"
			if (posUI.posMenuUI.menu && posUI.posMenuUI.menu.menuOrdyConfig && posUI.posMenuUI.menu.menuOrdyConfig.shopSuburbConfigList) {
				for (var i = 0; i < posUI.posMenuUI.menu.menuOrdyConfig.shopSuburbConfigList.length; i++) {
					if (posUI.posMenuUI.menu.menuOrdyConfig.shopSuburbConfigList[i].suburb.suburbId == address.suburbId) {
						var suburbConfig = posUI.posMenuUI.menu.menuOrdyConfig.shopSuburbConfigList[i];
						//console.log(posUI.posMenuUI.menu.menuOrdyConfig.shopSuburbConfigList[i]);
						posUI.cartUI.cart.deliveryCharge = suburbConfig.deliveryPrice;
						//suburbConfig.minDeliveryMins;
						break;
					}
				}
			}
			// TODO reset cart
		},
		onCustomerAddressesWithinServicedSuburbsClear : function (posUI) {
			posUI.cartUI.cart.deliveryAddress = null;
			posUI.cartUI.cart.deliverySuburb = null;
		},
		showCreateCustomer : function (posUI) {
			this.hideAllContainers();
			this.container.showDetail = true;
			this.customer = {status:'ACTIVE',contact:{name:null,email:null,addressList:[],phoneList:[],languageId:null,status:'ACTIVE'}};
		},
		onNewCustomerClick : function (posUI) {
			posUI.posCustomerSearchUI.showCreateCustomer(posUI);
			posUI.posCustomerSearchUI.customer.contact.name = posUI.posCustomerSearchUI.searchRequest.name;
			if (posUI.posCustomerSearchUI.searchRequest.phone != '') {
				posUI.posCustomerSearchUI.customer.contact.phoneList.push({phoneNumber:posUI.posCustomerSearchUI.searchRequest.phone,phoneType:'OTHER', status:'ACTIVE'});
			}
		},
		deleteCustomer : function (posUI) {
			if (confirm('Are you sure you wish to delete this customer?')) {
	    		this.posService.customerDelete(this.customer.customerId, {posUI:posUI})
	    	    	.success(function (data, status, headers, config) {
	    	    		config.posUI.posCustomerSearchUI.customer.status = 'DELETED';
	    	    		config.posUI.posCustomerSearchUI.searchResult.items[config.posUI.posCustomerSearchUI.customerIndex] = config.posUI.posCustomerSearchUI.customer;
	    	    	})
	    	    	.error(function (error) {alert('[deleteCustomer] Request Failed: ' + error.message);});
				this.backToSearch(posUI);
			}
		},
		customerSave : function (posUI) {
			this.customer.shopId = SHOP_ID;
			var x = this.customer.contact.phoneList.length;
			while (x--) {
				if (!this.customer.contact.phoneList[x].phoneNumber) {
					this.customer.contact.phoneList.splice(x, 1);
				}
			}
			if (posUI.posCustomerSearchUI.customer.customerId) {
		    	this.posService.updateCustomer({entity:this.customer}, {posUI:posUI})
			    .success(function (data, status, headers, config) {
		    		config.posUI.posCustomerSearchUI.searchResult.items[config.posUI.posCustomerSearchUI.customerIndex] = config.posUI.posCustomerSearchUI.customer;
		    		config.posUI.posCustomerSearchUI.onCustomerUpdate(config.posUI);
		    		alert('Customer saved successfully');
			    })
			    .error(function (error) {alert('Request Failed: ' + error.message);});
			} else {
				this.customer.contact.languageId = posUI.shop.languageTables[0].language.languageId;
		    	this.posService.customerCreate({entity:this.customer}, {posUI:posUI})
			    .success(function (data, status, headers, config) {
		    		config.posUI.posCustomerSearchUI.customer.customerId = data.entity.customerId;
		    		config.posUI.posCustomerSearchUI.customer.customerNumber = data.entity.customerNumber;
		    		config.posUI.posCustomerSearchUI.onCustomerUpdate(config.posUI);
		    		alert('Customer saved successfully');
			    })
			    .error(function (error) {alert('Request Failed: ' + error.message);});
			}
		},
		showCreateAddress : function (posUI) {
//			this.addressAutocomplete = null;
			this.addressIndex = -1;
			this.address = {
				status:'ACTIVE',
				address : null,
				geocodeLatitude : null,
				geocodeLongitude : null,
				suburb : null,
				suburbName : null,
				stateCode : null,
				countryCode : null,
				postcode : null,
				suburbId : null
			};

			this.hideAllContainers();
			this.container.addressDetail = true;
//			var posServiceLocal = this.posService;
			posUI.util.googleMaps.addressAutocomplete('address', posUI.shop.address.countryCode, this.address, function () {
				posUI.posCustomerSearchUI.onAddressAutocompleteSuccess(posUI);
			});
		},
		onAddressAutocompleteSuccess : function (posUI) {
//			posUI.getScope().$apply();
			posUI.getTimeout()(function (){
				if (posUI.posCustomerSearchUI.address.countryCode && posUI.posCustomerSearchUI.address.postcode) {
					posUI.posService.suburbSearch(
						{countryCode:posUI.posCustomerSearchUI.address.countryCode,postcode:posUI.posCustomerSearchUI.address.postcode}, {posUI:posUI}
					)
					.success(function (data, status, headers, config) {
						config.posUI.posCustomerSearchUI.suburbOptions = data;
						for (var i = 0; i < data.length; i++) {
							if (data[i].name.toUpperCase() == config.posUI.posCustomerSearchUI.address.suburbName.toUpperCase()) {
								config.posUI.posCustomerSearchUI.address.suburb = data[i]; 
								config.posUI.posCustomerSearchUI.address.suburbId = data[i].suburbId; 
								break;
							}
						}
					})
					.error(function (error) {alert('[suburbSearch] Request Failed: ' + error.message);});
				}
				posUI.util.googleMaps.setupMap(posUI.posCustomerSearchUI.address, 'customerAddressMap');
			}, 1000);
		},
		deleteAddress : function () {
			if (this.address.theUserAddressId) {
				this.customer.contact.addressList[this.addressIndex].status = 'DELETED';
			} else {
				this.customer.contact.addressList.splice(this.addressIndex, 1);
			}
			this.backToCustomerDetails();
		},
		updateAddress : function () {
			if (this.addressIndex < 0) {
				this.customer.contact.addressList.push(this.address);
			} else {
				this.customer.contact.addressList[this.addressIndex] = this.address;
			}
			this.backToCustomerDetails();
		},
		showUpdateAddress : function (posUI, address, index) {
			posUI.posCustomerSearchUI.address = address;
			if (posUI.posCustomerSearchUI.address) {
				if (posUI.posCustomerSearchUI.address) {
					posUI.posCustomerSearchUI.address.suburb = {suburbId:posUI.posCustomerSearchUI.address.suburbId};
				}
			}
    		this.posService.getBusinessServiceSuburbList(
        			{posUI:posUI}
        		)
    	    	.success(function (data, status, headers, config) {
    	    		posUI.posCustomerSearchUI.suburbOptions = data;
    	    		posUI.posMenuUI.servicedSuburbs = data;
    	    	})
    	    	.error(function (error) {
    	    		alert('[showUpdateAddress] Request Failed: ' + error.message);
    	    	}
    	    );

			this.addressIndex = index;
			this.address = angular.copy(address);
			this.hideAllContainers();
			this.container.addressDetail = true;
			posUI.util.googleMaps.addressAutocomplete('address', posUI.shop.address.countryCode, this.address, function () {
				posUI.posCustomerSearchUI.onAddressAutocompleteSuccess(posUI);
			});
			posUI.posCustomerSearchUI.onAddressAutocompleteSuccess(posUI);
		},
		deletePhone : function () {
			if (this.phone.theUserPhoneId) {
				this.customer.contact.phoneList[this.phoneIndex].status = 'DELETED';
			} else {
				this.customer.contact.phoneList.splice(this.phoneIndex, 1);
			}
			this.backToCustomerDetails();
		},
		updatePhone : function () {
			if (this.phoneIndex < 0) {
				this.customer.contact.phoneList.push(this.phone);
			} else {
				this.customer.contact.phoneList[this.phoneIndex] = this.phone;
			}
			this.backToCustomerDetails();
		},
		showCreatePhone : function () {
			this.phoneIndex = -1;
			this.phone = {status:'ACTIVE'};
			this.hideAllContainers();
			this.container.phoneDetail = true;
		},
		showUpdatePhone : function (phone, index) {
			this.phoneIndex = index;
			this.phone = angular.copy(phone);
			this.hideAllContainers();
			this.container.phoneDetail = true;
		},
		backToSearch : function (posUI) {
			this.customer = null;
			this.customerAddressesWithinServicedSuburbs = null;
			this.hideAllContainers();
			this.container.showSearch = true;
		},
		showSearch : function (posUI) {
			this.hideAllContainers();
			this.container.showSearch = true;
		},
	    hideAllContainers : function () {
			for(var key in this.container) {
			    this.container[key] = false;
			}
	    }
	};
	result.resetSearch();
	return result;
}]);

MYORDY_POS_APP.factory('posOrderItemOptionsUI', [function() {
	return {
		options : {},
		parentComboMenuItems : null,
		orderItem : null,
		onDoneCallback : null,
		orderOptionAdd : function (posUI, item) {
			this.orderItem.extraOptions.addOptions.push({option:item,qty:1,amount:0});
			posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
		},
		addOptionDelete : function (posUI, index) {
			this.orderItem.extraOptions.addOptions.splice(index, 1);
			posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
		},
		orderOptionRemove : function (posUI, item) {
			this.orderItem.extraOptions.removeOptions.push({option:item,qty:1,amount:0,price:item.price});
			posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
		},
		removeOptionDelete : function (posUI, index) {
			this.orderItem.extraOptions.removeOptions.splice(index, 1);
			posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
		},
		filterParentComboMenuItems : function (posUI, includeTag, excludeTag) {
			var result = [];
			if (this.parentComboMenuItems) {
				for (var i = 0; i < this.parentComboMenuItems.length; i++) {
					if (this.parentComboMenuItems[i].tagList) {
						var candidate = this.parentComboMenuItems[i];
						if (includeTag) {
							candidate = null;
							for (var j = 0; j < this.parentComboMenuItems[i].tagList.length; j++) {
								if (this.parentComboMenuItems[i].tagList[j].code == includeTag) {
									candidate = this.parentComboMenuItems[i];
									break;
								}
							} 
						}
						if (excludeTag) {
							for (var j = 0; j < this.parentComboMenuItems[i].tagList.length; j++) {
								if (this.parentComboMenuItems[i].tagList[j].code == excludeTag) {
									candidate = null;
									break;
								}
							}
						}
						if (candidate) {
							result.push(candidate);
						}
					}
				}
			}
			result.sort(
				function (a, b) {
					var aName = posUI.shopLanguageTable[posUI.util.message.getComboMenuItemNameCode(a)];
					var bName = posUI.shopLanguageTable[posUI.util.message.getComboMenuItemNameCode(b)];
					return aName.localeCompare(bName);
				}
			);

			return result;
		},
		initAddOptions : function (posUI, menuItem) {
			this.options = {extraItems:{addOptions:[],removeOptions:[]}};
			if (menuItem) {
				if (menuItem.extraOptionConfig && menuItem.extraOptionConfig.extraOptionsGroupCode) {
					this.options.extraItems.addOptions = posUI.util.orderItemOption.findExtraOptionGroup(
						menuItem.extraOptionConfig.extraOptionsGroupCode, posUI.shop.business.extraOptionGroupList
					);
					for (var i = 0; i < this.options.extraItems.addOptions.extraOptionItemList.length; i++) {
						this.options.extraItems.addOptions.extraOptionItemList[i].name
							= posUI.shopLanguageTable[posUI.util.message.getExtraOptionItemNameCode(this.options.extraItems.addOptions.extraOptionItemList[i])];
					}
				}
			}
		},
		initOptions : function (posUI, orderItem, onDoneCallback) {
			// TODO do the following only if this.orderItem != orderItem
			this.orderItem = orderItem;
			this.onDoneCallback = onDoneCallback;
			this.parentComboMenuItems = posUI.util.menuItem.findParentComboMenuItems(this.orderItem.menuItem, posUI.posMenuUI.menu.menuItemComboList);
			this.initAddOptions(posUI, this.orderItem.menuItem);
		}
    };
}]);

MYORDY_POS_APP.factory('posMenuComboItemUI', [function() {
	return {
		comboOrderItem : null,
		comboOption : null,
		comboOptionSelectProductMenuItems : null,
		onCancelCallback : null,
		comboOrderItemValidationResult : null,
		addOrderItemToCart : false,
		comboOrdyItemListIndex : -1,
		getComboOptionComboOrderItems : function (posUI, comboOption) {
			var result = [];
			if (posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.orderItems) {
				for (var i = 0; i < posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.orderItems.length; i++) {
					var ordyItem = posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.orderItems[i];
					if (posUI.util.tag.isTagInListById(ordyItem.orderItem.menuItem.tagList, comboOption.menuItemTag)) {
						result.push(ordyItem);
					}
				}
			}
			return result;
		},
		getComboOptionComboOrderItemsComboSub : function (posUI, comboOption) {
			var result = [];
			if (posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.ordyItemsComboSub) {
				for (var i = 0; i < posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.ordyItemsComboSub.length; i++) {
					var ordyItemComboSub = posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.ordyItemsComboSub[i];
					if (ordyItemComboSub.orderItemComboSub && ordyItemComboSub.orderItemComboSub.comboMenuItem) {
						if (posUI.util.tag.isTagInListById(ordyItemComboSub.orderItemComboSub.comboMenuItem.tagList, comboOption.menuItemTag)) {
							result.push(ordyItemComboSub);
						}
					}
				}
			}
			return result;
		},
		minusQty : function (posUI) {
			var comboOrderItem = posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem;
			if (comboOrderItem.qty > 1) {
				comboOrderItem.qty--;
			}
			posUI.cartUI.setComboOrderItemAmount(posUI, this.comboOrderItem);
		},
		plusQty : function (posUI) {
			var comboOrderItem = posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem;
			comboOrderItem.qty++;
			posUI.cartUI.setComboOrderItemAmount(posUI, this.comboOrderItem);
		},
		minusQtyChoice : function (posUI, ordyItem) {
			if (ordyItem.qty > 1) {
				ordyItem.qty--;
			}
			posUI.cartUI.setComboOrderItemAmount(posUI, this.comboOrderItem);
		},
		plusQtyChoice : function (posUI, ordyItem) {
			ordyItem.qty++;
			posUI.cartUI.setComboOrderItemAmount(posUI, this.comboOrderItem);
		},
		onComboOptionItemRemove : function (posUI, index) {
			if (confirm('Are you sure you wish to remove this item?')) {
				posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.orderItems.splice(index, 1);
				posUI.cartUI.setComboOrderItemAmount(posUI, this.comboOrderItem);
			}
		},
		onCancel : function (posUI) {
			this.onCancelCallback(posUI);
		},
		onAddToCart : function (posUI) {
			this.comboOrderItemValidationResult = this.validateComboOrderItem(posUI, this.comboOrderItem);
			if (this.comboOrderItemValidationResult.valid) {
				posUI.cartUI.addComboItemToCart(posUI);
				posUI.posMenuItemUI.closeMenuItemModelBox();
				posUI.posMenuItemUI.showHome(posUI);
			}
		},
		validateComboOrderItem : function (posUI, comboOrderItem) {
			var result = {valid:true,errorMessageList:[]};
			for (var i = 0; i < comboOrderItem.comboMenuItem.comboOptionList.length; i++) {
				this.validateComboOrderItemOption(posUI, comboOrderItem, comboOrderItem.comboMenuItem.comboOptionList[i], result);
			}
			return result;
		},
		validateComboOrderItemOption : function (posUI, comboOrderItem, comboOption, result) {
			var comboOptionQtyFound = 0;
			for (var i = 0; i < comboOrderItem.orderItems.length; i++) {
				for (var j = 0; j < comboOrderItem.orderItems[i].orderItem.menuItem.tagList.length; j++) {
					if (comboOrderItem.orderItems[i].orderItem.menuItem.tagList[j].tagId == comboOption.menuItemTag.tagId) {
						comboOptionQtyFound += comboOrderItem.orderItems[i].qty;
						break;
					}
				}
			}
			for (var i = 0; i < comboOrderItem.ordyItemsComboSub.length; i++) {
				for (var j = 0; j < comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.comboMenuItem.tagList.length; j++) {
					if (comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.comboMenuItem.tagList[j].tagId == comboOption.menuItemTag.tagId) {
						comboOptionQtyFound += comboOrderItem.ordyItemsComboSub[i].qty;
						break;
					}
				}
			}
			if (comboOptionQtyFound < comboOption.qtyRequired) {
				result.valid = false;
				result.errorMessageList.push(comboOption.qtyRequired + ' ' + posUI.shopLanguageTable[posUI.util.message.getComboMenuItemOptionNameFullCode(comboOption)] + ' required');
			} else if (comboOptionQtyFound > comboOption.qtyRequired) {
				result.valid = false;
				result.errorMessageList.push(posUI.shopLanguageTable[posUI.util.message.getComboMenuItemOptionNameFullCode(comboOption)] + ' cant be more than ' + comboOption.qtyRequired);
			}
		},
		onComboOptionSelect : function (posUI, menuItem) {
			posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.orderItems.push(
				{orderItem:posUI.cartUI.makeNewOrderItem(posUI, menuItem, null),qty:1}
			);
			posUI.cartUI.setComboOrderItemAmount(posUI, this.comboOrderItem);
			posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItemValidationResult = null;
			posUI.posMenuItemUI.showComboMenuItem(posUI);
		},
		showComboOptionSelect : function (posUI, comboOption) {
			this.comboOption = comboOption;
			this.comboOptionSelectProductMenuItems = posUI.util.menuItem.findProductMenuItemByTag(posUI.posMenuUI.menuItemMap, posUI.posMenuUI.menu.menuId, this.comboOption.menuItemTag.tagId);
			if (this.comboOptionSelectProductMenuItems) {
				this.comboOptionSelectProductMenuItems.sort(
					function (a, b) {
						var aName = null;
						var bName = null;
						if (a.product && b.product) {
							aName = posUI.shopLanguageTable[posUI.util.message.getProductNameFullCode(a.product)];
							bName = posUI.shopLanguageTable[posUI.util.message.getProductNameFullCode(b.product)];
						} else {
							var aName = '';
							var bName = '';
						}
						return aName.localeCompare(bName);
					}
				);
			}
			posUI.posMenuItemUI.showComboMenuItemChoice(posUI);
		},
		onCustomiseDone : function (posUI) {
			posUI.cartUI.setComboOrderItemAmount(posUI, posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem);
			posUI.posMenuItemUI.showComboMenuItem(posUI);
		},
		showComboOrderItem : function (posUI, onCancelCallback, comboOrderItem) {
			this.comboOrderItemValidationResult = null;
			this.onCancelCallback = onCancelCallback;
    		this.comboOrderItem = comboOrderItem;
			posUI.posMenuItemUI.showComboMenuItem(posUI);
		},
		onOrderItemUpdateDone : function (posUI) {
			this.comboOrderItemValidationResult = this.validateComboOrderItem(posUI, this.comboOrderItem);
			if (this.comboOrderItemValidationResult.valid) {
//				posUI.posMenuItemUI.closeMenuItemModelBox();
				posUI.cartUI.cart.comboOrdyItemList[this.comboOrdyItemListIndex] = this.comboOrderItem;
				posUI.cartUI.resetCartAmount(posUI);
				posUI.posMenuItemUI.showHome(posUI);
			}
		},
		onOrderItemClickFromCart : function (posUI, onCancelCallback, comboOrderItem, comboOrdyItemListIndex) {
			this.comboOrdyItemListIndex = comboOrdyItemListIndex;
			this.addOrderItemToCart = false;
			this.showComboOrderItem(posUI, onCancelCallback, angular.copy(comboOrderItem));
		},
		setupNewComboOrderItem : function (posUI, comboMenuItem, qty) {
			this.comboOrdyItemListIndex = -1;
			this.addOrderItemToCart = true;
    		var comboOrderItem = posUI.cartUI.makeNewComboOrderItem(posUI, comboMenuItem, qty);
			posUI.cartUI.setComboOrderItemAmount(posUI, comboOrderItem);
    		this.comboOrderItem = comboOrderItem;
		},
		onMenuItemComboSelectFromMenu : function (posUI, comboMenuItem) {
			var qty = 1;
			this.setupNewComboOrderItem(posUI, comboMenuItem, qty);
			this.showComboOrderItem(posUI,
				function(posUI){posUI.posMenuItemUI.closeMenuItemModelBox(); posUI.posMenuItemUI.showHome(posUI);}, this.comboOrderItem
			);
		},
		onMenuItemComboSelectFromMenuItem : function (posUI, comboMenuItem, qty, onCancelCallback) {
			this.comboOrdyItemListIndex = -1;
			this.addOrderItemToCart = true;
    		var comboOrderItem = posUI.cartUI.makeNewComboOrderItem(posUI, comboMenuItem, qty);
			comboOrderItem.orderItems.push({orderItem:posUI.posMenuItemUI.orderItem,qty:1});
			posUI.cartUI.setComboOrderItemAmount(posUI, comboOrderItem);
			this.showComboOrderItem(posUI, onCancelCallback, comboOrderItem);
		}
    };
}]);


MYORDY_POS_APP.factory('posMenuItemUI', ['posService', 'posOrderItemOptionsUI', 'posMenuComboItemUI', '$modal', function(posServiceArg, posOrderItemOptionsUIArg, posMenuComboItemUIArg, $modal) {
	return {
		posService : posServiceArg,
		posOrderItemOptionsUI : posOrderItemOptionsUIArg,
		posMenuComboItemUI : posMenuComboItemUIArg,
		container : {showDetail:false,showCustomise:false,comboMenuItem:false,comboMenuItemChoice:false},
		menuItem : null,
		parentMenuItem : null,
		modalInstance : null,
		orderItem : null,
		selectedOrderItemIndex : null,
		addOrderItemToCart : false,
		priceFormatted : 0.00,
		menuItemL1 : null,
		specialsMenuItems : null,
		showHome : function (posUI) {
			posUI.hideAllPOSRootContainers();
			posUI.rootContainer.showHome = true;
		},
		showSpecials : function (posUI) {
			posUI.hideAllPOSRootContainers();
			posUI.rootContainer.showSpecials = true;
			this.specialsMenuItems = posUI.util.menuItem.findComboMenuItemByTag(posUI.posMenuUI.menu.menuItemComboList, "[-SPECIAL-]");
			if (!this.specialsMenuItems.sorted) {
				this.specialsMenuItems.sort(
					function (a, b) {
						var aName = null;
						var bName = null;
						aName = posUI.shopLanguageTable[posUI.util.message.getComboMenuItemNameFullCode(a)];
						bName = posUI.shopLanguageTable[posUI.util.message.getComboMenuItemNameFullCode(b)];
						return aName.localeCompare(bName);
					}
				);
				this.specialsMenuItems.sorted = true;
			}
		},
		onMenuItemClick : function (posUI, menuItem) {
			this.menuItemL1 = menuItem;
			posUI.hideAllPOSRootContainers();
			posUI.rootContainer.showMenuItem = true;
			if (!this.menuItemL1.menuItems.sorted) {
				this.menuItemL1.menuItems.sort(
						function (a, b) {
							var aName = null;
							var bName = null;
							if (a.category) {
								aName = posUI.shopLanguageTable[posUI.util.message.getCategoryNameCode(a.category)];
							} else if (a.product) {
								aName = posUI.shopLanguageTable[posUI.util.message.getProductNameFullCode(a.product)];
							}
							if (b.category) {
								bName = posUI.shopLanguageTable[posUI.util.message.getCategoryNameCode(b.category)];
							} else if (b.product) {
								bName = posUI.shopLanguageTable[posUI.util.message.getProductNameFullCode(b.product)];
							}
							return aName.localeCompare(bName);
						}
				);
				this.menuItemL1.menuItems.sorted = true;
			}
		},
		onDeleteComboItemFromCart : function (posUI) {
			if (confirm('Are you sure you wish to delete this item?')) {
				posUI.cartUI.cart.comboOrdyItemList.splice(this.posMenuComboItemUI.comboOrdyItemListIndex, 1);
				//this.closeMenuItemModelBox();
				posUI.cartUI.resetCartAmount(posUI);
				posUI.posMenuItemUI.showHome(posUI);
				posUI.cartUI.resetShowDiscountPercent(posUI);
			}
		},

		onAddToCard : function (posUI) {
			posUI.cartUI.addToCart(posUI);
			posUI.posMenuItemUI.showHome(posUI);
		},

		onOrderItemUpdateDone : function (posUI) {
			posUI.cartUI.optomiseCart(posUI);
			posUI.posMenuItemUI.showHome(posUI);
		},

		onOrderItemCancel : function (posUI) {
			posUI.posMenuItemUI.showHome(posUI);
		},


		onDeleteFromCart : function (posUI) {
			if (confirm('Are you sure you wish to delete this item?')) {
				posUI.cartUI.cart.ordyItemList.splice(this.selectedOrderItemIndex, 1);
				//this.closeMenuItemModelBox();
				posUI.cartUI.resetCartAmount(posUI);
				posUI.posMenuItemUI.showHome(posUI);
				posUI.cartUI.resetShowDiscountPercent(posUI);
			}
		},
		onMenuItemComboSelectFromMenu : function (posUI, comboMenuItem) {
//			if (this.modalInstance) {
//				this.modalInstance.close();
//			}
//			this.openModelInstance(posUI);
			this.posMenuComboItemUI.onMenuItemComboSelectFromMenu(posUI, comboMenuItem);
		},
		onOrderItemMenuItemChange : function (posUI, menuItem) {
			posUI.posMenuItemUI.orderItem.menuItem = menuItem;
			posUI.posMenuItemUI.orderItem.extraOptions = {addOptions:[],removeOptions:[]};
			posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
			posUI.posMenuItemUI.posOrderItemOptionsUI.initOptions(posUI, this.orderItem, null);
		},
	    showDetail : function (posUI) {
	    	posUI.posMenuItemUI.hideAllContainers();
	    	posUI.posMenuItemUI.container.showDetail = true;
		},
		showMenuItemL1 : function (posUI) {
	    	posUI.posMenuItemUI.hideAllContainers();
	    	posUI.rootContainer.showMenuItem = true;
	    	//posUI.posMenuItemUI.container.showDetail = true;
		},
		showCustomise : function (posUI, orderItem, onDoneCallback) {
			posUI.posMenuItemUI.posOrderItemOptionsUI.initOptions(posUI, orderItem, onDoneCallback);
			posUI.posMenuItemUI.hideAllContainers();
			posUI.posMenuItemUI.container.showCustomise = true;
		},
		showComboMenuItem : function (posUI) {
			posUI.hideAllPOSRootContainers();
			posUI.rootContainer.showMenuItemSecondLevel = true;
			posUI.posMenuItemUI.hideAllContainers();
			posUI.posMenuItemUI.container.comboMenuItem = true;
		},
		showComboMenuItemChoice : function (posUI) {
			posUI.posMenuItemUI.hideAllContainers();
			posUI.posMenuItemUI.container.comboMenuItemChoice = true;
		},
		editComboOrderItemSub : function (posUI, comboOrderItem, ordyItemComboSub, comboOrdyItemListIndex) {
			comboOrderItem.ordyItemsComboSub.splice(comboOrdyItemListIndex, 1);
			posUI.cartUI.cart
				.comboOrdyItemList[posUI.posMenuItemUI.posMenuComboItemUI.comboOrdyItemListIndex]
				.ordyItemsComboSub.splice(comboOrdyItemListIndex, 1);
	
			posUI.cartUI.optomiseCart(posUI);

			posUI.posMenuItemUI.posMenuComboItemUI.comboOrdyItemListIndex = -1;
			posUI.posMenuItemUI.posMenuComboItemUI.addOrderItemToCart = true;
			posUI.posMenuItemUI.posMenuComboItemUI.showComboOrderItem(posUI, null, ordyItemComboSub.orderItemComboSub);
		},
		showComboOrderItemInModelBox : function (posUI, comboOrderItem, comboOrdyItemListIndex) {
	    	posUI.posMenuItemUI.posMenuComboItemUI.onOrderItemClickFromCart(posUI, null, comboOrderItem, comboOrdyItemListIndex);
		},
		showOrderItemInModelBox : function (posUI, orderItem, index) {
			this.selectedOrderItemIndex = index;
	    	this.showMenuItemInModelBox(posUI, orderItem.parentMenuItem, orderItem.clickedMenuItem, orderItem);
		},
	    closeMenuItemModelBox : function () {
	    	if (this.modalInstance) {
	    		this.modalInstance.close();
	    	}
		},
	    showMenuItemInModelBox : function (posUI, parentMenuItem, menuItem, orderItem) {


	    	if (orderItem) {
	    		this.orderItem = orderItem;
	    		this.addOrderItemToCart = false;
	    	} else {
	    		this.orderItem = posUI.cartUI.makeNewOrderItem(posUI, menuItem, parentMenuItem);
	    		this.addOrderItemToCart = true;
	    	}
			posUI.posMenuItemUI.posOrderItemOptionsUI.initOptions(posUI, this.orderItem, null);

	    	this.parentMenuItem = parentMenuItem;
			this.menuItem = menuItem;
			this.hideAllContainers();
			this.container.showDetail = true;
			this.minusQty = function (posUI) {
				if (this.orderItem.qty > 1) {
					this.orderItem.qty--;
				}
				posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
			};
			this.plusQty = function (posUI) {
				this.orderItem.qty++;
				posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
			};
	    	
	    	posUI.hideAllPOSRootContainers();
			posUI.rootContainer.showMenuItemSecondLevel = true;

/*
	    	if (this.modalInstance) {
	    		this.modalInstance.close();
	    	}
	    	if (orderItem) {
	    		this.orderItem = orderItem;
	    		this.addOrderItemToCart = false;
	    	} else {
	    		this.orderItem = posUI.cartUI.makeNewOrderItem(posUI, menuItem, parentMenuItem);
	    		this.addOrderItemToCart = true;
	    	}
			posUI.posMenuItemUI.posOrderItemOptionsUI.initOptions(posUI, this.orderItem, null);

	    	this.parentMenuItem = parentMenuItem;
			this.menuItem = menuItem;
			this.hideAllContainers();
			this.container.showDetail = true;
			this.minusQty = function (posUI) {
				if (this.orderItem.qty > 1) {
					this.orderItem.qty--;
				}
				posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
			};
			this.plusQty = function (posUI) {
				this.orderItem.qty++;
				posUI.cartUI.setOrderItemAmount(posUI, this.orderItem);
			};
			this.openModelInstance(posUI);
*/
	    },
	    openModelInstance : function (posUI) {
			this.modalInstance = $modal.open({
				templateUrl: 'posMenuItemModel.jspf',
//				windowClass: 'app-modal-window',
				controller: 'POSMenuItemModalInstanceCtrl',
				resolve: {
					request : function () {
						return {
							posUI : posUI
						};
					}
				}
			});
			this.modalInstance.result.then(function () {
			}, function () {
			});
	    },
	    hideAllContainers : function () {
			for(var key in this.container) {
			    this.container[key] = false;
			}
	    }
    };
}]);

MYORDY_POS_APP.factory('posMenuUI', ['posService', '$modal',
function(posServiceArg, $modal) {
    return {
    	menu : null,
    	modalInstance : null,
    	servicedSuburbs : null,
		posService : posServiceArg,
    	menuItemMap : null,
    	onMenuSelect : function (posUI, menu) {
    		if (menu) {
    			posUI.hideAllPOSContainers();
    			posUI.container.showMenu = true;
    			posUI.posMenuUI.menu = menu;
    			posUI.cartUI.cart = posUI.cartUI.resetCart(posUI);
    			posUI.cookies.put('posMenuUI.menu.menuId', menu.menuId);

    			posUI.posService.getMenuServiceSuburbList(
            		posUI.posMenuUI.menu.menuId, {posUI:posUI}
            	)
        	    .success(function (data, status, headers, config) {
        	    	config.posUI.posMenuUI.servicedSuburbs = data;
    	    	})
    	    	.error(function (error) {
    	    		alert('[getMenuServiceSuburbList] Request Failed: ' + error.message);}
    	    	);

    			posUI.posCustomerSearchUI.onCustomerUpdate(posUI);
    		}
//	   		posUI.scope.$broadcast("MENU_CHANGED", menu);
//	   		posUI.posMenuUI.menu = null;
//    		posUI.posMenuUI.menu = menu;
//    		posUI.posMenuUI.menu = posUI.shop.menuList[0];
//window.location.reload();
//	   		console.log(posUI.shop.menuList[0]);
    	},
//		getShopServiceSuburbList : function (config) {
//		    return $http.get(this.contextPath + this.urlBase + '/shopServiceSuburbList', config);
//		},

//    	xxx : function (posUI, suburb) {
//			posUI.posCustomerSearchUI.onCustomerSuburbChange(posUI, suburb);
////    		console.log(suburb);
//    	},
    	showBusinessServicedSuburbsConfig : function (posUI) {
    		this.posService.getBusinessServiceSuburbList(
        			{posUI:posUI}
        		)
    	    	.success(function (data, status, headers, config) {
    	    		//console.log(data);
    	    		posUI.posCustomerSearchUI.suburbOptions = data;
    	    		posUI.posMenuUI.servicedSuburbs = data;
    	    		this.modalInstance = $modal.open({
        				templateUrl: 'posSuburbSelectModel.jspf',
        				controller: 'POSSuburbSelectModalInstanceCtrl',
        				resolve: {
        					request : function () {
        						return {
        							posUI : posUI
        						};
        					}
        				}
        			});
    	    	})
    	    	.error(function (error) {
    	    		alert('[showBusinessServicedSuburbsConfig] Request Failed: ' + error.message);}
    	    	);
		},
    	showServicedSuburbsConfig : function (posUI) {
    		this.posService.getMenuServiceSuburbList(
    			posUI.posMenuUI.menu.menuId, {posUI:posUI}
    		)
	    	.success(function (data, status, headers, config) {
	    		config.posUI.posMenuUI.servicedSuburbs = data;

    	    	this.modalInstance = $modal.open({
    				templateUrl: 'posMenuModel.jspf',
    				controller: 'POSMenuModalInstanceCtrl',
    				resolve: {
    					request : function () {
    						return {
    							posUI : posUI
    						};
    					}
    				}
    			});

	    	})
	    	.error(function (error) {
	    		alert('[getMenuServiceSuburbList] Request Failed: ' + error.message);}
	    	);
    	},
    	resetMenu : function (posUI) {
    		var selectedMenu = null;
    		posUI.hideAllPOSContainers();
    		posUI.posMenuUI.menu = null;
    		posUI.posMenuUI.servicedSuburbs = null;
    		if (posUI.shop && posUI.shop.menuList) {
    			if (posUI.shop.menuList.length > 1) {
    				if (posUI.cookies.get('posMenuUI.menu.menuId')) {
    					for (var i = 0; i < posUI.shop.menuList.length; i++) {
    						if (posUI.cookies.get('posMenuUI.menu.menuId') == posUI.shop.menuList[i].menuId) {
//    		    				posUI.container.showMenu = true;
    							selectedMenu = posUI.shop.menuList[i];
    							break;
    						}
    					}
    				}
    			} else {
//    				posUI.container.showMenu = true;
    				selectedMenu = posUI.shop.menuList[0];
//    				posUI.cookies.put('posMenuUI.menu.menuId', posUI.posMenuUI.menu.menuId);
    			}
    			if (selectedMenu) {
    				posUI.posMenuUI.onMenuSelect(posUI, selectedMenu);
    			} else {
    				posUI.hideAllPOSContainers();
    				posUI.container.showMenuSelect = true;
    			}
    		}
    	},
    	onShopLoad : function (posUI) {
//console.log(posUI.shop);
	   		if (posUI.shop) {
	   			posUI.posMenuUI.resetMenu(posUI);
    			posUI.posMenuUI.menuItemMap = posUI.util.menuItem.getMenuItemMap(posUI.shop);
	   		} else {
	   			alert('Shop load failed ... please try again.');
	   		}
    	}
    };
}]);

MYORDY_POS_APP.factory('cartUI', ['posService', '$modal',
function(posServiceArg, $modal) {
    return {
//    	menuCart : {},
    	modalInstance : null,
		container : {showOrderItems:true,showCustomerContactDetailsForm:false,showOnlinePaymentForm:false},
		cartContainer : {showDiscount:true},
		menuDiscountCoupon : {
			couponCode:null,
			coupon:null,
			couponError:null,
			restoreOldState:function(posUI, menuDiscountCoupon) {
				posUI.cartUI.menuDiscountCoupon.couponCode = menuDiscountCoupon.couponCode;
				posUI.cartUI.menuDiscountCoupon.coupon = menuDiscountCoupon.coupon;
				posUI.cartUI.menuDiscountCoupon.couponError = menuDiscountCoupon.couponError;
			},
			resetAndGetOldState:function(posUI) {
				var result = {
					couponCode:posUI.cartUI.menuDiscountCoupon.couponCode,
					coupon:posUI.cartUI.menuDiscountCoupon.coupon,
					couponError:posUI.cartUI.menuDiscountCoupon.couponError
				};
				posUI.cartUI.menuDiscountCoupon.couponCode = null;
				posUI.cartUI.menuDiscountCoupon.coupon = null;
				posUI.cartUI.menuDiscountCoupon.couponError = null;
				return result;
			},
			redeem:function(posUI) {
				posUI.cartUI.menuDiscountCoupon.coupon = null;
				posUI.cartUI.menuDiscountCoupon.couponError = null;
				posUI.cartUI.cart.cashback = null;
				posUI.cartUI.cart.menuDiscountCoupon = null;
				var coupon = posUI.util.tools.findArrayItemByPropertyValue(posUI.cartUI.cart.menu.menuDiscountCouponList, 'couponCode', posUI.cartUI.menuDiscountCoupon.couponCode);
				if (coupon) {
					var setCashbackOnMinimumFinalTotal = function (minimumFinalTotalAmount, cashbackAmount)	{
						posUI.cartUI.cart.cashback = 0;
						var finalTotalAmount = posUI.cartUI.getFinalTotalAmount(posUI, posUI.cartUI.cart);
						if (finalTotalAmount < minimumFinalTotalAmount) {
							posUI.cartUI.menuDiscountCoupon.couponError = 'Minimum order amount validation failed';
						} else {
							posUI.cartUI.cart.menuDiscountCoupon = {status:'ACTIVE',menuDiscountCouponId:coupon.menuDiscountCouponId};
							posUI.cartUI.menuDiscountCoupon.coupon = coupon;
							posUI.cartUI.cart.cashback = cashbackAmount;
						}
					};
					if (coupon.ordyCashbackFormula) {
						eval(coupon.ordyCashbackFormula + ';');
					}
				} else {
					posUI.cartUI.menuDiscountCoupon.couponError = 'Coupon not found';
				}
			}
		},
    	cart : null,
    	customerPhoneNumbers : null,
    	deliveryAddressAutocomplete : null,
    	userIsPOSOperator : false,
		posService : posServiceArg,
		onLoginResponseChange : function (posUI, loginResponse) {
    		this.userIsPOSOperator = posUI.util.security.isInStaffRoleList(loginResponse, "SHOP_POS_OPERATOR");
    		this.setDiscountPercent(posUI, this.cart);
       		if (this.userIsPOSOperator) {
       			posUI.ordyInboxUI.startInboxMonitor(posUI);
       		} else {
       			posUI.ordyInboxUI.stopInboxMonitor(posUI);
       		}
    	},
		initCartUI : function (posUI) {
			posUI.getTimeout()(function (){
				posUI.cartUI.deliveryAddressAutocomplete = {
					status:'ACTIVE',
					address : null,
					geocodeLatitude : null,
					geocodeLongitude : null,
					suburb : null,
					suburbName : null,
					stateCode : null,
					countryCode : null,
					postcode : null,
					suburbId : null
				};
				posUI.util.googleMaps.addressAutocomplete('deliveryAddress', posUI.shop.address.countryCode, posUI.cartUI.deliveryAddressAutocomplete, function () {
					posUI.cartUI.cart.deliveryAddress = posUI.cartUI.deliveryAddressAutocomplete.address;
					if (posUI.posMenuUI.servicedSuburbs && posUI.cartUI.deliveryAddressAutocomplete.suburbName && posUI.cartUI.deliveryAddressAutocomplete.postcode) {
						for (var i = 0; i < posUI.posMenuUI.servicedSuburbs.length; i++) {
							if (posUI.posMenuUI.servicedSuburbs[i].postcode == posUI.cartUI.deliveryAddressAutocomplete.postcode
									&& posUI.posMenuUI.servicedSuburbs[i].name.toUpperCase() == posUI.cartUI.deliveryAddressAutocomplete.suburbName.toUpperCase()) {
								posUI.cartUI.cart.deliverySuburb = posUI.posMenuUI.servicedSuburbs[i];
								posUI.getScope().$apply();
								break;
							}
						}
					}
				});
			}, 1000);
		},
		processStripePayment : function (posUI) {
			posUI.blockUI.start("Processing payment ...");
			posUI.util.onlinePayment.stripe.createToken(posUI.onlinePayment.stripe);
		},
		cancelStripePayment : function (posUI) {
			if (confirm('Are you sure you wish to cancel this payment?')) {
				posUI.cartUI.cart.onlinePaymentStripeToken = null;
			}
		},
		showCustomerContactDetailsForm : function (posUI) {
			posUI.util.nav.hideAllContainers(this.container);
			this.container.showCustomerContactDetailsForm = true;
		},
		showOnlinePaymentForm : function (posUI) {
			posUI.util.nav.hideAllContainers(this.container);
			this.container.showOnlinePaymentForm = true;
		},
		onCustomerContactDetailsBackButtonClick : function (posUI) {
			if (posUI.util.onlinePayment.isOnlinePaymentAvailable(posUI.onlinePayment)) {
				posUI.cartUI.showOnlinePaymentForm(posUI);
			} else {
				posUI.cartUI.showOrderItems(posUI);
			}
		},
		onCartNextButtonClick : function (posUI) {
			if (posUI.util.menu.isDeliveryMenu(posUI.cartUI.cart.menu) && !posUI.cartUI.cart.deliverySuburb) {
				alert('Delivery suburb is required.');
			} else {
				if (posUI.util.onlinePayment.isOnlinePaymentAvailable(posUI.onlinePayment)) {
					posUI.cartUI.showOnlinePaymentForm(posUI);
				} else {
					posUI.cartUI.showCustomerContactDetailsForm(posUI);
				}
			}
		},
    	onStripeCreateToken : function (stripeUI, createTokenResult) {
    		stripeUI.posUI.getTimeout()(function (){
	    		stripeUI.posUI.blockUI.stop();
			}, 1000);
			if (createTokenResult.error) {
				document.getElementById(stripeUI.errorElementId).textContent = createTokenResult.error.message;
			} else {
		    	stripeUI.posUI.cartUI.cart.onlinePaymentStripeToken = createTokenResult.token.id;
		    	stripeUI.posUI.cartUI.showCustomerContactDetailsForm(stripeUI.posUI);
			}
		},
		onPaypalGetPayment : function (paypalUI) {
			return {
		    	flow: 'checkout',
		    	amount: paypalUI.posUI.cartUI.getFinalTotalAmount(paypalUI.posUI, paypalUI.posUI.cartUI.cart) / 100,
		    	currency: paypalUI.posUI.shop.currencyCode
		    };
		},
		onPaypalAuthorize : function (paypalUI, payload) {
			paypalUI.posUI.cartUI.cart.onlinePaymentPaypalNonce = payload.nonce;
	    	paypalUI.posUI.cartUI.showCustomerContactDetailsForm(paypalUI.posUI);
    	},
    	showOrderItems : function (posUI) {
			posUI.util.nav.hideAllContainers(this.container);
			this.container.showOrderItems = true;
		},
    	cancel : function (posUI, confirmIt) {
			var cart = posUI.cartUI.cart;
			var confirmed = true;
			if (confirmIt && cart && (cart.ordyItemList.length > 0 || cart.comboOrdyItemList.length > 0)) {
				confirmed = confirm('Are you sure you wish to cancel this order?');
			}
			if (confirmed) {
				posUI.cartUI.cart = null;
				posUI.cookies.remove('posMenuUI.menu.menuId');
				posUI.posMenuUI.resetMenu(posUI);
				posUI.posCustomerSearchUI.reset(posUI);
			}
		},
//		showSuburbWarning : function (posUI) {
//			var result = false;
//			if (posUI.cartUI.cart && posUI.cartUI.cart.menu && posUI.cartUI.cart.menu.menuOrdyConfig && posUI.cartUI.cart.menu.menuOrdyConfig.shopSuburbConfigList) {
//				for (var i = 0; i < posUI.cartUI.cart.menu.menuOrdyConfig.shopSuburbConfigList.length; i++) {
////					console.log(posUI.cartUI.cart.menu.menuOrdyConfig.shopSuburbConfigList[i]);
//				}
//				result = true;
//			}
//			return result;
//		},
		makeComboOrdyItemListForSubmission : function (posUI, comboOrdyItemList) {
			var result = [];
    		for (var i = 0; i < comboOrdyItemList.length; i++) {
    			var comboOrdyItemTemp = comboOrdyItemList[i];
    			var comboOrdyItemCopyTemp = {
    				menuItemComboId : comboOrdyItemTemp.comboMenuItem.menuItemComboId,
    	    		qty : comboOrdyItemTemp.qty,
    	    		note : comboOrdyItemTemp.note,
    	    		amount : comboOrdyItemTemp.amount,
    	    		orderItems:[],
    	    		ordyItemsComboSub:[]
    			};
        		for (var j = 0; j < comboOrdyItemTemp.orderItems.length; j++) {
        			var ordyItemForSubmission = posUI.cartUI.makeOrdyItemForSubmission(comboOrdyItemTemp.orderItems[j].orderItem);
        			ordyItemForSubmission.qty = comboOrdyItemTemp.orderItems[j].qty;
        			comboOrdyItemCopyTemp.orderItems.push(ordyItemForSubmission);
        		}
        		for (var j = 0; j < comboOrdyItemTemp.ordyItemsComboSub.length; j++) {
        			var ordyItemComboSubForSubmission = {
        				menuItemComboId : comboOrdyItemTemp.ordyItemsComboSub[j].orderItemComboSub.comboMenuItem.menuItemComboId,
        				orderItems : [],
        				note : '',
        				qty : comboOrdyItemTemp.ordyItemsComboSub[j].orderItemComboSub.qty
        			};
        			ordyItemComboSubForSubmission.qty = comboOrdyItemTemp.ordyItemsComboSub[j].qty;
        			for (var k = 0; k < comboOrdyItemTemp.ordyItemsComboSub[j].orderItemComboSub.orderItems.length; k++) {
        				ordyItemComboSubForSubmission.orderItems.push(
        					posUI.cartUI.makeOrdyItemForSubmission(comboOrdyItemTemp.ordyItemsComboSub[j].orderItemComboSub.orderItems[k].orderItem)
        				);
        			}
        			comboOrdyItemCopyTemp.ordyItemsComboSub.push(ordyItemComboSubForSubmission);
        		}

    			result.push(comboOrdyItemCopyTemp);
    		}
    		return result;
		},

		setDiscountPercent : function (posUI, order) {
			var result = 0;
			if (!posUI.cartUI.userIsPOSOperator) {
				if (posUI.posMenuUI.menu && posUI.posMenuUI.menu.menuOrdyConfig) {
					result += posUI.posMenuUI.menu.menuOrdyConfig.discountPercentOnlineOrdy;
					if (posUI.previousOrdersUI.previousOrders
							&& posUI.previousOrdersUI.previousOrders.items
							&& posUI.previousOrdersUI.previousOrders.items.length == 0) {
						result += posUI.posMenuUI.menu.menuOrdyConfig.discountPercentFirstOrdy;
					}
				}
			}
			if (null != order) {
				order.discountPercent = result;
			}
			posUI.cartUI.resetShowDiscountPercent(posUI);
		},

		resetShowDiscountPercent : function (posUI) {
			posUI.cartUI.cartContainer.showDiscount = false;
			if (posUI.cartUI.cart && (posUI.cartUI.cart.ordyItemList || posUI.cartUI.cart.comboOrdyItemList)) {
				if (posUI.cartUI.userIsPOSOperator) {
					posUI.cartUI.cartContainer.showDiscount = posUI.cartUI.cart.discountPercent && !posUI.cartUI.cart.fixedAmount;
				} else {
					posUI.cartUI.cartContainer.showDiscount = (posUI.cartUI.cart.ordyItemList.length > 0 || posUI.cartUI.cart.comboOrdyItemList.length > 0) && (posUI.cartUI.cart.discountPercent > 0);
				}
			}
		},
/*
		getDiscountPercent : function (posUI, order) {
			var result = 0;
			if (posUI.cartUI.cart && posUI.posMenuUI.menu && posUI.posMenuUI.menu.menuOrdyConfig) {
				if (posUI.cartUI.userIsPOSOperator) {
					result = order.discountPercent;
				} else {
					result += posUI.posMenuUI.menu.menuOrdyConfig.discountPercentOnlineOrdy;
					if (posUI.previousOrdersUI.previousOrders
							&& posUI.previousOrdersUI.previousOrders.items
							&& posUI.previousOrdersUI.previousOrders.items.length == 0) {
						result += posUI.posMenuUI.menu.menuOrdyConfig.discountPercentFirstOrdy;
					}
				}
			}
			return result;
		},
*/

		getDiscountAmount : function (posUI, order) {
			var result = 0;
			if (order) {
				if (order.discountPercent) {
					var discountPercent = order.discountPercent; //posUI.cartUI.getDiscountPercent(posUI, order);
					result = ((discountPercent / 100) * posUI.cartUI.getOrderRawAmount(posUI, order));
				}
			}
			return result;
		},
		getOrderRawAmount : function (posUI, order) {
			var result = 0;
			if (order) {
				result = (order.amount + order.deliveryCharge);
			}
			return result;
		},
		getAdjustmentAmount : function (posUI, order) {
			var result = 0;
			if (order) {
				if (order.fixedAmount) {
					result = (order.fixedAmount * 100) - (posUI.cartUI.getOrderRawAmount(posUI, order));
				}
			}
			return result;
		},
		getFinalTotalAmount : function (posUI, order) {
			var result = 0;
			if (order) {
				if (order.fixedAmount) {
					result = (order.fixedAmount * 100);
				} else {
					result = posUI.cartUI.getOrderRawAmount(posUI, order) - (posUI.cartUI.getDiscountAmount(posUI, order) + order.cashback);
				}
			}
			return result;
		},
		editTotal : function (posUI) {
			if (posUI.cartUI.userIsPOSOperator && posUI.cartUI.cart) {
				if (!posUI.cartUI.cart.fixTotalAmount) {
					posUI.cartUI.cart.fixTotalAmount = (posUI.cartUI.cart.amount + posUI.cartUI.cart.deliveryCharge) / 100;
				}
				if (!posUI.cartUI.cart.discountTotalAmount) {
					posUI.cartUI.cart.discountTotalAmount = 0;
				}
				if (!posUI.cartUI.cart.cashback) {
					posUI.cartUI.cart.cashback = 0;
				}
				this.modalInstance = $modal.open({
					templateUrl: 'posCartTotalModel.jspf',
					controller: 'POSCartTotalModelModalInstanceCtrl',
					resolve: {
						request : function () {
							return {
								posUI : posUI
							};
						}
					}
				});
			}
		},
		makeOrdyItemForSubmission : function (ordyItemTemp) {
			var result = {
				menuItemId:ordyItemTemp.menuItem.menuItemId,
				price:ordyItemTemp.menuItem.price,
				qty:ordyItemTemp.qty,
				note:ordyItemTemp.note,
				extraOptions:null,
				amount:ordyItemTemp.amount
    		};
			if (ordyItemTemp.extraOptions
					&& (ordyItemTemp.extraOptions.addOptions.length > 0 || ordyItemTemp.extraOptions.removeOptions.length > 0)) {
				result.extraOptions = {addOptions:null,removeOptions:null};
				if (ordyItemTemp.extraOptions.addOptions.length > 0) {
					result.extraOptions.addOptions = [];
					for (var i = 0; i < ordyItemTemp.extraOptions.addOptions.length; i++) {
						result.extraOptions.addOptions.push(
							{
								qty:ordyItemTemp.extraOptions.addOptions[i].qty,
								amount:ordyItemTemp.extraOptions.addOptions[i].amount,
								price:ordyItemTemp.extraOptions.addOptions[i].option.price,
								option:{extraOptionItemId:ordyItemTemp.extraOptions.addOptions[i].option.extraOptionItemId}
							}
						);
					}
				}
				if (ordyItemTemp.extraOptions.removeOptions.length > 0) {
					result.extraOptions.removeOptions = [];
					for (var i = 0; i < ordyItemTemp.extraOptions.removeOptions.length; i++) {
						result.extraOptions.removeOptions.push(
							{
								qty:ordyItemTemp.extraOptions.removeOptions[i].qty,
								amount:ordyItemTemp.extraOptions.removeOptions[i].amount,
								price:ordyItemTemp.extraOptions.removeOptions[i].option.price,
								option:{extraOptionItemId:ordyItemTemp.extraOptions.removeOptions[i].option.extraOptionItemId}
							}
						);
					}
				}
			}
    		return result;
		},
		submitOrderForCustomer : function (posUI) {
			posUI.cartUI.showOrderItems(posUI);
			if (confirm('Are you sure you wish to submit this order?')) {
				posUI.cartUI.submitOrder(posUI);
			}
		},
		submitOrder : function (posUI) {
			var cart = posUI.cartUI.cart;
    		var cartCopy = {
        		customer:{customerId:null},
    			amount:cart.amount,
    			shopId:posUI.shop.shopId,
				languageId:posUI.shop.languageTables[0].language.languageId,
				note:cart.note,
				menuId:cart.menu.menuId,
		    	onlinePaymentStripeToken:cart.onlinePaymentStripeToken,
		    	onlinePaymentPaypalNonce:cart.onlinePaymentPaypalNonce,
				deliveryTime:posUI.util.menu.getDefaultDeliveryTime(cart.menu, cart.deliverySuburb),
				deliveryAddress:null,
				deliverySuburb:null,
				deliveryCharge:0,
				customerName:cart.customerName,
				customerMobileNumber:cart.customerMobileNumber,
				customerEmail:cart.customerEmail,
				discountPercent:cart.discountPercent,
				cashback:cart.cashback,
				menuDiscountCoupon:cart.menuDiscountCoupon,
				fixedAmount:cart.fixedAmount,
				ordyItemList:[],
				comboOrdyItemList:posUI.cartUI.makeComboOrdyItemListForSubmission(posUI, cart.comboOrdyItemList)
    		};
    		for (var i = 0; i < cart.ordyItemList.length; i++) {
    			cartCopy.ordyItemList.push(posUI.cartUI.makeOrdyItemForSubmission(cart.ordyItemList[i]));
    		}

    		if (posUI.posCustomerSearchUI.customer) {
    			cartCopy.customer.customerId = posUI.posCustomerSearchUI.customer.customerId;
    		}
    		if (posUI.util.menu.isDeliveryMenu(cart.menu)) {
    			cartCopy.deliveryAddress = cart.deliveryAddress;
    			cartCopy.deliverySuburb = cart.deliverySuburb;
    			cartCopy.deliveryCharge = cart.deliveryCharge;
    		}

    		cartCopy.shopId = SHOP_ID;

    		var contactPhoneList = null;
    		if (posUI.posCustomerSearchUI.customer && posUI.posCustomerSearchUI.customer.contact) {
    			contactPhoneList = posUI.posCustomerSearchUI.customer.contact.phoneList;
    		}
    		posUI.cartUI.customerPhoneNumbers = posUI.util.theUserPhone.getCustomerPhoneNumbers(contactPhoneList, posUI.cartUI.cart.customerMobileNumber);

    		this.posService.submitOrdy({entity:cartCopy}, {posUI:posUI})
	    	.success(function (data, status, headers, config) {
	    		config.posUI.cartUI.cart.orderTime = data.entity.orderTime;
	    		config.posUI.cartUI.cart.ordyNumber = data.entity.ordyNumber;
	    		config.posUI.cartUI.cart.deliveryTime = data.entity.deliveryTime;
	    		config.posUI.posOrderSearchUI.order = config.posUI.cartUI.cart;
	    		
            	if (null == config.posUI.posMenuUI.menu.menuOrdyConfig.printCopiesCount) {
            		config.posUI.posMenuUI.menu.menuOrdyConfig.printCopiesCount = 1;
            	}
            	posUI.getTimeout()(function (){
		            if (config.posUI.cartUI.userIsPOSOperator) {
		            	window.print();
		            } else {
		            	config.posUI.previousOrdersUI.previousOrders = null;
		            	config.posUI.previousOrdersUI.continueAutoRefresh = true;
		            	posUI.cartUI.modalInstance = $modal.open({
		    				templateUrl: 'ordyPaymentModel.jspf',
		    				controller: 'OrderPaymentModalCtrl',
		    				resolve: {
		    					request : function () {return {posUI : config.posUI, ordyId : data.entity.ordyId};}
		    				}
		    			});
		            	posUI.cartUI.modalInstance.result.then(function () {
		    			}, function () {
		    			});
		            }
		            config.posUI.cartUI.cancel(config.posUI, false);
		            if (!config.posUI.cartUI.userIsPOSOperator) {
		            	config.posUI.previousOrdersUI.startPreviousOrdersMonitor(posUI); // TODO refresh my order log and show my order log
		            }
				}, 1000);
	    	})
	    	.error(function (error) {
	    		posUI.cartUI.modalInstance.close();
	            if (config.posUI.cartUI.userIsPOSOperator) {
	            	window.print();
	            }
	            alert('[submitOrdy] Request Failed: ' + error.message);}
	    	);
    	},
/*
    	payOnlineEway : function (ordyId, posUI) {
    		posUI.cartUI.showLoadingOnlinePaymentPageMessage = true;
    		this.posService.payOnlineEway(ordyId, {posUI:posUI})
	    	.success(function (data, status, headers, config) {
	    		if (data && data.sharedPaymentUrl) {
		    	    var eWAYConfig = {sharedPaymentUrl: data.sharedPaymentUrl};
			    	eCrypt.showModalPayment(eWAYConfig, function (result, transactionID, errors) {
		  	    	    	if (result == "Complete") {
		  	    	    		alert("Payment complete! eWAY Transaction ID: " + transactionID);
		  	    	    		posUI.cartUI.modalInstance.close();
		  	    	    		// update myordy about payment success
		  	    	    	} else if (result == "Error") {
		  	    	    		alert("There was a problem completing the payment: " + errors);
		  	    	    		posUI.cartUI.showLoadingOnlinePaymentPageMessage = false;
		  	    	      	}
			    	    }
			    	);
	    		} else {
	    	    	alert("Sorry, Online Payment is not available. Please try again later.");
	        		posUI.cartUI.showLoadingOnlinePaymentPageMessage = false;
	    		}
	    	})
	    	.error(function (error) {
	            alert('[payOnlineEway] Request Failed: ' + error.message);
	    		posUI.cartUI.showLoadingOnlinePaymentPageMessage = false;
	    	});
    	},
*/
    	getComboOrderItemPrice : function (posUI, comboMenuItem) {
    		return this.getComboOrderItemAmount(posUI, this.makeNewComboOrderItem(posUI, comboMenuItem, 1));
    	},
    	makeNewComboOrderItem : function (posUI, comboMenuItem, qty) {
	    	var result = {
	    		comboMenuItem : comboMenuItem,
	    		qty : qty,
	    		note : null,
	    		amount : 0,
				ordyItemsComboSub : [],
	    		orderItems : []
	    	};
	    	return result;
    	},    	
    	makeNewOrderItem : function (posUI, menuItem, parentMenuItem) {
	    	var result = {
	    		clickedMenuItem : menuItem,
	    		menuItem : posUI.util.menuItem.getDefaultProductMenuItem(menuItem),
	    		qty : 1,
	    		note : null,
	    		amount : 0,
	    		extraOptions : {addOptions:[],removeOptions:[]},
	    		parentMenuItem : parentMenuItem
	    	};
	    	result.amount = result.menuItem.price;
	    	return result;
    	},
		setComboOrderItemAmount : function (posUI, comboOrderItem) {
			comboOrderItem.amount = this.getComboOrderItemAmount(posUI, comboOrderItem);
			posUI.cartUI.resetCartAmount(posUI);
    	},
		getComboOrderItemAmount : function (posUI, comboOrderItem) {
			var result = 0;
			var getComboItemExtraAmount = function (code) {
				return posUI.util.orderItemOption.getComboItemExtraAmount(comboOrderItem, code);
			};
			var getComboItemSubExtraAmount = function (code, subItemCode) {
				return posUI.util.orderItemOption.getComboItemSubExtraAmount(posUI.util, comboOrderItem, code, subItemCode);
			};
			var getComboItemSubCountByTag = function (code) {
				return posUI.util.orderItemOption.getComboItemSubCountByTag(posUI.util, comboOrderItem, code);
			};
			var getComboItemSubItemCountByTag = function (code, subItemCode) {
				return posUI.util.orderItemOption.getComboItemSubItemCountByTag(posUI.util, comboOrderItem, code, subItemCode);
			};
			var getComboItemPrice = function (code) {
				return posUI.util.orderItemOption.getComboItemPrice(comboOrderItem, code);
			};
			var getComboItemCountByTag = function (code) {
				return posUI.util.orderItemOption.getComboItemCountByTag(comboOrderItem, code);
			};
			eval(
				' var qty = ' + comboOrderItem.qty + ';'
			+   ' result = ' + comboOrderItem.comboMenuItem.priceFormula + ';'
			);
			return result;
    	},
    	setOrderItemAmount : function (posUI, orderItem) {
			orderItem.amount = orderItem.qty * orderItem.menuItem.price;
			if (orderItem.extraOptions) {
				if (orderItem.extraOptions.addOptions) {
					for (var i = 0; i < orderItem.extraOptions.addOptions.length; i++) {
						var theOption = orderItem.extraOptions.addOptions[i];
						theOption.amount = (theOption.qty * orderItem.qty) * theOption.option.addPrice;
						orderItem.amount += theOption.amount;
					}
				}
				if (orderItem.extraOptions.removeOptions) {
					for (var i = 0; i < orderItem.extraOptions.removeOptions.length; i++) {
						var theOption = orderItem.extraOptions.removeOptions[i];
						theOption.amount = (theOption.qty * orderItem.qty) * theOption.option.removePrice;
						orderItem.amount += theOption.amount;
					}
				}
			}
			posUI.cartUI.resetCartAmount(posUI);
		},
    	resetCartAmount : function (posUI) {
    		var cart = posUI.cartUI.cart;
    		cart.amount = posUI.cartUI.calculateCartAmount(cart);
		},
    	calculateCartAmount : function (cart) {
    		var result = 0;
    		if (cart) {
    			for (var i = 0; i < cart.comboOrdyItemList.length; i++) {
    				result += cart.comboOrdyItemList[i].amount;
    			}
    			for (var i = 0; i < cart.ordyItemList.length; i++) {
    				result += cart.ordyItemList[i].amount;
    			}
    		}
//    		if (cart.deliveryCharge) {
//    			result += cart.deliveryCharge;
//    		}
    		return result;
		},
    	resetCart : function (posUI) {
   			//posUI.posMenuUI.resetMenu(posUI);
    		this.cart = {
				shop:{shopId:null},
				languageId:null,
				ordyItemList:[],
				comboOrdyItemList:[],
				note:null,
				menu:posUI.posMenuUI.menu,
				onlinePaymentStripeToken:null,
				onlinePaymentPaypalNonce:null,
				deliveryTime:null,
				deliveryAddress:null,
				deliverySuburb:null,
				deliveryCharge:null,
				customerName:null,
				customerMobileNumber:null,
				customerEmail:null,
				customer:null,
				cashback:0,
				amount:0
    		};
    		posUI.cartUI.setDiscountPercent(posUI, this.cart);
    		return this.cart;
		},
    	addComboItemToCart : function (posUI) {
    		var cart = posUI.cartUI.cart;
    		cart.comboOrdyItemList.push(posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem);
    		cart.amount += posUI.posMenuItemUI.posMenuComboItemUI.comboOrderItem.amount;
			posUI.cartUI.optomiseCart(posUI);
		},
    	getSpecialApplicableComboMenuItemsUniqueSorted : function (posUI, cartOrderItems, comboItemsCandidatesForSpecials) {
			var result = [];
			var applicableComboMenuItems = [];			
			for (var i = 0; i < cartOrderItems.length; i++) {
				if (cartOrderItems[i].qty > 0) {
					applicableComboMenuItems = applicableComboMenuItems.concat(
						posUI.util.menuItem.findComboMenuItemByTag(
							posUI.util.menuItem.findParentComboMenuItems(cartOrderItems[i].menuItem, posUI.posMenuUI.menu.menuItemComboList),
							"[-SPECIAL-]"
						)
					);
				}
			}
			for (var i = 0; i < comboItemsCandidatesForSpecials.length; i++) {
				if (comboItemsCandidatesForSpecials[i].qty > 0) {
					applicableComboMenuItems = applicableComboMenuItems.concat(
						posUI.util.menuItem.findComboMenuItemByTag(
							posUI.util.menuItem.findParentComboMenuItems(comboItemsCandidatesForSpecials[i].comboMenuItem, posUI.posMenuUI.menu.menuItemComboList),
							"[-SPECIAL-]"
						)
					);
				}
			}
			if (applicableComboMenuItems.length > 0) {
				var applicableComboMenuItemsUnique = posUI.util.tools.findUniqueItemsInArray(applicableComboMenuItems, 'menuItemComboId');
				
				var comboOrderItemsForSorting = [];
				for (var i = 0; i < applicableComboMenuItemsUnique.length; i++) {
					var emptyComboOrderItem = this.makeNewComboOrderItem(posUI, applicableComboMenuItemsUnique[i], 1);
					emptyComboOrderItem.amount = this.getComboOrderItemAmount(posUI, emptyComboOrderItem);
					comboOrderItemsForSorting.push(emptyComboOrderItem);
				}
				comboOrderItemsForSorting.sort(
					function (a, b) {
						return b.amount - a.amount;
					}
				);
				for (var i = 0; i < comboOrderItemsForSorting.length; i++) {
					result.push(comboOrderItemsForSorting[i].comboMenuItem);
				}
			}
			return result;
    	},
    	optomiseCart : function (posUI) {
			var optimisedCart = angular.copy(posUI.cartUI.cart);
			optimisedCart.ordyItemList = [];
			optimisedCart.comboOrdyItemList = this.optomiseCart_getCopyOfAllComboOrdyItemNotSpecials(posUI.util.tag, posUI.cartUI.cart);
			var cartOrderItems = this.optomiseCart_getCopyOfAllCartOrderItemsForOptomisation(posUI.util.tag, posUI.cartUI.cart);

			var applicableComboMenuItemsUniqueSorted = this.getSpecialApplicableComboMenuItemsUniqueSorted(posUI, cartOrderItems, optimisedCart.comboOrdyItemList);
			// TODO applicableComboMenuItemsUniqueSorted should include non special combo items 
			while (applicableComboMenuItemsUniqueSorted.length > 0) {
				var comboOrderItemsFound = [];
				for (var i = 0; i < applicableComboMenuItemsUniqueSorted.length; i++) {
					var candidate = {
						comboMenuItem : applicableComboMenuItemsUniqueSorted[i],
						comboOptionOrderItems : this.optomiseCart_makeCandidateComboOptionOrderItems(posUI.util.tag, applicableComboMenuItemsUniqueSorted[i], cartOrderItems, optimisedCart.comboOrdyItemList)
					};
					if (posUI.util.menuItem.isComboOptionOrderItemsQtyEnough(applicableComboMenuItemsUniqueSorted[i], candidate.comboOptionOrderItems)) {
						comboOrderItemsFound.push(candidate);
						break; // need only the top item rest to be rebuild again after taking away items in combo
					}
				}
				if (comboOrderItemsFound.length > 0) {
					for (var i = 0; i < comboOrderItemsFound.length; i++) {
				    	var comboOrderItemsExtractedFromBucket = this.optomiseCart_makeComboOrdyItemsFromBucket(posUI, comboOrderItemsFound[i].comboMenuItem, comboOrderItemsFound[i].comboOptionOrderItems);
				    	Array.prototype.push.apply(optimisedCart.comboOrdyItemList, comboOrderItemsExtractedFromBucket);
					}
					applicableComboMenuItemsUniqueSorted = this.getSpecialApplicableComboMenuItemsUniqueSorted(posUI, cartOrderItems, optimisedCart.comboOrdyItemList);
				} else {
					break;
				}
			}
			for (var coi = 0; coi < cartOrderItems.length; coi++) {
				if (cartOrderItems[coi].qty > 0) {
					posUI.cartUI.setOrderItemAmount(posUI, cartOrderItems[coi]);
					optimisedCart.ordyItemList.push(cartOrderItems[coi]);
				}
			}
			var comboOrdyItemList = [];
			for (var coi = 0; coi < optimisedCart.comboOrdyItemList.length; coi++) {
				if (optimisedCart.comboOrdyItemList[coi].qty > 0) {
					comboOrdyItemList.push(optimisedCart.comboOrdyItemList[coi]);
				}
			}
			optimisedCart.comboOrdyItemList = comboOrdyItemList;
			optimisedCart.amount = posUI.cartUI.calculateCartAmount(optimisedCart);
			if (optimisedCart.amount < posUI.cartUI.cart.amount) {
				posUI.cartUI.cart = optimisedCart;
			}
			posUI.cartUI.resetShowDiscountPercent(posUI);
    	},
    	optomiseCart_makeComboOrdyItemsFromBucket : function (posUI, comboMenuItem, orderItemsBucket) {
    		var result = [];
			while (posUI.util.menuItem.isComboOptionOrderItemsQtyEnough(comboMenuItem, orderItemsBucket)) {
				var comboOrderItemTemp = this.makeNewComboOrderItem(posUI, comboMenuItem, 1);
				for (var i = 0; i < comboMenuItem.comboOptionList.length; i++) {
					var qtyAddedTotal = 0;
					for (var j = 0; j < orderItemsBucket[i].orderItems.length; j++) {
						if (qtyAddedTotal >= comboMenuItem.comboOptionList[i].qtyRequired) {
							break;
						}
						var qtyAddedTemp = 0;
						var orderItemFromBucket = orderItemsBucket[i].orderItems[j];
						if (orderItemFromBucket.qty > 0) {
							var orderItemTemp = null;
							if ((qtyAddedTotal + orderItemFromBucket.qty) > comboMenuItem.comboOptionList[i].qtyRequired) {
								orderItemTemp = angular.copy(orderItemFromBucket);
								orderItemTemp.qty = comboMenuItem.comboOptionList[i].qtyRequired - qtyAddedTotal;
								posUI.cartUI.setOrderItemAmount(posUI, orderItemTemp);
								qtyAddedTemp = orderItemTemp.qty;
							} else {
								qtyAddedTemp = orderItemFromBucket.qty;
								orderItemTemp = angular.copy(orderItemFromBucket);
							}
							comboOrderItemTemp.orderItems.push({orderItem:orderItemTemp,qty:qtyAddedTemp});
							orderItemFromBucket.qty -= qtyAddedTemp;
							orderItemsBucket[i].qty -= qtyAddedTemp;
							qtyAddedTotal += qtyAddedTemp;
						}
					}
					for (var j = 0; j < orderItemsBucket[i].ordyItemsComboSub.length; j++) {
						if (qtyAddedTotal >= comboMenuItem.comboOptionList[i].qtyRequired) {
							break;
						}
						var qtyAddedTemp = 0;
						var orderItemComboSubFromBucket = orderItemsBucket[i].ordyItemsComboSub[j];
						if (orderItemComboSubFromBucket.qty > 0) {
							var orderItemComboSubTemp = null;
							if ((qtyAddedTotal + orderItemComboSubFromBucket.qty) > comboMenuItem.comboOptionList[i].qtyRequired) {
								orderItemComboSubTemp = angular.copy(orderItemComboSubFromBucket);
								orderItemComboSubTemp.qty = comboMenuItem.comboOptionList[i].qtyRequired - qtyAddedTotal;
								posUI.cartUI.setOrderItemAmount(posUI, orderItemComboSubTemp);
								qtyAddedTemp = orderItemComboSubTemp.qty;
							} else {
								qtyAddedTemp = orderItemComboSubFromBucket.qty;
								orderItemComboSubTemp = angular.copy(orderItemComboSubFromBucket);
							}
							comboOrderItemTemp.ordyItemsComboSub.push({orderItemComboSub:orderItemComboSubTemp,qty:qtyAddedTemp});
							orderItemComboSubFromBucket.qty -= qtyAddedTemp;
							orderItemsBucket[i].qty -= qtyAddedTemp;
							qtyAddedTotal += qtyAddedTemp;
						}
					}
				}
				comboOrderItemTemp.amount = this.getComboOrderItemAmount(posUI, comboOrderItemTemp);
				result.push(comboOrderItemTemp);
			}
			return result;
    	},
    	optomiseCart_makeCandidateComboOptionOrderItems : function (tagUtils, comboMenuItem, cartOrderItems, comboItemsCandidatesForSpecials) {
			var result = [];
			for (var i = 0; i < comboMenuItem.comboOptionList.length; i++) {
				result[i] = {
					comboOption : comboMenuItem.comboOptionList[i],
					orderItems : [],
					ordyItemsComboSub : [],
					qty : 0
				};
				for (var j = 0; j < cartOrderItems.length; j++) {
					if (cartOrderItems[j].qty > 0
							&& tagUtils.isTagInListById(cartOrderItems[j].menuItem.tagList, comboMenuItem.comboOptionList[i].menuItemTag)) {
						result[i].orderItems.push(cartOrderItems[j]);
						result[i].qty += cartOrderItems[j].qty;
					}
				}
				for (var j = 0; j < comboItemsCandidatesForSpecials.length; j++) {
					if (comboItemsCandidatesForSpecials[j].qty > 0
							&& tagUtils.isTagInListById(comboItemsCandidatesForSpecials[j].comboMenuItem.tagList, comboMenuItem.comboOptionList[i].menuItemTag)) {
/*
						result[i].ordyItemsComboSub.push({
						    qty:comboItemsCandidatesForSpecials[j].qty,
						    note:null,
						    menuItemComboId:comboItemsCandidatesForSpecials[j].comboMenuItem.menuItemComboId,
						    orderItems:comboItemsCandidatesForSpecials[j].orderItems,
						    ordyItemCombo:null,
						    status:null
						});
*/
						result[i].ordyItemsComboSub.push(comboItemsCandidatesForSpecials[j]);
						result[i].qty += comboItemsCandidatesForSpecials[j].qty;
					}
				}
				if (result[i].qty < comboMenuItem.comboOptionList[i].qtyRequired) {
					// no point checking the next comboOption
					break;
				}
			}
			return result;
    	},
/*
    	optomiseCart : function (posUI, cartOrderItems) {
			var cart = posUI.cartUI.cart;
			var cartOrderItems = this.getCopyOfAllCartOrderItems(cart);
			var applicableComboMenuItemsUniqueSorted = this.getSpecialApplicableComboMenuItemsUniqueSorted(posUI, cartOrderItems);

			for (var i = 0; i < applicableComboMenuItemsUniqueSorted.length; i++) { // for all applicable combo menu items, sorted by price, highest amount first
				var comboMenuItem = applicableComboMenuItemsUniqueSorted[i];
				var comboOptionOrderIt	ems = [];
		
				for (var m = 0; m < comboMenuItem.comboOptionList.length; m++) { // make comboOptionOrderItem by checking all items in cart, 
					comboOptionOrderItems[m] = {
						comboOption : comboMenuItem.comboOptionList[m],
						orderItems : [],
						qty : 0
					};
					for (var j = 0; j < cartOrderItems.length; j++) {
						if (cartOrderItems[j].qty > 0) {
							for (var k = 0; k < cartOrderItems[j].menuItem.tagList.length; k++) {
								if (cartOrderItems[j].menuItem.tagList[k].tagId == comboMenuItem.comboOptionList[m].menuItemTag.tagId) {
									comboOptionOrderItems[m].orderItems.push(cartOrderItems[j]);
									comboOptionOrderItems[m].qty += cartOrderItems[j].qty;
								}
							}
						}
					}
					if (comboOptionOrderItems[m].qty < comboMenuItem.comboOptionList[m].qtyRequired) {
						break;
					}
				}
				var optimisedCart = angular.copy(posUI.cartUI.cart);
				optimisedCart.ordyItemList = [];
				optimisedCart.comboOrdyItemList = [];
		
				while (posUI.util.menuItem.isComboOptionOrderItemsQtyEnough(comboMenuItem, comboOptionOrderItems)) {
					var comboOrderItemTemp = this.makeNewComboOrderItem(posUI, comboMenuItem, 1);
					for (var m = 0; m < comboMenuItem.comboOptionList.length; m++) {
						var comboOptionOrderItemsQtyAddedTotal = 0;
						for (var n = 0; n < comboOptionOrderItems[m].orderItems.length; n++) {
							var comboOptionOrderItemsQtyAddedTemp = 0;
							if (comboOptionOrderItemsQtyAddedTotal >= comboMenuItem.comboOptionList[m].qtyRequired) {
								break;
							}
							if (comboOptionOrderItems[m].orderItems[n].qty > 0) {
								var orderItemTemp = null;
								if ((comboOptionOrderItemsQtyAddedTotal + comboOptionOrderItems[m].orderItems[n].qty) > comboMenuItem.comboOptionList[m].qtyRequired) {
									orderItemTemp = angular.copy(comboOptionOrderItems[m].orderItems[n]);
									orderItemTemp.qty = comboMenuItem.comboOptionList[m].qtyRequired - comboOptionOrderItemsQtyAddedTotal;
									posUI.cartUI.setOrderItemAmount(posUI, orderItemTemp);
									comboOptionOrderItemsQtyAddedTemp = orderItemTemp.qty;
								} else {
									comboOptionOrderItemsQtyAddedTemp = comboOptionOrderItems[m].orderItems[n].qty;
									orderItemTemp = angular.copy(comboOptionOrderItems[m].orderItems[n]);
								}
								comboOrderItemTemp.orderItems.push({orderItem:orderItemTemp,qty:comboOptionOrderItemsQtyAddedTemp});
								comboOptionOrderItems[m].orderItems[n].qty -= comboOptionOrderItemsQtyAddedTemp;
								comboOptionOrderItems[m].qty -= comboOptionOrderItemsQtyAddedTemp;
								comboOptionOrderItemsQtyAddedTotal += comboOptionOrderItemsQtyAddedTemp;
							}
						}
					}
					comboOrderItemTemp.amount = this.getComboOrderItemAmount(posUI, comboOrderItemTemp);
					optimisedCart.comboOrdyItemList.push(comboOrderItemTemp);
				}
				for (var coi = 0; coi < cartOrderItems.length; coi++) {
					if (cartOrderItems[coi].qty > 0) {
						posUI.cartUI.setOrderItemAmount(posUI, cartOrderItems[coi]);
						optimisedCart.ordyItemList.push(cartOrderItems[coi]);
					}
				}
				optimisedCart.amount = posUI.cartUI.calculateCartAmount(optimisedCart);
//console.log(optimisedCart);
//console.log(posUI.cartUI.cart);
				if (optimisedCart.amount < posUI.cartUI.cart.amount) {
					posUI.cartUI.cart = optimisedCart;
					break;
				}
			}
		},
*/
    	optomiseCart_getCopyOfAllComboOrdyItemNotSpecials : function (tagUtils, cart) {
		    var result = [];
		    if (cart.comboOrdyItemList) {
		    	for (var i = 0; i < cart.comboOrdyItemList.length; i++) {
	    			if (tagUtils.isTagInListByCode(cart.comboOrdyItemList[i].comboMenuItem.tagList, '[-SPECIAL-]')) {
	    				if (cart.comboOrdyItemList[i].ordyItemsComboSub) {
	    					for (var j = 0; j < cart.comboOrdyItemList[i].ordyItemsComboSub.length; j++) {
	    	    				if (cart.comboOrdyItemList[i].ordyItemsComboSub[j].orderItemComboSub.orderItems) {
	    	    					result.push(angular.copy(cart.comboOrdyItemList[i].ordyItemsComboSub[j].orderItemComboSub));
	    	    				}
	    					}
	    				}
	    			} else {
	    				if (cart.comboOrdyItemList[i].orderItems) {
	    					result.push(angular.copy(cart.comboOrdyItemList[i]));
	    				}
	    			}
		    	}
		    }

/*
		    if (cart.comboOrdyItemList) {
		    	for (var i = 0; i < cart.comboOrdyItemList.length; i++) {
		    		if (cart.comboOrdyItemList[i].orderItems) {
		    			if (tagUtils.isTagInListByCode(cart.comboOrdyItemList[i].comboMenuItem.tagList, '[-SPECIAL-]')) {
		    				for (var j = 0; j < cart.comboOrdyItemList[i].orderItems.length; j++) {
		    					var orderItem = angular.copy(cart.comboOrdyItemList[i].orderItems[j].orderItem);
		    					orderItem.qty = orderItem.qty * cart.comboOrdyItemList[i].qty;
		    					result.push(orderItem);
		    				}
		    			}
		    		}
		    	}
		    }
*/
		    return result;
		},
/*
		optomiseCart_getCopyOfNoneSpecialComboItemsCandidatesForSpecials : function (posUI, tagUtils, cart) {
		    var result = [];
		    if (cart.comboOrdyItemList) {
		    	for (var i = 0; i < cart.comboOrdyItemList.length; i++) {
		    		if (cart.comboOrdyItemList[i].orderItems) {
		    			if (!tagUtils.isTagInListByCode(cart.comboOrdyItemList[i].comboMenuItem.tagList, '[-SPECIAL-]')) {
		    				result.push(angular.copy(cart.comboOrdyItemList[i]));
		    			}
		    		}
		    	}
		    }
		    return result;
		},
*/
    	optomiseCart_getCopyOfAllCartOrderItemsForOptomisation : function (tagUtils, cart) {
		    var result = [];
		    if (cart.ordyItemList) {
		    	for (var i = 0; i < cart.ordyItemList.length; i++) {
		    		result.push(angular.copy(cart.ordyItemList[i]));
		    	}
		    }
		    if (cart.comboOrdyItemList) {
		    	for (var i = 0; i < cart.comboOrdyItemList.length; i++) {
		    		if (cart.comboOrdyItemList[i].orderItems) {
		    			if (tagUtils.isTagInListByCode(cart.comboOrdyItemList[i].comboMenuItem.tagList, '[-SPECIAL-]')) {
		    				for (var j = 0; j < cart.comboOrdyItemList[i].orderItems.length; j++) {
		    					var orderItem = angular.copy(cart.comboOrdyItemList[i].orderItems[j].orderItem);
//		    					orderItem.qty = orderItem.qty * cart.comboOrdyItemList[i].qty;
		    					orderItem.qty = cart.comboOrdyItemList[i].orderItems[j].qty * cart.comboOrdyItemList[i].qty;
		    					result.push(orderItem);
		    				}
		    			}
		    		}
		    	}
		    }
		    return result;
		},
    	addToCart : function (posUI) {
    		if (!posUI.cartUI.cart) {
    			posUI.cartUI.cart = posUI.cartUI.resetCart(posUI);
    		}
    		var cart = posUI.cartUI.cart;
    		// TODO also do the above when items are updated in cart
    		cart.ordyItemList.push(posUI.posMenuItemUI.orderItem);
    		cart.amount += posUI.posMenuItemUI.orderItem.amount;

    		this.optomiseCart(posUI);
    	},
	    openCashDrawer : function (posUI) {
	        var html = '<html>';
	        html += '<head>';
	        html +=   '<style type="text/css">body {font-family:Arial,Helvetica,sans-serif,Geneva,Tahoma,Verdana;font-size:9pt;}\ntd{font-size:9pt;}</style>';
	        html += '</head>';
	        html += '<body style="margin:0px;padding:0px;">';
	        html += '<div>Cash drawer opened</div>';
	        html += '</body></html>';
	        var printWin = window.open("", "MYORDY_OPEN_CASH_DRAWER_WINDOW");
	        printWin.document.open();
	        printWin.document.write(html);
	        printWin.document.close();
	        printWin.print();
	        printWin.close();
	    }
    };
}]);



MYORDY_POS_APP.factory('posUI', ['util', 'posService', 'posMenuUI', 'posMenuItemUI', 'cartUI', 'posOrderSearchUI', 'posCustomerSearchUI', 'loginUI', 'previousOrdersUI', 'ordyInboxUI',
function(utilArg, posServiceArg, posMenuUIArg, posMenuItemUIArg, cartUIArg, posOrderSearchUIArg, posCustomerSearchUIArg, loginUIArg, previousOrdersUIArg, ordyInboxUIArg) {
    return {
    	util : utilArg,
    	posService : posServiceArg,
    	posMenuUI : posMenuUIArg,
    	posMenuItemUI : posMenuItemUIArg,
    	cartUI : cartUIArg,
    	posOrderSearchUI : posOrderSearchUIArg,
    	posCustomerSearchUI : posCustomerSearchUIArg,
    	previousOrdersUI : previousOrdersUIArg,
    	loginUI : loginUIArg,
    	ordyInboxUI : ordyInboxUIArg,

    	container : {showMenuSelect:false,showMenu:false},
    	tabsetTabsContainer : {showOrder:false,showCustomer:false,showMyOrderLog:false,showOrderInbox:false},
    	rootContainer : {showHome:true, showMenuItem:false, showSpecials:false, showMenuItemSecondLevel:false},
		cookies : null,
    	shop : null,
    	scope : null,
    	shopLanguageTable : null,
    	referenceData : null,
    	onlinePayment : {
    		stripe:{publishableKey:'',cardElementId:null,errorElementId:null,stripeInstance:null,cardElement:null,onCreateToken:null,posUI:null},
    		paypal:{clientToken:'',buttonElementId:null,errorElementId:null,env:null,getPayment:null,onAuthorize:null,posUI:null}
    	},

    	setupUser : function () {
			this.loginUI.setupUser(this);
			var dusCookie = this.cookies.get('dus');
			if (!dusCookie) {
    			var now = new Date();
    		    var exp = new Date(now.getFullYear()+1, now.getMonth(), now.getDate());
    			this.cookies.put('dus', DUS, {
	    		  expires: exp
	    		});
			}

    	},
    	onLoginResponseChange : function (loginResponse) {
    		this.posCustomerSearchUI.onLoginResponseChange(this, loginResponse);
    		this.posOrderSearchUI.onLoginResponseChange(this, loginResponse);
    		this.cartUI.onLoginResponseChange(this, loginResponse);
    		this.previousOrdersUI.onLoginResponseChange(this, loginResponse);
    	},
    	openShop : function (shopId) {
        	this.posService.getShop(shopId, this)
    		.success(function (data, status, headers, config) {
           		var posUI = config.caller;
           		posUI.shop = data;
           		posUI.shopLanguageTable = posUI.shop.languageTables[0].languageTable;
           		posUI.posMenuUI.onShopLoad(posUI);
           		posUI.cartUI.initCartUI(posUI);

           		if (posUI.onlinePayment.stripe.publishableKey) {
           			posUI.onlinePayment.stripe.posUI = posUI;
           			posUI.onlinePayment.stripe.cardElementId = 'myordy-stripe-card-element';
           			posUI.onlinePayment.stripe.errorElementId = 'myordy-stripe-card-errors';
           			posUI.onlinePayment.stripe.onCreateToken = posUI.cartUI.onStripeCreateToken;
           			posUI.util.onlinePayment.stripe.setupCardInput(posUI.onlinePayment.stripe);
           		}
           		if (posUI.onlinePayment.paypal.clientToken) {
           			posUI.onlinePayment.paypal.posUI = posUI;
           			posUI.onlinePayment.paypal.buttonElementId = 'myordy-paypal-button';
           			posUI.onlinePayment.paypal.errorElementId = 'myordy-paypal-errors';
           			posUI.onlinePayment.paypal.getPayment = posUI.cartUI.onPaypalGetPayment;
           			posUI.onlinePayment.paypal.onAuthorize = posUI.cartUI.onPaypalAuthorize;
           			posUI.util.onlinePayment.paypal.setupPaypalButton(posUI.onlinePayment.paypal);
           		}

    		})
    		.error(function (error) {alert('Request Failed: ' + error.message);});

        	this.posService.getReferenceData({posUI:this})
	            .success(function (referenceData, status, headers, config) {config.posUI.referenceData = referenceData;})
	            .error(function (error) {alert('[getReferenceData] Request Failed: ' + error.message);});

    	},
	    hideAllPOSRootContainers : function () {
			for(var key in this.rootContainer) {
			    this.rootContainer[key] = false;
			}
		},
	    hideAllPOSContainers : function () {
			for(var key in this.container) {
			    this.container[key] = false;
			}
		}
    };
}]);


MYORDY_POS_APP.controller('MYORDY_POS_APP_CTRL', ['$scope', '$modal', '$window', '$cookies', '$timeout', '$interval', 'blockUI', 'posUI',
function ($scope, $modal, $window, $cookies, $timeout, $interval, blockUI, posUIArg) {
	$scope.sys = SYS;
	$scope.pageLoadServerTime = PAGE_LOAD_SERVER_TIME;
	$scope.contextPath = CONTEXT_PATH;
	$scope.userIpAddr = USER_IP_ADDR;
	$scope.posUI = posUIArg;
	$scope.posUI.cookies = $cookies;
	$scope.posUI.blockUI = blockUI;

	$scope.posUI.onlinePayment.stripe.publishableKey = $scope.posUI.util.tools.trim(STRIPE_MYORDY.publishableKey);
	$scope.posUI.onlinePayment.paypal.clientToken = $scope.posUI.util.tools.trim(PAYPAL_MYORDY.clientToken);

	$scope.posUI.getScope = function() {
		return $scope;
	}
	$scope.posUI.getTimeout = function() {
		return $timeout;
	}
	$scope.posUI.getInterval = function() {
		return $interval;
	}

	$scope.posUI.openShop(SHOP_ID);
	$scope.posUI.setupUser();

	//console.log($window.navigator.language);
	//console.log($window.navigator.userLanguage);

//	console.log(posUI.cookies.get('plt'));

//	posUIArg.trustSrc = function(src) {
//		return $sce.trustAsResourceUrl(src);
//	}

//	$scope.$on("MENU_CHANGED" ,function(event,menu){ 
//		  $scope.posUI.posMenuUI.menu = menu;
//	      console.log(menu);
//	  });
	//$cookies.put('obj', 'JB 123');
//	console.log($cookies);
//	console.log($cookies.getAll());
//	console.log($cookieStore);
	

//	$scope.$watch('posUI.posMenuUI.menu', function(newValue, oldValue) {
//		//alert(new Date());
//console.log(oldValue);
//console.log(newValue);
//		$scope.posUI.hideAllPOSContainers();
//		$scope.posUI.container.showMenu = true;
//		$scope.posUI.posMenuUI.menu = newValue;
//		//$scope.shopUI.menuItemUI.onMenuItemChange(newValue, oldValue);
//	});

}
]);

