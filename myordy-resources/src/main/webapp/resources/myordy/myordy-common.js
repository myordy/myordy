var SYS = {};
function makeMyOrdyAngularApp(appName) {
	var result = angular.module(appName, ['ui.bootstrap', 'userLocale', 'angularSpinner', 'ngCookies', 'internationalPhoneNumber', 'blockUI']);

	result.config(function($httpProvider) {
		$httpProvider.interceptors.push(function($q, usSpinnerService) {
			return {
				request : function(request) {
			        usSpinnerService.spin('spinner-main');
					return request;
				},
				requestError : function(rejection) {
			        usSpinnerService.stop('spinner-main');
					return $q.reject(rejection);
				},
			    response: function(response) {
			        usSpinnerService.stop('spinner-main');
			        return response;
			    },
			    responseError : function(rejection) {
			        usSpinnerService.stop('spinner-main');
					return $q.reject(rejection);
				}
			};
		});
	});

	result.config(function(blockUIConfig) {
		blockUIConfig.autoBlock = false;
	});
	
//	result.directive('googleplace', function() {
//	    return {
//	        require: 'ngModel',
//	        link: function(scope, element, attrs, model) {
//	            var options = {
//	                types: ['geocode'],
//	                componentRestrictions: {} // country: 'au'
//	            };
//	            scope.gPlace = new google.maps.places.Autocomplete(element[0], options);
//
//	            google.maps.event.addListener(scope.gPlace, 'place_changed', function() {
//	                scope.$apply(function() {
//	                    model.$setViewValue(element.val());                
//	                });
//	            });
//	        }
//	    };
//	});

	result.factory('util', [function() {
		return {
			googleMaps : {
				addressAutocomplete : function(addressInputElementId, countryCode, address, onPlaceChangedCallback) {
					var googleAutocomplete = new google.maps.places.Autocomplete((document.getElementById(addressInputElementId)), {types: ['geocode'], componentRestrictions: {country: countryCode}});
					googleAutocomplete.addListener('place_changed',
						function() {
							var place = googleAutocomplete.getPlace();
							for (var i = 0; i < place.address_components.length; i++) {
								var addressType = place.address_components[i].types[0];
								if ('locality' == addressType) {
									address.suburbName = place.address_components[i]['long_name'];
								} else if ('administrative_area_level_1' == addressType) {
									address.stateCode = place.address_components[i]['short_name'];;
								} else if ('country' == addressType) {
									address.countryCode = place.address_components[i]['short_name'];;
								} else if ('postal_code' == addressType) {
									address.postcode = place.address_components[i]['short_name'];;
								}
							}
							address.address = place.formatted_address;
							address.geocodeLatitude = place.geometry.location.lat();
							address.geocodeLongitude = place.geometry.location.lng();

							onPlaceChangedCallback();
						}
					);
				},
				setupMap : function (address, mapDivId) {
					var map = new google.maps.Map(document.getElementById(mapDivId), {
						center: {lat: Number(address.geocodeLatitude), lng: Number(address.geocodeLongitude)},
						zoom: 14
					});
					var marker = new google.maps.Marker({
						position: {lat: Number(address.geocodeLatitude), lng: Number(address.geocodeLongitude)},
						map: map,
						title: address.address
					});
				},
			},
			onlinePayment : {
				isOnlinePaymentAvailable : function (onlinePayment) {
					var result = false;
					if (onlinePayment && (onlinePayment.stripe || onlinePayment.paypal)) {
						if (onlinePayment.stripe && onlinePayment.stripe.stripeInstance) {
							result = true;
						} else if (onlinePayment.paypal && onlinePayment.paypal.clientToken) {
							result = true;
						}
					}
					return result;
				},
				stripe : {
					// STRIPE_MYORDY = {publishableKey:'',cardElementId:null,errorElementId:null,stripeInstance:null,cardElement:null,onCreateTokenSuccess:null,posUI:null};
					setupCardInput : function (stripeUI) {
						try {
							stripeUI.stripeInstance = Stripe(stripeUI.publishableKey);
							var elements = stripeUI.stripeInstance.elements();
							var style = {
							  base: {
							    color: '#32325d',
							    lineHeight: '24px',
							    fontFamily: '"Helvetica Neue", Helvetica, sans-serif',
							    fontSmoothing: 'antialiased',
							    fontSize: '16px',
							    '::placeholder': {
							      color: '#aab7c4'
							    }
							  },
							  invalid: {
							    color: '#fa755a',
							    iconColor: '#fa755a'
							  }
							};
							stripeUI.cardElement = elements.create('card', {style: style, hidePostalCode : true});
							stripeUI.cardElement.mount('#' + stripeUI.cardElementId);
							stripeUI.cardElement.addEventListener('change', function(event) {
								var displayError = document.getElementById(stripeUI.errorElementId);
								if (event.error) {
									displayError.textContent = event.error.message;
								} else {
									displayError.textContent = '';
								}
							});
						} catch (e) {
							stripeUI.stripeInstance = null;
						}
					},
					createToken : function (stripeUI) {
						stripeUI.stripeInstance.createToken(stripeUI.cardElement).then(function(result) {
							stripeUI.onCreateToken(stripeUI, result);
						});
					}
				},
				paypal : {
					setupPaypalButton : function (paypalUI) {
						braintree.client.create(
							{authorization: paypalUI.clientToken},
							function (clientErr, clientInstance) {
								if (clientErr) {document.getElementById(paypalUI.errorElementId).textContent = 'Error creating client: ' + clientErr; return;}
								braintree.paypalCheckout.create(
									{client: clientInstance},
									function (paypalCheckoutErr, paypalCheckoutInstance) {
										if (paypalCheckoutErr) {document.getElementById(paypalUI.errorElementId).textContent = 'Error creating client: ' + paypalCheckoutErr; return;}
										paypal.Button.render(
											{
												env: paypalUI.env,
												payment: function () {
													return paypalCheckoutInstance.createPayment(paypalUI.getPayment(paypalUI));
												},
												onAuthorize: function (data, actions) {
													return paypalCheckoutInstance.tokenizePayment(data)
													.then(function (payload) {
														paypalUI.onAuthorize(paypalUI, payload);
													});
												},
												onCancel: function (data) {},
												onError: function (err) { document.getElementById(paypalUI.errorElementId).textContent = 'checkout.js error: ' + err;}
											},
											'#' + paypalUI.buttonElementId
										).then(function () {});
									}
								);
							}
						);
					}
				}
			},
			theUserPhone : {
				findMobileNumber : function (phoneList) {
					var result = null;
					if (phoneList) {
						for (var i = 0; i < phoneList.length; i++) {
							if (phoneList[i].phoneType == 'MOBILE') {
								result = phoneList[i].phoneNumber;
								break;
							}
						}
					}
					return result;
				},
				getCustomerPhoneNumbers : function (phoneList, cartCustomerMobileNumber) {
					var result = cartCustomerMobileNumber;
					if ((null == result || '' == result.replace(/^\s*|\s*$/g,"")) && phoneList) {
						result = '';
						for (var i = 0; i < phoneList.length; i++) {
							result += phoneList[i].phoneNumber;
							result += ' ';
						}
					}
					return result;
				}
			},
			security : {
				isInStaffRoleList : function (loginResponse, role) {
					if (loginResponse && loginResponse.user && loginResponse.user.staffRoleList && role) {
						for (var i = 0; i < loginResponse.user.staffRoleList.length; i++) {
							if (role == loginResponse.user.staffRoleList[i].staffRole) {
								return true;
							}
						}
					}
					return false;
				}
			},
			tools : {
				findUniqueItemsInArray : function (theArray, idPropertyName) {
					var result = [];
					for (var i = 0; i < theArray.length; i++) {
						var itemNotFound = true;
						for (var j = 0; j < result.length; j++) {
							if (theArray[i][idPropertyName] == result[j][idPropertyName]) {
								itemNotFound = false;
								break;
							}
						}
						if (itemNotFound) {
							result.push(theArray[i]);
						}
					}
					return result;
		    	},
		    	loopCounter : function (count) {
					return new Array(count);
		    	},
		    	trim : function (str) {
					return str.replace(/^\s*|\s*$/g,"");
		    	},
		    	findArrayItemByPropertyValue : function (theArray, propertyToCheck, expectedValue) {
		    		var result = null;
		    		for (var i = 0; i < theArray.length; i++) {
		    			if (("" + expectedValue).toLowerCase() == ("" + theArray[i][propertyToCheck]).toLowerCase()) {
		    				result = theArray[i];
		    				break;
		    			}
		    		}
		    		return result;
		    	},
		    	areAllObjectPropertiesBlank : function (theObject, propertiesToCheck) {
		    		var result = true;
		    		for (var i = 0; i < propertiesToCheck.length; i++) {
		    			if (null != theObject[propertiesToCheck[i]]
		    					&& '' != this.trim(theObject[propertiesToCheck[i]])) {
		    				result = false;
		    				break;
		    			}
		    		}
		    		return result;
				}
			},
			tag : {
		    	isTagInListByCode : function (tagList, tagCodeToFind) {
		    		if (tagList && tagCodeToFind) {
		    			for (var i = 0; i < tagList.length; i++) {
		    				if (tagList[i].code == tagCodeToFind) {
		    					return true;
		    				}
		    			}
		    		}
					return false;
		    	},
		    	isTagInListById : function (tagList, tagToFind) {
					for (var i = 0; i < tagList.length; i++) {
						if (tagList[i].tagId == tagToFind.tagId) {
							return true;
						}
					}
					return false;
		    	}
			},
			message : {
				NO_ID : '__NO_ID__',
	    		getMenuItemName : function (shopLanguageTable, menuItem) {
	    			var result = null;
	    			if (menuItem) {
	    				if (menuItem.category) {
	    					result = shopLanguageTable[this.getCategoryNameCode(menuItem.category)];
	    				} else if (menuItem.product) {
	    					result = shopLanguageTable[this.getProductNameFullCode(menuItem.product)];
	    				}
	    			}
	    			return result;
	    		},
	    		getShopNameCode : function (shop) {
	    			if (shop && shop.shopId) {
	    				return 'shopName' + shop.shopId;
	    			}
					return 'shopName' + this.NO_ID;
	    		},
	    		getShopAddressCode : function (shop) {
	    			if (shop && shop.shopId) {
	    				return 'shopAddress' + shop.shopId;
	    			}
					return 'shopAddress' + this.NO_ID;
	    		},
	    		getShopPhoneCode : function (shop) {
	    			if (shop && shop.shopId) {
	    				return 'shopPhone' + shop.shopId;
	    			}
					return 'shopPhone' + this.NO_ID;
	    		},
	    		getShopBusinessNumberCode : function (shop) {
	    			if (shop && shop.shopId) {
	    				return 'shopBusinessNumber' + shop.shopId;
	    			}
					return 'shopBusinessNumber' + this.NO_ID;
	    		},
	    		getShopWebsiteCode : function (shop) {
	    			if (shop && shop.shopId) {
	    				return 'shopWebsite' + shop.shopId;
	    			}
					return 'shopWebsite' + this.NO_ID;
	    		},
	    		getMenuNameCode : function (menu) {
	    			if (menu && menu.menuId) {
	    				return "menu" + menu.menuId + "N";
	    			}
					return 'menu' + this.NO_ID + 'N';
	    		},
	    		getExtraOptionItemNameCode : function (extraOptionItem) {
	    			if (extraOptionItem && extraOptionItem.extraOptionItemId) {
	    				return "extOpt" + extraOptionItem.extraOptionItemId + "N";
	    			}
					return 'extOpt' + this.NO_ID + 'N';
	    		},
	    		getComboMenuItemNameCode : function (comboMenuItem) {
	    			if (comboMenuItem && comboMenuItem.menuItemComboId) {
	    				return "menuItemCombo" + comboMenuItem.menuItemComboId + "N";
	    			}
					return 'menuItemCombo' + this.NO_ID + 'NF';
	    		},
	    		getComboMenuItemDescriptionCode : function (comboMenuItem) {
	    			if (comboMenuItem && comboMenuItem.menuItemComboId) {
	    				return "menuItemCombo" + comboMenuItem.menuItemComboId + "D";
	    			}
					return 'menuItemCombo' + this.NO_ID + 'D';
	    		},
	    		getComboMenuItemNameFullCode : function (comboMenuItem) {
	    			if (comboMenuItem && comboMenuItem.menuItemComboId) {
	    				return "menuItemCombo" + comboMenuItem.menuItemComboId + "NF";
	    			}
					return 'menuItemCombo' + this.NO_ID + 'N';
	    		},
	    		getComboMenuItemOptionNameFullCode : function (comboOption) {
	    			if (comboOption && comboOption.menuItemComboOptionId) {
	    				return "menuItemComboOption" + comboOption.menuItemComboOptionId + "N";
	    			}
					return 'menuItemComboOption' + this.NO_ID + 'N';
	    		},
	    		getProductNameFullCode : function (product) {
	    			if (product && product.productId) {
	    	    		return "prd" + product.productId + "NF";
	    			}
		    		return 'prd' + this.NO_ID + 'NF';
	    		},
	    		getProductNameCode : function (product) {
	    			if (product && product.productId) {
	    	    		return "prd" + product.productId + "N";
	    			}
		    		return 'prd' + this.NO_ID + 'N';
	    		},
	    		getProductDescriptionCode : function (product) {
	    			if (product && product.productId) {
	    	    		return "prd" + product.productId + "D";
	    			}
		    		return 'prd' + this.NO_ID + 'D';
	    		},
	    		getCategoryNameCode : function (category) {
	    			if (category && category.categoryId) {
	    	    		return "cat" + category.categoryId + "N";
	    			}
		    		return 'cat' + this.NO_ID + 'N';
	    		},
	    		getCategoryDescriptionCode : function (category) {
	    			if (category && category.categoryId) {
	    				return "cat" + category.categoryId + "D";
	    			}
		    		return 'cat' + this.NO_ID + 'D';
	    		},
	    		cleanupLanguageTable : function (shopLanguageTable) {
	    			for(var key in shopLanguageTable) {
	    				if (key.indexOf(this.NO_ID) != -1) {
	    					shopLanguageTable[key] = '';
	    				}
	    			}
	    		}
			},
			orderItemOption : {
				getOrderItemExtraOptionAmount : function (qty, orderItem) {
					var result = 0;
					if (orderItem.extraOptions) {
						if (orderItem.extraOptions.addOptions) {
							for (var i = 0; i < orderItem.extraOptions.addOptions.length; i++) {
								var theOption = orderItem.extraOptions.addOptions[i];
								result += (theOption.qty * qty) * theOption.option.addPrice;
							}
						}
						if (orderItem.extraOptions.removeOptions) {
							for (var i = 0; i < orderItem.extraOptions.removeOptions.length; i++) {
								var theOption = orderItem.extraOptions.removeOptions[i];
								result += (theOption.qty * qty) * theOption.option.removePrice;
							}
						}
					}
					return result;
				},
				getComboItemSubItemCountByTag : function (util, comboOrderItem, code, subItemCode) {
					var result = 0;
					if (comboOrderItem.ordyItemsComboSub && code) {
						for (var i = 0; i < comboOrderItem.ordyItemsComboSub.length; i++) {
			    			if (util.tag.isTagInListByCode(comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.comboMenuItem.tagList, code)) {
								for (var j = 0; j < comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems.length; j++) {
									if (util.tag.isTagInListByCode(comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems[j].orderItem.menuItem.tagList, subItemCode)) {
										result += comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems[j].qty;										
									}
								}
			    			}
						}
					}
					return result;
				},
				getComboItemSubExtraAmount : function (util, comboOrderItem, code, subItemCode) {
					var result = 0;
					if (comboOrderItem.ordyItemsComboSub && code && subItemCode) {
						for (var i = 0; i < comboOrderItem.ordyItemsComboSub.length; i++) {
			    			if (util.tag.isTagInListByCode(comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.comboMenuItem.tagList, code)) {
								for (var j = 0; j < comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems.length; j++) {
									if (util.tag.isTagInListByCode(comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems[j].orderItem.menuItem.tagList, subItemCode)) {
										result += this.getOrderItemExtraOptionAmount(
											comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems[j].qty,
											comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.orderItems[j].orderItem
										);
									}
								}
			    			}
						}
					}
					return result;
				},
				getComboItemExtraAmount : function (comboOrderItem, code) {
					var result = 0;
					if (comboOrderItem.orderItems && code) {
						for (var i = 0; i < comboOrderItem.orderItems.length; i++) {
							if (comboOrderItem.orderItems[i].orderItem.menuItem.tagList) {
								for (var j = 0; j < comboOrderItem.orderItems[i].orderItem.menuItem.tagList.length; j++) {
									if (code == comboOrderItem.orderItems[i].orderItem.menuItem.tagList[j].code) {
										result += this.getOrderItemExtraOptionAmount(comboOrderItem.orderItems[i].qty, comboOrderItem.orderItems[i].orderItem);
										break;
									}
								}
							}
						}
					}
					return result;
				},
				getComboItemPrice : function (comboOrderItem, code) {
					var result = 0;
					if (comboOrderItem.orderItems && code) {
						for (var i = 0; i < comboOrderItem.orderItems.length; i++) {
							if (comboOrderItem.orderItems[i].orderItem.menuItem.tagList) {
								for (var j = 0; j < comboOrderItem.orderItems[i].orderItem.menuItem.tagList.length; j++) {
									if (code == comboOrderItem.orderItems[i].orderItem.menuItem.tagList[j].code) {
										result += comboOrderItem.orderItems[i].orderItem.menuItem.price;
										break;
									}
								}
							}
						}
					}
					return result;
				},
				getComboItemCountByTag : function (comboOrderItem, code) {
					var result = 0;
					if (comboOrderItem.orderItems && code) {
						for (var i = 0; i < comboOrderItem.orderItems.length; i++) {
							if (comboOrderItem.orderItems[i].orderItem.menuItem.tagList) {
								for (var j = 0; j < comboOrderItem.orderItems[i].orderItem.menuItem.tagList.length; j++) {
									if (code == comboOrderItem.orderItems[i].orderItem.menuItem.tagList[j].code) {
										result += comboOrderItem.orderItems[i].qty;
										break;
									}
								}
							}
						}
					}
					return result;
				},
				getComboItemSubCountByTag : function (util, comboOrderItem, code) {
					var result = 0;
					if (comboOrderItem.ordyItemsComboSub && code) {
						for (var i = 0; i < comboOrderItem.ordyItemsComboSub.length; i++) {
			    			if (util.tag.isTagInListByCode(comboOrderItem.ordyItemsComboSub[i].orderItemComboSub.comboMenuItem.tagList, code)) {
								result += comboOrderItem.ordyItemsComboSub[i].qty;
			    			}
						}
					}
					return result;
				},
				findExtraOptionItem : function (extraOptionItemList, extraOptionItemId) {
					var result = null;
					if (extraOptionItemList && extraOptionItemId) {
						for (var i = 0; i < extraOptionItemList.length; i++) {
							if (extraOptionItemId == extraOptionItemList[i].extraOptionItemId) {
								result = extraOptionItemList[i];
								break;
							}
						}
					}
					return result;
				},
				findExtraOptionGroup : function (code, extraOptionGroupList) {
					var result = null;
					for (var i = 0; extraOptionGroupList.length; i++) {
						if (code == extraOptionGroupList[i].code) {
							result = extraOptionGroupList[i];
							break;
						}
					}
					return result;
	    		}
			},
			menu : {
				isDeliveryMenu : function (menu) {
					return menu && menu.menuOrdyConfig && menu.menuOrdyConfig.shopSuburbConfigList && menu.menuOrdyConfig.shopSuburbConfigList.length > 0;
				},
				getDefaultDeliveryTime : function (menu, deliverySuburb) {
					var result = 0;
					if (menu && menu.menuOrdyConfig) {
						result = menu.menuOrdyConfig.defaultDeliveryMins;
						if (deliverySuburb && this.isDeliveryMenu(menu)) {
							for (var i = 0; i < menu.menuOrdyConfig.shopSuburbConfigList.length; i++) {
								if (menu.menuOrdyConfig.shopSuburbConfigList[i].suburb) {
									if (menu.menuOrdyConfig.shopSuburbConfigList[i].suburb.suburbId == deliverySuburb.suburbId) {
										result = menu.menuOrdyConfig.shopSuburbConfigList[i].minDeliveryMins;
										break;
									}
								}
							}
						}
					}
					return new Date(new Date().getTime() + (result * 60000));
				},
				findMenuById : function (menuList, menuId) {
					var result = null;
					if (menuList && menuId) {
						for (var i = 0; i < menuList.length; i++) {
							if (menuList[i].menuId == menuId) {
								result = menuList[i];
								break;
							}
						}
					}
	    			return result;
				},
				findCustomerAddressesWithinServicedSuburbs : function (menu, customer) {
					var result = [];
					if (customer && customer.contact && customer.contact.addressList && menu.menuOrdyConfig && menu.menuOrdyConfig.shopSuburbConfigList) {
						for (var i = 0; i < customer.contact.addressList.length; i++) {
							if (customer.contact.addressList[i].suburbId) {
								for (var j = 0; j < menu.menuOrdyConfig.shopSuburbConfigList.length; j++) {
									if (menu.menuOrdyConfig.shopSuburbConfigList[j].suburb) {
										if (customer.contact.addressList[i].suburbId == menu.menuOrdyConfig.shopSuburbConfigList[j].suburb.suburbId) {
											result.push(customer.contact.addressList[i]);
											break;
										}
									}
								}
							}
						}
					}
					return result;
				}
			},
			menuItem : {
			    isComboOptionOrderItemsQtyEnough : function (comboMenuItem, comboOptionOrderItems) {
					for (var m = 0; m < comboMenuItem.comboOptionList.length; m++) {
						if (comboOptionOrderItems[m].qty < comboMenuItem.comboOptionList[m].qtyRequired) {
							return false;
						}
					}
					return true;
			    },
			    findParentComboMenuItems : function (menuItem, menuItemComboList) {
			    	var result = [];
			    	if (menuItem.tagList && menuItemComboList) {
			    		for (var i = 0; i < menuItem.tagList.length; i++) {
			    			for (var j = 0; j < menuItemComboList.length; j++) {
			    				if (menuItemComboList[j].comboOptionList) {
			    					for (var k = 0; k < menuItemComboList[j].comboOptionList.length; k++) {
			    	    				if (menuItemComboList[j].comboOptionList[k].menuItemTag && menuItem.tagList[i].tagId == menuItemComboList[j].comboOptionList[k].menuItemTag.tagId) {
			    	    					result.push(menuItemComboList[j]);
			    	    				}
			    					}
			    				}
			    			}
			    		}
			    	}
			    	return result;
				},
				findComboMenuItemByTag : function (menuItemComboList, tagCode) {
					var result = [];
					if (menuItemComboList) {
						for (var i = 0; i < menuItemComboList.length; i++) {
							if (menuItemComboList[i].tagList) {
								for (var j = 0; j < menuItemComboList[i].tagList.length; j++) {
									if (menuItemComboList[i].tagList[j].code == tagCode) {
										result.push(menuItemComboList[i]);
										break;
									}
								}
							}
						}
					}
		    		return result;
				},
				findProductMenuItemByTag : function (menuItemMap, menuId, tagId) {
					var result = [];
					if (menuItemMap) {
						for(var key in menuItemMap) {
							var menuItemTemp = menuItemMap[key];
							if (menuItemTemp.uiProp.menuId == menuId) {
								if (menuItemTemp.product && menuItemTemp.tagList) {
									for (var i = 0; i < menuItemTemp.tagList.length; i++) {
										if (tagId == menuItemTemp.tagList[i].tagId) {
											result.push(menuItemTemp);
											break;
										}
									}
								}
							}
						}
					}
					return result;
				},
				isProduct : function (menuItem) {
					return menuItem.product != null;
	    		},
				findComboMenuItem : function (menuItemMap, comboMenuItemId) {
					var result = null;
					if (menuItemMap) {
						result = menuItemMap[this.getMenuItemComboCode(comboMenuItemId)];
					}
					return result;
	    		},
				findMenuItem : function (menuItemMap, menuItemId) {
					var result = null;
					if (menuItemMap) {
						result = menuItemMap[this.getMenuItemCode(menuItemId)];
					}
					return result;
				},
	    		getMenuItemCode : function (menuItemId) {
	    			if (menuItemId) {
	    	    		return "menuItem" + menuItemId;
	    			}
		    		return 'menuItemNULL';
	    		},
	    		getMenuItemComboCode : function (menuItemComboId) {
	    			if (menuItemComboId) {
	    	    		return "menuItemCombo" + menuItemComboId;
	    			}
		    		return 'menuItemComboNULL';
	    		},
				getMenuItemMap : function (shop) {
					var result = {};
					var menuTemp = null;
					var menuItemTemp = null;
					for (var i = 0; i < shop.menuList.length; i++) {
						menuTemp = shop.menuList[i];
						for (var j = 0; j < menuTemp.menuItems.length; j++) {
							var menuItemTemp = menuTemp.menuItems[j];
							this.addMenuItemToMap(result, menuItemTemp, menuTemp.menuId);
						}
						for (var j = 0; j < menuTemp.menuItemComboList.length; j++) {
							var menuItemComboTemp = menuTemp.menuItemComboList[j];
							menuItemComboTemp.uiProp = {menuId:menuTemp.menuId};
							result[this.getMenuItemComboCode(menuItemComboTemp.menuItemComboId)] = menuItemComboTemp;
						}
					}
					return result;
	    		},
				addMenuItemToMap : function (menuItemMap, menuItem, menuId) {
					menuItem.uiProp = {menuId:menuId};
					menuItemMap[this.getMenuItemCode(menuItem.menuItemId)] = menuItem;
					if (menuItem.menuItems) {
						for (var i = 0; i < menuItem.menuItems.length; i++) {
							this.addMenuItemToMap(menuItemMap, menuItem.menuItems[i], menuId);
						}
					}
				},
				getDefaultProductMenuItem : function (menuItem) {
					var result = menuItem;
					if (this.isProduct(menuItem)) {
						result = menuItem;
					} else if (menuItem.menuItems) {
						for (var i = 0; i < menuItem.menuItems.length; i++) {
							if (this.isProduct(menuItem.menuItems[i])) {
								result = menuItem.menuItems[i];
								break;
							}
						}
					}
					return result;
				}
			},
			nav : {
	    		goBack : function (theUI, backFunctions, config) {
	    			if (backFunctions && backFunctions.length > 0) {
	    				var backFunction = backFunctions.pop();
	    				backFunction(theUI, config);
	    			}
	    		},
	    		hideAllContainers : function(containers) {
	    			for(var key in containers) {
	    			    containers[key] = false;
	    			}
	    		},
				openMyOrdyPopupContent : function(content) {
				    var theWindow = window.open(content, 'MYORDY_CONTENT_POPUP', 'directories=no,menubar=no,location=no,resizable=yes,scrollbars=yes,width=700,height=550');
				    theWindow.focus();
	    		}
	    	}
		};
	}]);


	return result;
}
