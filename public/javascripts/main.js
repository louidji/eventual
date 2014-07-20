/*
 * Author: Sari Haj Hussein
 */
var app = angular.module("app", ["ngResource"])
	.constant("apiUrl", "http://localhost:9000\:9000/api") // to tell AngularJS that 9000 is not a dynamic parameter
	.config(["$routeProvider", function($routeProvider) {
		return $routeProvider.when("/", {
			templateUrl: "/assets/html/main.html",
			controller: "ListCtrl"
		}).when("/result", {
                templateUrl: "/assets/html/main.html",
                controller: "ListResult"
        }).when("/create", {
			templateUrl: "/assets/html/detail.html",
			controller: "CreateCtrl"
	    }).when("/search", {
            templateUrl: "/assets/html/search.html",
            controller: "SearchCtrl"
         }).when("/edit/:id", {
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

app.service('CelebritiesService', function() {
   var celebrities = [];
 });



app.controller('MenuController', function(){
    this.tab = 1;

    this.setTab = function(newValue){
      this.tab = newValue;
    };

    this.isSet = function(tabName){
      return this.tab === tabName;
    };
  });

// the global controller
app.controller("AppCtrl", ["$scope", "$location", function($scope, $location) {
	// the very sweet go function is inherited to all other controllers
	$scope.go = function (path) {
		$location.path(path);
	};
}]);

// the list controller
app.controller("ListCtrl", ["$scope", "$resource", "apiUrl", function($scope, $resource, apiUrl) {
	var Celebrities = $resource(apiUrl + "/celebrities"); // a RESTful-capable resource object
	$scope.celebrities = Celebrities.query(); // for the list of celebrities in public/html/main.html
}]);

// the list controller
app.controller("ListResult", ["CelebritiesService", "$scope", function(CelebritiesService, $scope) {
	$scope.celebrities = CelebritiesService.celebrities;
}]);

// the search controller
app.controller("SearchCtrl",  ["CelebritiesService", "$scope", "$resource", "$timeout", "apiUrl", function(CelebritiesService, $scope, $resource, $timeout, apiUrl) {
// TODO
	// to search a celebrity
	$scope.firstname = "";
	$scope.search = function(firstname) {
		var Celebrities = $resource(apiUrl + "/celebrities/find/:searchParam", {searchParam:firstname}); // :firstname a RESTful-capable resource object

        CelebritiesService.celebrities  = Celebrities.query(); // for the list of celebrities in public/html/main.html

        $timeout(function() { $scope.go('/result'); });
	};
}]);

// the create controller
app.controller("CreateCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
	// to save a celebrity
	$scope.save = function() {
		var CreateCelebrity = $resource(apiUrl + "/celebrities/new"); // a RESTful-capable resource object
		CreateCelebrity.save($scope.celebrity); // $scope.celebrity comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);

// the edit controller
app.controller("EditCtrl", ["$scope", "$resource", "$routeParams", "$timeout", "apiUrl", function($scope, $resource, $routeParams, $timeout, apiUrl) {
	var ShowCelebrity = $resource(apiUrl + "/celebrities/:id", {id:"@id"}); // a RESTful-capable resource object
	if ($routeParams.id) {
		// retrieve the corresponding celebrity from the database
		// $scope.celebrity.id.$oid is now populated so the Delete button will appear in the detailForm in public/html/detail.html
		$scope.celebrity = ShowCelebrity.get({id: $routeParams.id});
		$scope.dbContent = ShowCelebrity.get({id: $routeParams.id}); // this is used in the noChange function
	}
	
	// decide whether to enable or not the button Save in the detailForm in public/html/detail.html 
	$scope.noChange = function() {
		return angular.equals($scope.celebrity, $scope.dbContent);
	};

	// to update a celebrity
	$scope.save = function() {
		var UpdateCelebrity = $resource(apiUrl + "/celebrities/" + $routeParams.id); // a RESTful-capable resource object
		UpdateCelebrity.save($scope.celebrity); // $scope.celebrity comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
	
	// to delete a celebrity
	$scope.delete = function() {
		var DeleteCelebrity = $resource(apiUrl + "/celebrities/" + $routeParams.id); // a RESTful-capable resource object
		DeleteCelebrity.delete(); // $scope.celebrity comes from the detailForm in public/html/detail.html
		$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
	};
}]);


app.directive('celebrities', function(){
    return {
        restrict: 'E',
        templateUrl: 'assets/directives/celebrities.html'
    };
});
