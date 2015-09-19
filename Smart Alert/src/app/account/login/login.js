'use strict';

angular.module('SmartBankApp')

    .config(function ($stateProvider) {

        $stateProvider

            .state('account.login', {
                url: "/login",
                views: {
                    'account-content@account': {
                        templateUrl: 'account/login/login.html'
                    }
                }
            })

    });