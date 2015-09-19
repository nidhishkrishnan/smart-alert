angular.module('SmartBank.form.controls',[])
	.directive('iText', function(){
		// Runs during compile
		return {
			scope: {}, // {} = isolate, true = child, false/undefined = no change
			restrict: 'EA', // E = Element, A = Attribute, C = Class, M = Comment
			template: "<input type = 'text' class = 'form-control' />",
			replace: true
		};
	})
	.directive('iPwd', function(){
		// Runs during compile
		return {
			scope: {}, // {} = isolate, true = child, false/undefined = no change
			restrict: 'EA', // E = Element, A = Attribute, C = Class, M = Comment
			template: "<input type = 'password' class = 'form-control' />",
			replace: true
		};
	})
	.directive('iEmail', function(){
		return {
			scope: {}, // {} = isolate, true = child, false/undefined = no change
			restrict: 'EA', // E = Element, A = Attribute, C = Class, M = Comment
			template: "<input type = 'email' class = 'form-control' />",
			replace: true
		};
	});