'use strict';

angular.module('SmartBankApp')

.config(function ($stateProvider) {

    $stateProvider

        .state('account.register', {
            url: "/register",
            views: {
                'account-content@account': {
                    templateUrl: 'account/register/register.html'
                }
            }
        })

});
