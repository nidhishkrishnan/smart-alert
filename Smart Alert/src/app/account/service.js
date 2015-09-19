angular.module('SmartBankApp.account.service', [])

 .run(['validator', 'defaultErrorMessageResolver', function(validator, defaultErrorMessageResolver) {
     defaultErrorMessageResolver.getErrorMessages().then(function(errorMessages) {
         errorMessages['createAccountUsernameRequired'] = 'User name is required';
         errorMessages['createAccountEmailRequired'] = 'Email is required';
         errorMessages['createAccountMobileRequired'] = 'Mobile is required';
     });
 }])

.factory("AccountService", function(Restangular) {
    return {
        createNewUserAccount: function(account) {
            Restangular.all('user/createUserAccount').post(account).then(function(response) {
                return response;
            });
        }
    }
})


