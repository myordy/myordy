var MYORDY_ADMIN_APP = makeMyOrdyAngularApp('MYORDY_ADMIN_APP');

var ModalInstanceCtrl = MYORDY_ADMIN_APP.controller('MenuItemModalInstanceCtrl', function ($scope, $modalInstance, request) {
	$scope.request = request;
	$scope.ok = function () {
		$modalInstance.close();
	};
	$scope.cancel = function () {
		$modalInstance.dismiss();
	};
	$scope.goBackCategory = function () {
		$scope.request.shopUI.categoryUI.goBack($scope.request.shopUI, true, null);
	};
	$scope.goBackProduct = function () {
		$scope.request.shopUI.productUI.goBack($scope.request.shopUI, true, null);
	};
});

angular.module('MYORDY_ADMIN_APP').factory('adminService', ['$http', function($http) {
	return {
		contextPath:CONTEXT_PATH,
		urlBase : '/admin-rest',
		getShops : function (caller) {
		    return $http.get(this.contextPath + this.urlBase + '/shop', {caller:caller});
		},
		getShop : function (id, caller) {
		    return $http.get(this.contextPath + this.urlBase + '/shop/' + id, {caller:caller});
		},
		insertShop : function (shop, caller) {
		    return $http.post(this.contextPath + this.urlBase + '/shop', shop, {caller:caller});
		},
		updateShop : function (shopDTO, caller) {
		    return $http.put(this.contextPath + this.urlBase + '/shop', shopDTO, {caller:caller})
		},
		insertMenu : function (shopId, menuDTO, config) {
		    return $http.post(this.contextPath + this.urlBase + '/menu/' + shopId, menuDTO, config);
		},
		updateMenu : function (shopId, menuDTO, config) {
		    return $http.put(this.contextPath + this.urlBase + '/menu/' + shopId, menuDTO, config)
		},
		insertCategory : function (shopId, dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/category/' + shopId, dto, config);
		},
		updateCategory : function (shopId, dto, config) {
		    return $http.put(this.contextPath + this.urlBase + '/category/' + shopId, dto, config)
		},
		insertProduct : function (shopId, dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/product/' + shopId, dto, config);
		},
		updateProduct : function (shopId, dto, config) {
		    return $http.put(this.contextPath + this.urlBase + '/product/' + shopId, dto, config)
		},
		insertMenuItem : function (dto, config) {
		    return $http.post(this.contextPath + this.urlBase + '/menu-item', dto, config);
		},
		updateMenuItem : function (dto, config) {
		    return $http.put(this.contextPath + this.urlBase + '/menu-item', dto, config)
		},
		getReferenceData : function () {
		    return $http.get(this.contextPath + this.urlBase + '/referenceData')
		}
	};
}]);
/*
angular.module('MYORDY_ADMIN_APP').factory('util', [function() {
	return {
		languageTable : {
			copyMessages : function (shopUI, messages) {
				for (var i = 0; i < messages.length; i++) {
					shopUI.shopLanguageTable[messages[i].code] = messages[i].message;
				}
				shopUI.util.menu.clearLanguageTable(shopUI.shopLanguageTable);
				shopUI.shopLanguageTableOriginal = shopUI.shopLanguageTable;
			}
		},
    	shop : {
    		getNameCode : function (shop) {
    			if (shop && shop.shopId) {
    				return 'shopName' + shop.shopId;
    			}
				return 'shopName_NO_ID';
    		}//,
//    		clearLanguageTable : function (shopLanguageTable) {
//    			shopLanguageTable[this.getNameCode(null)] = '';
//    		}
    	},
    	nav : {
    		goBack : function (shopUI, backFunctions, config) {
    			if (backFunctions && backFunctions.length > 0) {
    				var backFunction = backFunctions.pop();
    				backFunction(shopUI, config);
    			}
    		}
    	},
    	menuItem : {
    		getMenuItemName : function (shopUI, menuItem) {
    			var result = null;
    			if (menuItem) {
    				if (menuItem.category) {
    					result = shopUI.shopLanguageTable[shopUI.util.menu.getCategoryNameCode(menuItem.category)];
    				} else if (menuItem.product) {
    					result = shopUI.shopLanguageTable[shopUI.util.menu.getProductNameFullCode(menuItem.product)];
    				}
    			}
    			return result;
    		},
    		insertMenuItem : function (menuItems, menuItem) {
    			for (var i = 0; i < menuItems.length; i++) {
    				if (menuItem.menuItemId == menuItems[i].menuItemId) {
    					menuItems.splice(i, 1);
    					break;
    				}
    			}
    			var menuItemsLengthBeforeInsert = menuItems.length;
    			for (var i = 0; i < menuItems.length; i++) {
    				if (menuItem.previousMenuItemId == menuItems[i].previousMenuItemId) {
    					menuItems[i].previousMenuItemId = menuItem.menuItemId;
    					menuItems.splice(i, 0, menuItem);
    					break;
    				}
    			}
    			if (menuItemsLengthBeforeInsert == menuItems.length) {
    				menuItems.push(menuItem);
    			}
    		},
    		getLastMenuItemId : function (menuItemList) {
    			var result = null;
    			if (menuItemList) {
    				result = menuItemList[menuItemList.length - 1].menuItemId;
    			}
    			return result;
    		}
    	},
    	menu : {
    		getNameCode : function (menu) {
    			if (menu && menu.menuId) {
    				return "menu" + menu.menuId + "N";
    			}
				return 'menu_NO_ID_N';
    		},
    		getProductNameFullCode : function (product) {
    			if (product && product.productId) {
    	    		return "prd" + product.productId + "NF";
    			}
	    		return 'prd_NO_ID_NF';
    		},
    		getProductNameCode : function (product) {
    			if (product && product.productId) {
    	    		return "prd" + product.productId + "N";
    			}
	    		return 'prd_NO_ID_N';
    		},
    		getProductDescriptionCode : function (product) {
    			if (product && product.productId) {
    	    		return "prd" + product.productId + "D";
    			}
	    		return 'prd_NO_ID_D';
    		},
    		getCategoryNameCode : function (category) {
    			if (category && category.categoryId) {
    	    		return "cat" + category.categoryId + "N";
    			}
	    		return 'cat_NO_ID_N';
    		},
    		getCategoryDescriptionCode : function (category) {
    			if (category && category.categoryId) {
    				return "cat" + category.categoryId + "D";
    			}
	    		return 'cat_NO_ID_D';
    		},
    		clearLanguageTable : function (shopLanguageTable) {
    			shopLanguageTable[this.getNameCode(null)] = '';
    			shopLanguageTable[this.getCategoryNameCode(null)] = '';
    			shopLanguageTable[this.getCategoryDescriptionCode(null)] = '';
    			shopLanguageTable[this.getProductNameFullCode(null)] = '';
    			shopLanguageTable[this.getProductNameCode(null)] = '';
    			shopLanguageTable[this.getProductDescriptionCode(null)] = '';
    		}
    	}
	};
}]);
*/
angular.module('MYORDY_ADMIN_APP').factory('menuItemUI', ['adminService', '$modal', function(adminServiceArg, $modal) {
	return {
		adminService : adminServiceArg,
		container : {showDetail:false, showCreate:false},
		menuItem : null,
		menuItemOriginal : null,
		siblingMenuItems : null,
		parentMenuItem : null,
		parentMenuItemId : null,
		menuItemType : null,
		menu : null,
		menuId : null,
		disableCategory : false,
		disableProduct : false,
		modalInstance : null,
		priceFormatted : 0.00,

		onMenuItemChange : function (newValue, oldValue) {
			this.priceFormatted = 0.00;
			if (newValue && newValue.product) {
				this.priceFormatted = newValue.price / 100;
			}
	    },
		backToMenuItemDetails : function (shopUI) {
			shopUI.hideAllMenuItemContainers();
			shopUI.menuItemUI.container.showDetail = true;
	    },
		backToCreateMenuItemForm : function (shopUI, config) {
			shopUI.hideAllMenuItemContainers();
			shopUI.menuItemUI.container.showCreate = true;
			if (config) {
				if (config.product) {
					shopUI.menuItemUI.menuItem.product = config.product;
				}
				if (config.category) {
					shopUI.menuItemUI.menuItem.category = config.category;
				}
			}
	    },
	    hideAllContainers : function () {
			for(var key in this.container) {
			    this.container[key] = false;
			}
	    },
	    showCreateMenuItemForm : function (shopUI, menu, parentMenuItem, disableCategory, disableProduct) {
	    	if (this.modalInstance) {
	    		this.modalInstance.close();
	    	}
	    	this.disableCategory = disableCategory;
			this.disableProduct = disableProduct;
			this.parentMenuItem = parentMenuItem;
			this.menu = menu;
			if (!this.disableProduct) {
				this.menuItemType = 'product';
			}
			if (!this.disableCategory) {
				this.menuItemType = 'category';
			}
			this.menuItem = {menuItemId:null,category:{categoryId:null,status:'ACTIVE'},product:{productId:null,status:'ACTIVE'},status:'ACTIVE'};
			if (menu) {
				this.siblingMenuItems = menu.menuItems;
				this.menuId = menu.menuId;
			} else if (parentMenuItem) {
				this.siblingMenuItems = parentMenuItem.menuItems;
				this.parentMenuItemId = parentMenuItem.menuItemId;
			}
			if (this.siblingMenuItems && this.siblingMenuItems.length > 0) {
				this.menuItem.previousMenuItemId = this.siblingMenuItems[this.siblingMenuItems.length - 1].menuItemId;
			}
			this.hideAllContainers();
			this.container.showCreate = true;

			this.modalInstance = $modal.open({
				templateUrl: 'menuItemModel.jspf',
				controller: 'MenuItemModalInstanceCtrl',
				resolve: {
					request : function () {
						return {
							shopUI : shopUI,
							menu : menu,
							parentMenuItem : parentMenuItem
						};
					}
				}
			});
	    },
	    showMenuItemInModelBox : function (shopUI, menu, parentMenuItem, menuItem) {
	    	if (this.modalInstance) {
	    		this.modalInstance.close();
	    	}
	    	this.disableCategory = true;
			this.disableProduct = true;
			this.parentMenuItem = parentMenuItem;
			this.menu = menu;
			this.menuItemOriginal = menuItem;
			this.menuItem = angular.copy(menuItem);
			shopUI.setupForUpdate();
			if (parentMenuItem) {
				this.parentMenuItemId = parentMenuItem.menuItemId;
			}
			if (menu) {
				this.siblingMenuItems = menu.menuItems;
				this.menuId = menu.menuId;
			} else if (parentMenuItem) {
				this.siblingMenuItems = parentMenuItem.menuItems;
			}
			this.hideAllContainers();
			this.container.showDetail = true;
			this.modalInstance = $modal.open({
				templateUrl: 'menuItemModel.jspf',
				controller: 'MenuItemModalInstanceCtrl',
				resolve: {
					request : function () {
						return {
							shopUI : shopUI,
							menu : menu,
							parentMenuItem : parentMenuItem
						};
					}
				}
			});
			this.modalInstance.result.then(function () {
			}, function () {
				this.menuItem = this.menuItemOriginal;
				shopUI.rollbackUpdate();
			});
	    },
	    createMenuItem : function (shopUI) {
	    	if (shopUI.menuItemUI.menuItemType == 'product') {
	    		this.menuItem.category = null;
	    	} else if (shopUI.menuItemUI.menuItemType == 'category') {
	    		this.menuItem.product = null;
	    	}
	    	this.adminService.insertMenuItem(
	    		{entity:this.menuItem,relatedEntityRef:{parentMenuItemId:this.parentMenuItemId,menuId:this.menuId}},
	    		{shopUI:shopUI}
	    	)
	    	.success(function (data, status, headers, config) {
	    		if (config.shopUI.menuItemUI.menu) {
	    			if (!config.shopUI.menuUI.menu.menuItems) {
	    				config.shopUI.menuUI.menu.menuItems = [];
	    			}
	    			config.shopUI.menuItemUI.insertMenuItem(config.shopUI.menuUI.menu.menuItems, data.entity);
	    		} else if (config.shopUI.menuItemUI.parentMenuItem) {
	    			if (!config.shopUI.menuItemUI.parentMenuItem.menuItems) {
	    				config.shopUI.menuItemUI.parentMenuItem.menuItems = [];
	    			}
	    			config.shopUI.menuItemUI.insertMenuItem(config.shopUI.menuItemUI.parentMenuItem.menuItems, data.entity);
	    		}
	    		config.shopUI.menuItemUI.modalInstance.close();
	    	})
	    	.error(function (error) {alert('[createMenuItem] Request Failed: ' + error.message);});
        },
	    updateMenuItem : function (shopUI, menuItemWithId) {
	    	this.adminService.updateMenuItem(
	    		{entity:menuItemWithId,relatedEntityRef:{parentMenuItemId:this.parentMenuItemId,menuId:this.menuId}},
	    		{shopUI:shopUI})
	    	.success(function (data, status, headers, config) {
	    		if (!config.shopUI.menuUI.menu.menuItems) {
	    			config.shopUI.menuUI.menu.menuItems = [];
	    		}
	    		config.shopUI.menuItemUI.insertMenuItem(shopUI.menuItemUI.siblingMenuItems, data.entity);
	    		config.shopUI.menuItemUI.modalInstance.close();
	    	})
	    	.error(function (error) {alert('[updateMenuItem] Request Failed: ' + error.message);});
		},
		insertMenuItem : function (menuItems, menuItem) {
			for (var i = 0; i < menuItems.length; i++) {
				if (menuItem.menuItemId == menuItems[i].menuItemId) {
					menuItems.splice(i, 1);
					break;
				}
			}
			var menuItemsLengthBeforeInsert = menuItems.length;
			for (var i = 0; i < menuItems.length; i++) {
				if (menuItem.previousMenuItemId == menuItems[i].previousMenuItemId) {
					menuItems[i].previousMenuItemId = menuItem.menuItemId;
					menuItems.splice(i, 0, menuItem);
					break;
				}
			}
			if (menuItemsLengthBeforeInsert == menuItems.length) {
				menuItems.push(menuItem);
			}
		}
	};
}]);

angular.module('MYORDY_ADMIN_APP').factory('productUI', [function() {
	return {
		container : {showDetail:false,showList:false},
		product : null,
		productOriginal : null,
		backFunctions : [],
		goBack : function (shopUI, rollbackUpdate, config) {
			if (rollbackUpdate) {
				this.product = this.productOriginal;
				shopUI.rollbackUpdate();
			}
			shopUI.util.nav.goBack(shopUI, shopUI.productUI.backFunctions, config);
		},
		backToManageProduct : function (shopUI) {
			shopUI.hideAllMenuItemContainers();
			shopUI.productUI.container.showList = true;
		},
		showList : function (shopUI, backFunction) {
			this.backFunctions.push(backFunction);
			this.backToManageProduct(shopUI);
		},
		showDetail : function (shopUI, product, backFunction) {
			this.backFunctions.push(backFunction);
			this.productOriginal = product;
			this.product = angular.copy(product);
			shopUI.setupForUpdate();
			shopUI.hideAllMenuItemContainers();
			this.container.showDetail = true;
		},
		showCreateForm : function (shopUI, backFunction) {
			this.backFunctions.push(backFunction);
			this.product = {productId:null,status:"ACTIVE"};
			shopUI.hideAllMenuItemContainers();
			this.container.showDetail = true;
		},
		createProduct : function (shopUI) {
			shopUI.adminService.insertProduct(shopUI.shop.shopId, {entity:{productId:null,status:this.product.status}}, {shopUI:shopUI})
	    	.success(function (data, status, headers, config) {
	    		if (!config.shopUI.shop.productList) {
	    			config.shopUI.shop.productList = [];
	    		}
	    		config.shopUI.shop.productList.push(data.entity);
	    		config.shopUI.productUI.updateProduct(config.shopUI, data.entity);
	    	})
	    	.error(function (error) {alert('[createProduct] Request Failed: ' + error.message);});
		},
		updateProduct : function (shopUI, productWithId) {
			shopUI.adminService.updateProduct(shopUI.shop.shopId, {
				entity:{productId:productWithId.productId,status:this.product.status},
				languageId:shopUI.shop.languageTables[0].language.languageId,
				messages:[
				    {code : shopUI.util.message.getProductNameCode(productWithId), message : shopUI.shopLanguageTable[shopUI.util.message.getProductNameCode(this.product)]},
				    {code : shopUI.util.message.getProductNameFullCode(productWithId), message : shopUI.shopLanguageTable[shopUI.util.message.getProductNameFullCode(this.product)]},
				    {code : shopUI.util.message.getProductDescriptionCode(productWithId), message : shopUI.shopLanguageTable[shopUI.util.message.getProductDescriptionCode(this.product)]}
				]
			}, {shopUI:shopUI})
		    .success(function (data, status, headers, config) {
		    	config.shopUI.copyMessages(data.messages);
		    	config.shopUI.productUI.product = data.entity;
		    	config.shopUI.productUI.goBack(config.shopUI, false, {product:config.shopUI.productUI.product});
		    })
		    .error(function (error) {alert('[updateProduct] Request Failed: ' + error.message);});
		}
	};
}]);

angular.module('MYORDY_ADMIN_APP').factory('categoryUI', [function() {
	return {
		container : {showDetail:false,showList:false},
		category : null,
		categoryOriginal : null,
		backFunctions : [],
		goBack : function (shopUI, rollbackUpdate, config) {
			if (rollbackUpdate) {
				this.category = this.categoryOriginal;
				shopUI.rollbackUpdate();
			}
			shopUI.util.nav.goBack(shopUI, shopUI.categoryUI.backFunctions, config);
		},
		backToManageCategory : function (shopUI) {
			shopUI.hideAllMenuItemContainers();
			shopUI.categoryUI.container.showList = true;
		},
		showList : function (shopUI, backFunction) {
			this.backFunctions.push(backFunction);
			this.backToManageCategory(shopUI);
		},
		showDetail : function (shopUI, category, backFunction) {
			this.backFunctions.push(backFunction);
			this.categoryOriginal = category;
			this.category = angular.copy(category);
			shopUI.setupForUpdate();
			shopUI.hideAllMenuItemContainers();
			this.container.showDetail = true;
		},
		showCreateForm : function (shopUI, backFunction) {
			this.backFunctions.push(backFunction);
			this.category = {categoryId:null,status:"ACTIVE"};
			shopUI.hideAllMenuItemContainers();
			this.container.showDetail = true;
		},
		createCategory : function (shopUI) {
			shopUI.adminService.insertCategory(shopUI.shop.shopId, {entity:{categoryId:null,status:this.category.status}}, {shopUI:shopUI})
	    	.success(function (data, status, headers, config) {
	    		if (!config.shopUI.shop.categoryList) {
	    			config.shopUI.shop.categoryList = [];
	    		}
	    		config.shopUI.shop.categoryList.push(data.entity);
	    		config.shopUI.categoryUI.updateCategory(config.shopUI, data.entity);
	    	})
	    	.error(function (error) {alert('[createCategory] Request Failed: ' + error.message);});
		},
		updateCategory : function (shopUI, categoryWithId) {
			shopUI.adminService.updateCategory(shopUI.shop.shopId, {
				entity:{categoryId:categoryWithId.categoryId,status:this.category.status},
				languageId:shopUI.shop.languageTables[0].language.languageId,
				messages:[
				    {code : shopUI.util.message.getCategoryNameCode(categoryWithId), message : shopUI.shopLanguageTable[shopUI.util.message.getCategoryNameCode(this.category)]},
				    {code : shopUI.util.message.getCategoryDescriptionCode(categoryWithId), message : shopUI.shopLanguageTable[shopUI.util.message.getCategoryDescriptionCode(this.category)]}
				]
			}, {shopUI:shopUI})
		    .success(function (data, status, headers, config) {
		    	config.shopUI.copyMessages(data.messages);
		    	config.shopUI.categoryUI.category = data.entity;
		    	config.shopUI.categoryUI.goBack(config.shopUI, false, {category:config.shopUI.categoryUI.category});
		    })
		    .error(function (error) {alert('[updateCategory] Request Failed: ' + error.message);});
		}
	};
}]);

angular.module('MYORDY_ADMIN_APP').factory('menuUI', ['adminService', function(adminServiceArg) {
	return {
		menu : null,
		menuOriginal : null,
		adminService : adminServiceArg,
		menuContainer : {showMenuDetail:false,showMenuItemDetail:false},

		goBack : function (shopUI, rollbackUpdate) {
			if (rollbackUpdate) {
				this.menu = this.menuOriginal;
				shopUI.rollbackUpdate();
			}
			shopUI.showShopDetails();
		},
		showCreateMenuForm : function (shopUI) {
	    	this.menu = {menuId:null,status:"ACTIVE"};
			shopUI.hideAllShopContainers();
			this.menuContainer.showMenuDetail = true;
		},
		showMenuDetails : function (shopUI, menu) {
	    	this.menuOriginal = menu;
	    	this.menu = angular.copy(menu);
			shopUI.setupForUpdate();
			shopUI.hideAllShopContainers();
			this.menuContainer.showMenuDetail = true;
	    },
	    createMenu : function (shopUI) {
	    	this.adminService.insertMenu(shopUI.shop.shopId, {entity:{menuId:null,status:this.menu.status}}, {shopUI:shopUI})
		    	.success(function (data, status, headers, config) {
	           		var shopUI = config.shopUI;
	           		var menuDTO = data;
	           		shopUI.menuUI.updateMenu(shopUI, menuDTO.entity);
		    	})
		    	.error(function (error) {alert('[createMenu] Request Failed: ' + error.message);});
        },
	    updateMenu : function (shopUI, menuWithId) {
	    	this.adminService.updateMenu(shopUI.shop.shopId, {
				entity:{menuId:menuWithId.menuId,status:this.menu.status},
				languageId:shopUI.shop.languageTables[0].language.languageId,
				messages:[{code : shopUI.util.message.getMenuNameCode(menuWithId), message : shopUI.shopLanguageTable[shopUI.util.message.getMenuNameCode(this.menu)]}]
			}, {shopUI:shopUI})
		    .success(function (data, status, headers, config) {
		    	config.shopUI.copyMessages(data.messages);
		    	config.shopUI.menuUI.menu = data.entity;
		    	config.shopUI.menuUI.goBack(config.shopUI, false);
		    })
		    .error(function (error) {alert('[updateMenu] Request Failed: ' + error.message);});
	    }

	};
}]);

angular.module('MYORDY_ADMIN_APP').factory('shopUI', ['adminService', 'util', 'menuUI', 'categoryUI', 'menuItemUI', 'productUI',
function(adminServiceArg, utilArg, menuUIArg, categoryUIArg, menuItemUIArg, productUIArg) {
	return {
		shopList : {shops:null},
		shop : null,
		shopLanguageTable : null,
		shopLanguageTableOriginal : null,
		util : utilArg,
		adminService : adminServiceArg,
		menuUI : menuUIArg,
		categoryUI : categoryUIArg,
		menuItemUI : menuItemUIArg,
		productUI : productUIArg,
		shopContainer : {showShopDetail:false,showShopList:false},
		referenceData : null,
		showShopList : function () {
			this.adminService.getShops(this)
	           	.success(function (data, status, headers, config) {
	           		var shopUI = config.caller;
	           		shopUI.shopList.shops = data;
	           		shopUI.hideAllShopContainers();
	           		shopUI.shopContainer.showShopList = true;
	           	})
	           	.error(function (error) {alert('Request Failed: ' + error.message);});
		},
	    shopView : function (shopId) {
	    	this.adminService.getShop(shopId, this)
	    		.success(function (data, status, headers, config) {
	           		var shopUI = config.caller;
	           		shopUI.shop = data;
	           		shopUI.shopLanguageTable = shopUI.shop.languageTables[0].languageTable;
	           		shopUI.hideAllShopContainers();
	           		shopUI.shopContainer.showShopDetail = true;
	    		})
	    		.error(function (error) {alert('Request Failed: ' + error.message);});
	    },
	    showShopDetails : function () {
	    	this.hideAllShopContainers();
	    	this.shopContainer.showShopDetail = true;
	    },
	    showCreateShopForm : function () {
	    	this.shop = {shopId:null,status:"ACTIVE"};
	    	this.shopLanguageTable = {};
	    	this.hideAllShopContainers();
	    	this.shopContainer.showShopDetail = true;
	    },
	    createShop : function () {
	    	this.adminService.insertShop(
	    		{entity:{shopId:this.shop.shopId,status:this.shop.status}}, this
	    	)
		    .success(function (data, status, headers, config) {
	           		var shopUI = config.caller;
	           		var shopDTO = data;
		    		shopDTO.entity.languageTables[0].languageTable[shopUI.util.shop.getNameCode(shopDTO.entity)] = shopUI.shopLanguageTable[shopUI.util.shop.getNameCode(shopUI.shop)];
		    		shopUI.shop = shopDTO.entity;
					shopUI.shopLanguageTable = shopDTO.entity.languageTables[0].languageTable;
					shopUI.updateShop();
		    })
		    .error(function (error) {alert('Request Failed: ' + error.message);});
        },
	    updateShop : function () {
	    	this.adminService.updateShop(
	    		{
	    			entity:{shopId:this.shop.shopId,status:this.shop.status},
	    			languageId:this.shop.languageTables[0].language.languageId,
	    			messages:[{code : this.util.shop.getNameCode(this.shop), message : this.shopLanguageTable[this.util.shop.getNameCode(this.shop)]}]
	    		}, this
	    	)
		    .success(function (data, status, headers, config) {/*console.log(data);*/})
		    .error(function (error) {alert('Request Failed: ' + error.message);});
	    },
        menuDetails : function (menu) {
	    	this.menu = menu;
	    	this.hideAllShopContainers();
	    	this.shopContainer.showMenuDetail = true;
	    },

	    setupForUpdate : function () {
			this.shopLanguageTableOriginal = this.shopLanguageTable;
			this.shopLanguageTable = angular.copy(this.shopLanguageTable);
	    },

	    rollbackUpdate : function () {
			this.shopLanguageTable = this.shopLanguageTableOriginal;
	    },

		copyMessages : function (messages) {
			for (var i = 0; i < messages.length; i++) {
				this.shopLanguageTable[messages[i].code] = messages[i].message;
			}
			this.util.message.cleanupLanguageTable(this.shopLanguageTable);
			this.shopLanguageTableOriginal = this.shopLanguageTable;
		},

	    hideAllMenuItemContainers : function () {
			for(var key in this.menuItemUI.container) {
			    this.menuItemUI.container[key] = false;
			}
			for(var key in this.categoryUI.container) {
			    this.categoryUI.container[key] = false;
			}
			for(var key in this.productUI.container) {
			    this.productUI.container[key] = false;
			}
	    },

	    hideAllShopContainers : function () {
	    	this.menuItemUI.hideAllContainers();
			for(var key in this.menuUI.menuContainer) {
			    this.menuUI.menuContainer[key] = false;
			}
			for(var key in this.shopContainer) {
			    this.shopContainer[key] = false;
			}
		}
	};
}]);


MYORDY_ADMIN_APP.controller('MYORDY_ADMIN_APP_CTRL', [
          '$scope', '$modal', '$window', 'adminService', 'shopUI',
      function ($scope, $modal, $window, adminService, shopUI) {

		$scope.sys = SYS;
		$scope.pageLoadServerTime = PAGE_LOAD_SERVER_TIME;
		$scope.contextPath = CONTEXT_PATH;

		$scope.shopUI = shopUI;
//		$scope.util = util;

		$scope.$watch('shopUI.menuItemUI.menuItem', function(newValue, oldValue) {
			$scope.shopUI.menuItemUI.onMenuItemChange(newValue, oldValue);
		});

		$scope.openMyOrdyPopupContent = function(content) {
		    var theWindow = window.open(content, 'MYORDY_CONTENT_POPUP', 'directories=no,menubar=no,location=no,resizable=yes,scrollbars=yes,width=700,height=550');
		    theWindow.focus();
		};

	    adminService.getReferenceData()
            .success(function (referenceData) {$scope.shopUI.referenceData = referenceData;})
            .error(function (error) {alert('[getReferenceData] Request Failed: ' + error.message);});
    }
]);

