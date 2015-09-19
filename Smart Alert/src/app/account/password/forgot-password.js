'use strict';
angular.module('SmartBankApp')

.controller('ForgotPasswordCtrl', function ForgotPasswordCtrl($scope, $modalInstance) {
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
})


