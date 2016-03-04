var nsApp = angular.module('nsApp',['ngRoute']);  

nsApp.config(['$routeProvider',function ($routeProvider) {  
    $routeProvider  
        .when('/welcome', {templateUrl: 'pages/welcome.html',controller: 'DefaultController'})
        .when('/home', {templateUrl: 'pages/home.html',controller: 'DefaultController'})  
        .when('/users', {templateUrl: 'pages/users.html',controller: 'DefaultController'})  
        .when('/user', {templateUrl: 'pages/user.html',controller: 'DefaultController'})  
        .when('/gallery', {templateUrl: 'pages/gallery.html',controller: 'DefaultController'})  
        .when('/calendar', {templateUrl: 'pages/calendar.html',controller: 'DefaultController'})  
        .when('/faq', {templateUrl: 'pages/faq.html',controller: 'DefaultController'})  
        .when('/help', {templateUrl: 'pages/help.html',controller: 'DefaultController'})  
        .when('/sign-in', {templateUrl: 'pages/sign-in.html',controller: 'DefaultController'})  
        .when('/sign-up', {templateUrl: 'pages/sign-up.html',controller: 'DefaultController'})  
        .when('/reset-password', {templateUrl: 'pages/reset-password.html',controller: 'DefaultController'})  
        .when('/403', {templateUrl: 'pages/403.html',controller: 'DefaultController'})  
        .when('/404', {templateUrl: 'pages/404.html',controller: 'DefaultController'}) 
        .when('/500', {templateUrl: 'pages/500.html',controller: 'DefaultController'})  
        .when('/503', {templateUrl: 'pages/503.html',controller: 'DefaultController'}) 
        .when('/privacy-policy', {templateUrl: 'pages/privacy-policy.html',controller: 'DefaultController'})  
        .when('/terms-and-conditions', {templateUrl: 'pages/terms-and-conditions.html',controller: 'DefaultController'})  
		.otherwise({redirectTo: '/welcome'})
		;  
}]);
 
nsApp.controller('DefaultController',function($scope,$routeParams) {  
	//$scope.id = $routeParams.id;
});  


$(function(){
	$(document).ajaxSuccess(function(e,xhr,c){
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
  