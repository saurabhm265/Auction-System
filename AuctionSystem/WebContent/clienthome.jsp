<%@page import="beans.Client"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    <%
    Client client = (Client)session.getAttribute("client");
	if (client == null){
		response.sendRedirect("index.jsp");
	}

    %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="js/clienthome.js"></script>
<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<script type="text/javascript" src="js/materialize.min.js"></script>
</head>
<body ng-app="auction" ng-controller="auctionController">

  <nav>
    <div class="nav-wrapper">
	<span class="brand-logo">Auction System</span>
	<ul class="right">
      <li><a href="Logout">Logout</a></li>
    </ul>
    </div>
  </nav>

<div class="container">
	<div class="row"></div>
    
    <div class="row">
      <ul class="tabs">
        <li class="tab col s3 m1"><a class="active" href="#searchDiv">Search</a></li>
        <li class="tab col s3 m1"><a href="#bidWins">Bid Wins</a></li>
        <li class="tab col s3 m1"><a href="#myItemDiv">My Postings</a></li>
        <li class="tab col s3 m1"><a href="#postItem">Post Item</a></li>
      </ul>
	</div>    

	<div id="postItem">
		<div class="row">
			<div class="col s12 m6 offset-m3">
				<form id="publishForm">
					<div class="row">
						<div class="input-field col s12">
							<input id="item_name" type="text" name="name" class="validate" ng-model="new_item.name"/>
							<label for="item_name">Item name</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input id="description" type="text" name="description" class="validate"  ng-model="new_item.description"/>
							<label for="description">Description</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input id="min_bid" type="number" name="min_bid" class="validate" ng-model="new_item.minBid"/>
							<label for="min_bid">Minimum Bid</label>
						</div>
					</div>
					<button ng-click="publish()" type="button"  class="waves-effect waves-light btn">Publish</button>
				</form>
			</div>
		</div>
	</div>

	<div id="bidWins">
				<div class="row">
					<div ng-repeat="item in bidwins"  class="row" style="margin-bottom: 0px">
							<div class="card">
							<div class="card-content">
								<span class="card-title">
									{{item.name}}
								</span>
								<p>{{item.description}}</p>
							</div>
							<div class="card-action">
								<div class="row" style="margin-bottom: 0px">
									<div class="col s4">
										<span>Won at </span>
										<h5>{{item.currentbid != -1 ? item.currentbid : 'No bids'}}</h5>
									</div>
								</div>
							</div>
							</div>
						</div>
					</div>

			<div ng-show="bidwins.length == 0">You have not won any item yet!</div>
		</div>	
	
	<div id="myItemDiv">
				<div class="row">
					<div ng-repeat="item in myitems"  class="row" style="margin-bottom: 0px">
							<form id="updateItemForm" >
							<div class="card">
							<div class="card-content">
								<span class="card-title">
									<span ng-show="!item.editItem" >{{item.name}}</span>
									<input type="hidden" name="item_id" value="{{item.id}}" />
									<input type="text" name="name" ng-show="item.editItem" ng-model="item.name"/>
								</span>
								<p ng-show="!item.editItem">{{item.description}}</p>
								<input type="text" name="description" ng-show="item.editItem" ng-model="item.description"/>
							</div>
							<div class="card-action">
								<div class="row" style="margin-bottom: 0px">
									<div class="col s4">
										Minimum bid 
										<h5 ng-show="!item.editItem">{{item.minBid}}</h5>
										<input type="number" name="min_bid" ng-show="item.editItem" ng-model="item.minBid"/>
									</div>
									<div class="col s4">
										<span ng-show="item.sold == 'N'" >Current bid </span>
										<span ng-show="item.sold == 'Y'" >Sold to {{item.soldTo}} at</span>
										<h5>{{item.currentbid != -1 ? item.currentbid : 'No bids'}}</h5>
									</div>
									<div class="col s4">
										<button ng-show="!item.editItem" ng-click="item.editItem = true"
											class="waves-effect waves-light btn">Edit</button>
										<button ng-show="item.editItem" ng-click="save(item)"
											class="waves-effect waves-light btn">Save</button>
										<button ng-show="item.sold == 'N'" ng-click="sell(item)"
											class="waves-effect waves-light btn">Sell</button>
									</div>
								</div>
							</div>
							</div>
							</form>
						</div>
					</div>

			<div ng-show="myitems.length == 0">You have not posted any item yet!</div>
		</div>	
	
	<div id="searchDiv">
		<nav>
			<div class="nav-wrapper">
				<form>
					<div class="input-field">
						<input id="search" ng-change="search()" 
							type="search" ng-model="searchStr" 
							ng-model-options='{ debounce: 1000 }'
							ng-hitenter="search()"/> 
						<label for="search">
							<i class="material-icons">search</i>
						</label>
						<i class="material-icons" ng-click="searchStr=''">close</i>
					</div>
				</form>
			</div>
			</nav>
			
			<div class="row"></div>
			<div class="row">
				<ul id="staggered-result">
					<li ng-repeat="item in searchitems"  class="row" style="margin-bottom: 0px; opacity: 0;">
						<div class="card">
						<div class="card-content">
							<span class="card-title">{{item.name}}</span>
							<p>{{item.description}}</p>
						</div>
							<div class="card-action">
								<div class="row" style="margin-bottom: 0px">
									<div class="col s4">
										Current bid 
										<h5>{{item.currentbid != 0 ? item.currentbid : item.minBid}}</h5>
									</div>
									<div class="input-field col s5 m2 offset-m4 ">
										<input id="bid_amount_{{item.id}}" type="number" ng-model="item.mybid" />
										<label for="bid_amount_{{item.id}}">Bid Amount</label>
									</div>
									<div class="col s3 m1">
										<button ng-click="bid(item)" class="waves-effect waves-light btn">Bid</button>
									</div>
								</div>
							</div>
							</div>
						</div>
				</ul>	
			</div>

			<div ng-show="searchitems.length == 0">No Results found</div>
		</div>

	</div>

</body>
</html>