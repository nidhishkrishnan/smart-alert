angular.module('SmartBankApp.schedules.services', [])

.factory("AppointmentSchedulesService", function AppointmentSchedulesService($http, Restangular) {
    return {
        getScheduledAppointments: function(empId) {
            return Restangular.all('appointment/getScheduledAppointments/'+empId).customGET();
        },
        saveStatus: function(appointmentId, status) {
            return Restangular.all('appointment/saveStatus/'+appointmentId+'/'+status).post();
        },
    };
});
