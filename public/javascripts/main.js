'use strict';

var app = angular.module("app", ["ngResource", "ngRoute", "celebrity"])
	.constant("apiUrl", "http://localhost\\:9000/api")
	.config(["$routeProvider", function($routeProvider) {
		return $routeProvider.when("/", {
			templateUrl: "/assets/html/main.html",
			controller: "ListCtrl"
		}).when("/celebrity/result", {
                templateUrl: "/assets/html/main.html",
                controller: "ListResult"
        }).when("/celebrity/create", {
			templateUrl: "/assets/html/detail.html",
			controller: "CreateCtrl"
	    }).when("/celebrity/search", {
            templateUrl: "/assets/html/search.html",
            controller: "SearchCtrl"
         }).when("/celebrity/edit/:id", {
			templateUrl: "/assets/html/detail.html",
			controller: "EditCtrl"
		}).otherwise({
			redirectTo: "/"
		});
	}
	]).config([
	"$locationProvider", function($locationProvider) {
		return $locationProvider.html5Mode(true).hashPrefix("!"); // enable the new HTML5 routing and histoty API
	}
]);
