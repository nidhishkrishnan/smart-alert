 angular.module('SmartBankApp.viewSchedules', ['SmartBankApp.viewSchedules.services'])

 .controller('ViewScheduledAppointmentsController', function ViewScheduledAppointmentsController($filter, $scope, notify, lodash, ngTableParams, ViewScheduledAppointmentsService) {
     $scope.viewScheduledAppointmentLoader = true;
     ViewScheduledAppointmentsService.getAllScheduledAppointments().then(function(appointments, status, headers, config) {
         $scope.viewScheduledAppointmentLoader = false;
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
 });