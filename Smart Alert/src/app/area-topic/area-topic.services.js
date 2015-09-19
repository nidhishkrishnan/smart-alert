angular.module('SmartBankApp.areasTopics.services', [])

.factory("AreasTopicsService", function AreasTopicsService(Restangular) {
    return {
        getAppointmentArea: function() {
            return Restangular.all('appointment/getAppointmentArea').customGET();
        },
        
        addSaveAreasTopics: function(area) {
            return Restangular.all('appointment/addSaveAreasTopics').post(area);
        },

        deleteLanguage: function(languageId) {
            return Restangular.one('employee/deleteLanguage', languageId).post();
        }
    };
});
