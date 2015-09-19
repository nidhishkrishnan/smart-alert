angular.module('SmartBankApp.live.services', [])

.factory("DiscussionRoomService", function DiscussionRoomService(Restangular) {
    return {
        getAllScheduledAppointments: function() {
            return Restangular.all('appointment/getAllScheduledAppointments').customGET();
        }
        // addSaveAreasTopics: function(area) {
        //     return Restangular.all('appointment/addSaveAreasTopics').post(area);
        // },

        // deleteLanguage: function(languageId) {
        //     return Restangular.one('employee/deleteLanguage', languageId).post();
        // }
    };
});
