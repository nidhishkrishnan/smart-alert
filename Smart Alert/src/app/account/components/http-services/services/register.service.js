'use strict';

angular.module('SmartBankApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


