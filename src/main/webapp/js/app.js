var nsApp = angular.module('nsApp',['ngRoute']);  

nsApp.config(['$routeProvider',function ($routeProvider) {  
    $routeProvider
        .when('/welcome', {templateUrl: 'pages/welcome.html',controller: 'DefaultController'})
        .when('/users', {templateUrl: 'pages/users.html',controller: 'DefaultController'})  
        .when('/productmanage', {templateUrl: 'pages/productmanage.html',controller: 'ProductManageController'})
        .when('/productadd', {templateUrl: 'pages/productadd.html',controller: 'ProductAddController'})
        .when('/productupdate', {templateUrl: 'pages/productupdate.html',controller: 'ProductUpdateController'})
        .when('/noticemanage', {templateUrl: 'pages/noticemanage.html',controller: 'NoticeManageController'})
        .when('/noticeadd', {templateUrl: 'pages/noticeadd.html',controller: 'NoticeAddController'})
        .when('/noticeupdate', {templateUrl: 'pages/noticeupdate.html',controller: 'NoticeUpdateController'})
        .when('/msgreceiver', {templateUrl: 'pages/msgreceiver.html',controller: 'MsgReceiverController'})
        .when('/msgreceiveradd', {templateUrl: 'pages/msgreceiveradd.html',controller: 'MsgReceiverAddController'})
        .when('/msglist', {templateUrl: 'pages/msglist.html',controller: 'MsgListController'})
        .when('/msgdetail', {templateUrl: 'pages/msgdetail.html',controller: 'MsgDetailController'})
        .when('/msgsender', {templateUrl: 'pages/msgsender.html',controller: 'MsgSenderController'})
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
  