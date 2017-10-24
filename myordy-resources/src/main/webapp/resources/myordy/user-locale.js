// Based on http://blog.trifork.com/2014/04/10/internationalization-with-angularjs/
var _DEFAULT_LANGUAGE_TABLE_SYSTEM = {
	myordy:'myordy',
//	categoryName:function (parameters) {return 'cat' + parameters.categoryId + 'N'},
//	productName:function (parameters) {return 'prd' + parameters.productId + 'N'},
	a1:'No product found ...'
};
var _USER_LANGUAGE_TABLE_SYSTEM = {
	// put selected user language text here
};
//
//if (!SHOP.defaultLanguageTable) {
//	SHOP.defaultLanguageTable = SHOP.languageTables[0].languageTable;
//}
//var DEFAULT_LANGUAGE_TABLE = angular.extend(_DEFAULT_LANGUAGE_TABLE_SYSTEM, SHOP.defaultLanguageTable);
//var USER_LANGUAGE_TABLE = angular.extend(_USER_LANGUAGE_TABLE_SYSTEM, SHOP.userLanguageTable);

var userLocale = angular.module('userLocale', []);

userLocale.factory('userLocaleService', [ '$interpolate', function($interpolate) {
	return {
		shopName : function (shop, parameters) {
			return this.userLocale(shop.languageTables[0].languageTable, 'shopName' + shop.shopId, parameters);
		},
		userLocale : function(table, label, parameters) {
			if (table.hasOwnProperty(label) && $.isFunction(table[label])) {
				label = table[label](parameters);
			}
			if (table.hasOwnProperty(label)) {
				if (parameters == null || $.isEmptyObject(parameters)) {
					return table[label];
				} else {
					return $interpolate(table[label])(parameters);
				}
			} else {
				return label;
			}
		}
	};
} ]);

userLocale.filter('shopName', [ 'userLocaleService', function(userLocaleService) {
	return function(shop, parameters) {
		if (shop) {
			return userLocaleService.shopName(shop, parameters);
		}
		return '';
	};
} ]);
