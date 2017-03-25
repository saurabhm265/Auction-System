/**
 * 
 */

var app = angular.module("auction", []);

app.directive('ngHitenter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngHitenter);
                });
                event.preventDefault();
            }
        });
    };
});

app.controller("auctionController", function($scope) {

	$('ul.tabs').tabs();
	
	$.get( 'GetMyItems', {}, function(data) {
		$scope.myitems = data;
		$scope.$apply();
	});

	$.get( 'SearchItems', {}, function(data) {
		$scope.searchitems = data;
		$scope.$apply();
		Materialize.showStaggeredList('#staggered-result',1000);
	});

	$.get( 'BidWins	', {}, function(data) {
		$scope.bidwins = data;
		$scope.$apply();
	});
	
	$scope.publish = function() {
    	$.post( 'Publish', $("#publishForm").serialize(), function(data) {
    		console.log(data);
			Materialize.toast($scope.new_item.name + " Published.", 5000 );
//        	alert($scope.new_item.name + " Published.");
        	$scope.new_item = {};
    		$scope.$apply();
    	});
    	$scope.myitems.push($scope.new_item);
	}
	
	$scope.search = function() {
		$.get( 'SearchItems', {'searchStr': $scope.searchStr}, function(data) {
			$scope.searchitems = data;
			$scope.$apply();
			Materialize.showStaggeredList('#staggered-result',1000);
		});
	}
	
	$scope.bid = function(item) {
		if (item.mybid <= item.currentbid) {
			Materialize.toast("Please bid higher than the current bid.", 3000 );
//			alert("Please bid higher than the current bid.")
			return;
		}
		if (item.mybid <= item.minBid) {
			Materialize.toast("Please bid higher than the minimum bid.", 3000 );
//			alert("Please bid higher than the current bid.")
			return;
		}
		
		$.post( 'Bid', 
    			{	'itemId':item.id,
    				'amount':item.mybid
    				},
    			function(data) {
    				item.currentbid = item.mybid;
    				$scope.$apply();
    			});
	}
	
	$scope.showSearchPage = true;
	$scope.showSP = function(fl) {
		$scope.showSearchPage = fl;
		$scope.$apply();
	}
	
	$scope.save = function(item){
		item.editItem = false;
	    $.post( 'UpdateItem', $("#updateItemForm").serialize(), function(data) {
	    	console.log(data);
			Materialize.toast(item.name + " Updated.", 5000 );
//	        alert($scope.new_item.name + " Published.");
	    });
	};
	
	$scope.sell = function(item){
	    $.post( 'Sold', {item_id: item.id}, function(data) {
	    	console.log(data);
	    	item.sold = 'Y';
			$scope.$apply();
			Materialize.toast(item.name + " Sold.", 5000 );
//	        alert($scope.new_item.name + " Published.");
	    });
	};
	
	$scope.createAccount = function() {
    	$.post( 'CreateAccount', $("#createAccountForm").serialize(), function(data) {
    		console.log(data);
    		if (data > 0)
    			window.location = "clienthome.jsp";
    		else{
    			Materialize.toast("User id already taken, please try another Login id.", 3000 );
//    			alert("User id already taken, please try another Login id.");
    		}
    	});
	};
	
	$scope.login = function() {
    	$.post( 'Login', $("#loginForm").serialize(), function(data) {
    		console.log(data);
    		if (data > 0)
    			window.location = "clienthome.jsp";
    		else{
    			Materialize.toast("Invalid Username or Password.", 5000 );
//    			alert("Invalid Username or Password.");
    		}
    	});
	}
});
