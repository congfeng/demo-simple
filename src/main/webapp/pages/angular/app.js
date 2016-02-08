var peisongApp = angular.module('peisongApp',['ngRoute']);  

peisongApp.config(['$routeProvider',function ($routeProvider) {  
    $routeProvider  
		.when('/home', {templateUrl: 'pages/home.html',controller: 'DefaultController'})
        .when('/pc-sc', {templateUrl: 'pages/pc-sc.html',controller: 'DefaultController'})  
        .when('/pc-cx', {templateUrl: 'pages/pc-cx.html',controller: 'DefaultController'})
		.when('/pc-fp', {templateUrl: 'pages/pc-fp.html',controller: 'DefaultController'})
		.when('/adddriver', {templateUrl: 'pages/adddriver.html',controller: 'DefaultController'})
		.when('/sj-jk', {templateUrl: 'pages/sj-jk.html',controller: 'DefaultController'})
		.when('/pc-sk', {templateUrl: 'pages/pc-sk.html',controller: 'DefaultController'})
		.when('/cx-sz', {templateUrl: 'pages/cx-sz.html',controller: 'DefaultController'})
		.when('/qycs', {templateUrl: 'pages/qycs.html',controller: 'DefaultController'})
		.when('/kf-sz', {templateUrl: 'pages/kf-sz.html',controller: 'DefaultController'})
		.when('/qy-sz', {templateUrl: 'pages/qy-sz.html',controller: 'DefaultController'})
		.when('/thsj', {templateUrl: 'pages/thsj.html',controller: 'DefaultController'})
		.when('/cys', {templateUrl: 'pages/cys.html',controller: 'DefaultController'})
		.when('/qx-sz', {templateUrl: 'pages/qx-sz.html',controller: 'DefaultController'})
		.otherwise({redirectTo: '/home'});  
}]);
 
peisongApp.controller('DefaultController',function($scope,$routeParams) {  
	//$scope.id = $routeParams.id;
});  
  