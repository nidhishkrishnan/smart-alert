angular.module('SmartBankApp.employees.schedules', ['SmartBankApp.schedules.services'])

.controller('AppointmentSchedulesController', function AppointmentSchedulesController($filter, $scope, $rootScope, $state, $stateParams, notify, lodash, ngTableParams, Restangular, AppointmentSchedulesService, CommonService) {
    $scope.labelShow = {};
    $scope.buttonShow = {};
    $scope.availability = {};
    $scope.labelStatus = {};
    $scope.scheduledAppointmentLoader = true;
    if (lodash.isUndefined($rootScope.userSession)) {
        $scope.scheduledAppointmentLoader = true;
        Restangular.all('user/isAuthenticated').customGET().then(function(response) {
            $scope.scheduledAppointmentLoader = false;
            if (response.data === null || response.message === null) {
                $state.go('main');
            } else {
                $rootScope.userSession = response.data;
                loadScheduledAppointments();
                $rootScope.apiKey = response.apiKey;
                CommonService.setUserDetails(response.data);
            }
        });
    } else {
        loadScheduledAppointments();
    }

    function loadScheduledAppointments() {
        $scope.scheduledAppointmentLoader = true;
        AppointmentSchedulesService.getScheduledAppointments($rootScope.userSession.accountType).then(function(appointments, status, headers, config) {
            $scope.scheduledAppointmentLoader = false;
            $scope.scheduledAppointmentsTable = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    appointmentDate: 'asc'
                }
            }, {
                counts: [],
                total: appointments.length,
                getData: function($defer, params) {
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(appointments, params.orderBy()) :
                        appointments;
                    $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
        });
    }

    $scope.editStatus = function(index, agentStatus)
    {
        $scope.agentStatusClone = angular.copy(agentStatus);
        $scope.labelShow[index] = false;
        $scope.buttonShow[index] = true;
    };

    $scope.setStatusLabel = function(agentStatus, index)
    {
        $scope.labelStatus[index] = angular.copy(agentStatus);
        $scope.labelShow[index] = true;
        $scope.buttonShow[index] = false;
    }

    $scope.saveStatus = function(appointmentId, agentStatus, index)
    {

      //  AppointmentSchedulesService.saveStatus(appointmentId).then(function(appointments, status, headers, config) {
            $scope.labelStatus[index] = agentStatus;
            $scope.labelShow[index] = true;
            $scope.buttonShow[index] = false;
        //});
    };

    $scope.cancel = function(index, agentStatus)
    {
        agentStatus = $scope.agentStatusClone;
        $scope.labelShow[index] = true;
        $scope.buttonShow[index] = false;
    };

    $scope.enterRoom = function(appointment) {
        $rootScope.userAppointmentDetails = appointment;
        $state.go('home.enterAgentRoom', {roomId: appointment.appointmentId});
    };
});
