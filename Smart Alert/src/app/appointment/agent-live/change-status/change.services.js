angular.module('SmartBankApp.changeStatus.services', [])

.factory("ChangeStatusService", function ChangeStatusService(Restangular) {
    return {
        getScheduledAppointments: function(empId) {
            return Restangular.all('appointment/getScheduledAppointments/'+empId).customGET();
        },
        saveStatus: function(appointmentId, appointmentStatus) {
            return Restangular.all('appointment/saveAppointmentStatus/'+appointmentId+'/'+appointmentStatus).post();
        }
    };
});
