'use strict';

angular.module('SmartBankApp')

    .config(function ($stateProvider) {

        $stateProvider

            .state('home.taxagent-settings', {
                url: "/taxagent",
                views: {
                    'home-content@home': {
                        templateUrl: 'dashboard/taxagent-settings/taxagent-settings.html'
                    }
                }
            })

    });