'use strict';

angular.module('SmartBankApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
