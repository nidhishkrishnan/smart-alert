 angular.module('SmartBankApp.employees', ['SmartBankApp.employees.add', 'SmartBankApp.employees.view'])

 .run(['validator', 'defaultErrorMessageResolver', function(validator, defaultErrorMessageResolver) {
     defaultErrorMessageResolver.getErrorMessages().then(function(errorMessages) {
         errorMessages['firstNameRequired'] = 'First name is required';
         errorMessages['lastNameRequired'] = 'Last name is required';
         errorMessages['genderRequired'] = 'Gender is required';
         errorMessages['dobRequired'] = 'Date of birth is required';
         errorMessages['martialStatusRequired'] = 'Martial status is required';
         errorMessages['languagesRequired'] = 'Languages is required';
         errorMessages['addressRequired'] = 'Address is required';
         errorMessages['pinRequired'] = 'Postal code is requried';
         errorMessages['mobileRequired'] = 'Mobile is required';
         errorMessages['emailRequired'] = 'Email is required';
     });
 }])
