var nsApp = angular.module('nsApp',['ngRoute']);  

nsApp.config(['$routeProvider',function ($routeProvider) {  
    $routeProvider
        .when('/welcome', {templateUrl: 'pages/welcome.html',controller: 'DefaultController'})
        .when('/users', {templateUrl: 'pages/users.html',controller: 'DefaultController'})  
        .when('/product-type1', {templateUrl: 'pages/productmanage.html',controller: 'ProductManageController'})
        .when('/product-type2', {templateUrl: 'pages/productmanage.html',controller: 'ProductManageController'})
        .when('/product-type3', {templateUrl: 'pages/productmanage.html',controller: 'ProductManageController'})
        .when('/product-type4', {templateUrl: 'pages/productmanage.html',controller: 'ProductManageController'})
        .when('/productadd', {templateUrl: 'pages/productadd.html',controller: 'ProductAddController'})
        .when('/productupdate', {templateUrl: 'pages/productupdate.html',controller: 'ProductUpdateController'})
        .when('/notice-type1', {templateUrl: 'pages/noticemanage.html',controller: 'NoticeManageController'})
        .when('/notice-type2', {templateUrl: 'pages/noticemanage.html',controller: 'NoticeManageController'})
        .when('/notice-type3', {templateUrl: 'pages/noticemanage.html',controller: 'NoticeManageController'})
        .when('/notice-type1', {templateUrl: 'pages/noticemanage.html',controller: 'NoticeManageController'})
        .when('/notice-type1', {templateUrl: 'pages/noticemanage.html',controller: 'NoticeManageController'}) 
        .when('/noticeadd', {templateUrl: 'pages/noticeadd.html',controller: 'DefaultController'})
        .when('/noticeupdate', {templateUrl: 'pages/noticeupdate.html',controller: 'DefaultController'})
		.otherwise({redirectTo: '/welcome'})
		;  
}]);
 
nsApp.controller('DefaultController',function($scope,$routeParams) {  
	$scope.id = $routeParams.id;
}); 

$(function(){
	$(document).ajaxSuccess(function(e,xhr,c){
		if(!xhr.responseJSON){
			return ;
		}
		if(xhr.responseJSON.s == 0){
			if(xhr.responseJSON.t == 1){
				window.location.href = "login.html";
			}else{
				showAlert(xhr.responseJSON.m);
			}
		}
	}).ajaxError(function(){
		
	});
});
  