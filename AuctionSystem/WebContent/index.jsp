<%@page import="beans.Client"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Auction System</title>

<script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js"></script>
<script src="js/clienthome.js"></script>
<link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="css/materialize.min.css"  media="screen,projection"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<script type="text/javascript" src="js/materialize.min.js"></script>
</head>
<body  ng-app="auction" ng-controller="auctionController">
  <nav>
    <div class="nav-wrapper">
	<span class="brand-logo">Auction System</span>
    </div>
  </nav>

	<div class="container">
	
	<div class="row"></div>
    
    <div class="row">
      <ul class="tabs">
        <li class="tab col s3 m1"><a class="active" href="#logInDiv">Log In</a></li>
        <li class="tab col s3 m1"><a href="#createAccountDiv">Create Account</a></li>
      </ul>
	</div>
	
	<div id="logInDiv">
		<div class="row">
			<div class="col s12 m6 offset-m3">
				<form id="loginForm">
					<div class="row">
						<div class="input-field col s12">
							<input id="login_id" type="text" name="login_id" 
								class="validate" ng-hitenter="login()"/>
							<label for="login_id">Login Id</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input id="login_password" type="password" 
								name="login_password" class="validate"  
								ng-model="login.password"
								ng-hitenter="login()"/>
							<label for="login_password">Password</label>
						</div>
					</div>
					<button ng-click="login()" type="button"  class="waves-effect waves-light btn">Login</button>
				</form>
			</div>
		</div>
	</div>
	    
	<div id="createAccountDiv">
		<div class="row">
			<div class="col s12 m6 offset-m3">
				<form id="createAccountForm" name="createAccountForm">
					<div class="row">
						<div class="input-field col s12">
							<input id="name" type="text" name="create_name" class="validate" ng-model="create.name"/>
							<label for="name">Name</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input id="login" type="text" name="login_id" class="validate" ng-model="create.login_id"/>
							<label for="login">Login Id</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input id="password" type="password" 
								name="create_password" class="validate"  
								ng-model="create.password"/>
							<label for="password">Password</label>
						</div>
					</div>
					<div class="row">
						<div class="input-field col s12">
							<input id="confirm_password" type="password" name="confirm_password"
								class="validate"  ng-model="create.confirmPassword"
								ng-hitenter="createAccount()"/>
							<label for="confirm_password">Confirm Password</label>
						</div>
						<p ng-show="create.confirmPassword != create.password && createAccountForm.confirm_password.$dirty">
							Passwords do not match!
						</p>
					</div>
					<button ng-click="createAccount()" type="button" 
						class="waves-effect waves-light btn">Create</button>
				</form>
			</div>
		</div>
	</div>
	    
	</div>
</body>
</html>