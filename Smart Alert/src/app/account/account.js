angular.module('SmartBankApp.account', [])

.controller('CreateNewCtrl', function CreateNewCtrl($scope, $rootScope, $modalInstance, Restangular, AccountModalData) {
    $scope.verification = AccountModalData.verification;
    $scope.basicDetails = AccountModalData.basicDetails;
    $scope.nextButton = AccountModalData.nextButton;
    $scope.message = AccountModalData.message;
    $scope.userId = AccountModalData.userId;
    $scope.next = function() {
        $scope.verification = true;
        $scope.basicDetails = false;
        $scope.nextButton = false;
    };

    $scope.ok = function() {
        $modalInstance.close();
    };

    $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
    };

    $scope.createNewUserAccount = function(newUser) {
        $scope.createAccountLoader = true;
        Restangular.all('user/createUserAccount').post(newUser).then(function(serverResponse) {
            //$modalInstance.close(serverResponse);
            $scope.createAccountLoader = false;
            $scope.verification = true;
            $scope.basicDetails = false;
            $scope.nextButton = false;
            $scope.userId = serverResponse.data;
        });
    };

    $scope.validateCode = function(code) {
        $scope.createAccountLoader = true;
        Restangular.one('user/verifyMobile/' + code, $scope.userId).post().then(function(serverResponse) {
            $scope.verification = true;
            $scope.createAccountLoader = false;
            $scope.basicDetails = false;
            $scope.nextButton = false;
            if (serverResponse.message === "success") {
                $modalInstance.close(serverResponse);
            } else {
                $scope.message = serverResponse.userAccount;
                $scope.verificationCode = '';
            }
        });
    };
});
