 angular.module('SmartBankApp.changeStatus', ['SmartBankApp.changeStatus.services'])

 .controller('ChangeAppointmentStatusController', function ChangeAppointmentStatusController($scope, $stateParams, $interval, $rootScope, $state, notify, lodash, ChangeStatusService, Restangular, CommonService) {
     if (lodash.isUndefined($rootScope.userSession)) {
         $scope.changeAppointmentLoader = true;
         Restangular.all('user/isAuthenticated').customGET().then(function(response) {
             if (response.data === null || response.message === null) {
                 $state.go('main');
             } else {
                 $rootScope.userSession = response.data;
                 loadScheduledAppointments();
             }
         });
     } else {
         loadScheduledAppointments();
     }

     function loadScheduledAppointments() {
         ChangeStatusService.getScheduledAppointments($rootScope.userSession.accountType).then(function(response) {
             $scope.changeAppointmentLoader = false;
             $scope.allAppointments = response;
         });
     }

     $scope.saveStatus = function(data) {
        $scope.changeAppointmentLoader = true;
         ChangeStatusService.saveStatus(data.appointmentId, data.appointmentStatus).then(function(response) {
            $scope.changeAppointmentLoader = false;
             if (response.data) {
                 notify({
                     messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + response.message + '</span>',
                     duration: 5000,
                     classes: 'alert-success',
                     scope: $scope,
                     templateUrl: 'common/notify.html'
                 });
                  $state.go('home.scheduledAppointments');
             } else {
                 notify({
                     messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + response.message + '</span>',
                     duration: 5000,
                     classes: 'alert-danger',
                     scope: $scope,
                     templateUrl: 'common/notify.html'
                 });
             }
         });
     };
 });
