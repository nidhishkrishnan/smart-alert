'use strict';

angular.module('SmartBankApp')

    .config(function ($stateProvider) {

        $stateProvider

            .state('home.profile-settings', {
                url: "/profile",
                views: {
                    'home-content@home': {
                        templateUrl: 'dashboard/profile-settings/profile-settings.html'
                    }
                }
            })

    });