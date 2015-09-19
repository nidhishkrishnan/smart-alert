angular.module('SmartBankApp.agent.services', [])

.factory("AgentDiscussionRoomService", function AgentDiscussionRoomService(Restangular) {
    return {
        getAllScheduledAppointments: function() {
            return Restangular.all('appointment/getAllScheduledAppointments').customGET();
        },
        saveChatHistory: function(chat) {
            return Restangular.all('appointment/saveChatHistory').post(chat);
        },
        getChatHistory: function(roomId) {
            return Restangular.all('appointment/getChatHistory/'+roomId).customGET();
        }


        // addSaveAreasTopics: function(area) {
        //     return Restangular.all('appointment/addSaveAreasTopics').post(area);
        // },

        // deleteLanguage: function(languageId) {
        //     return Restangular.one('employee/deleteLanguage', languageId).post();
        // }
    };
});
