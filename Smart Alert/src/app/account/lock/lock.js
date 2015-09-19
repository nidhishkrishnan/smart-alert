'use strict';

angular.module('SmartBankApp')

    .config(function ($stateProvider) {

        $stateProvider

            .state('account.lock', {
                url: "/lock",
                views: {
                    'account-content@account': {
                        templateUrl: 'account/lock/lock.html'
                    }
                }
            })

    });