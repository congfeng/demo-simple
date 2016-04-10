var cur_tab;
$(function(){
	$('.maintab div').click(function(){
		if(cur_tab == this.id){
			return ;
		}
		cur_tab = this.id;
		$('.maintab div i,span').css('color','#333');
		$(this.querySelectorAll('i,span')).css('color','#00CC99');
		window.location.href='#/'+this.id;
	});
})
document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

var myApp = angular.module('myApp',['ngRoute']);  
myApp.config(['$routeProvider',function ($routeProvider) {  
    $routeProvider  
        .when('/tab1', {templateUrl: 'tab1.html',controller: 'DefaultController'})
        .when('/tab2', {templateUrl: 'tab2.html',controller: 'DefaultController'})  
        .when('/tab3', {templateUrl: 'tab3.html',controller: 'DefaultController'})  
        .when('/tab4', {templateUrl: 'tab4.html',controller: 'DefaultController'})    
		.otherwise({redirectTo: '/welcome'})
		;  
}]);

myApp.controller('DefaultController',function($scope,$routeParams) {  
	//$scope.id = $routeParams.id;
});
