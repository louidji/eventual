'use strict';

var app = angular.module("celebrity", [])
	;

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

        $timeout(function() { $scope.go('/web/celebrity/result'); });
	};
}]);

// the create controller
app.controller("CreateCtrl", ["$scope", "$resource", "$timeout", "apiUrl", function($scope, $resource, $timeout, apiUrl) {
	// to save a celebrity
	$scope.save = function() {
		var CreateCelebrity = $resource(apiUrl + "/celebrities/new"); // a RESTful-capable resource object
		CreateCelebrity.save($scope.celebrity, function() {
			$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
		}); // $scope.celebrity comes from the detailForm in public/html/detail.html

	};
}]);

// the edit controller
app.controller("EditCtrl", ["$scope", "$resource", "$routeParams", "$timeout", "apiUrl", function($scope, $resource, $routeParams, $timeout, apiUrl) {
	var ShowCelebrity = $resource(apiUrl + "/celebrities/:id", {id:"@id"}); // a RESTful-capable resource object
	if ($routeParams.id) {
		// retrieve the corresponding celebrity from the database
		// $scope.celebrity.id.$oid is now populated so the Delete button will appear in the detailForm in public/html/detail.html
		$scope.celebrity = ShowCelebrity.get({id: $routeParams.id}, function() {
          $scope.dbContent = angular.copy($scope.celebrity);
        });
	}
	
	// decide whether to enable or not the button Save in the detailForm in public/html/detail.html 
	$scope.noChange = function() {
		return angular.equals($scope.celebrity, $scope.dbContent);
	};

	// to update a celebrity
	$scope.save = function() {
		var UpdateCelebrity = $resource(apiUrl + "/celebrities/" + $routeParams.id); // a RESTful-capable resource object
		UpdateCelebrity.save($scope.celebrity, function() {
			$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
		}); // $scope.celebrity comes from the detailForm in public/html/detail.html

	};
	
	// to delete a celebrity
	$scope.delete = function() {
		var DeleteCelebrity = $resource(apiUrl + "/celebrities/" + $routeParams.id); // a RESTful-capable resource object
		DeleteCelebrity.delete(function() {
				$timeout(function() { $scope.go('/'); }); // go back to public/html/main.html
		}); // $scope.celebrity comes from the detailForm in public/html/detail.html

	};
}]);


app.directive('celebrities', function(){
    return {
        restrict: 'E',
        templateUrl: 'assets/directives/celebrities.html'
    };
});
