angular.module('SmartBankApp.language.services', [])

.factory("LanguageService", function LanguageService(Restangular) {
    return {
        getAllLanguages: function() {
            return Restangular.all('employee/getAllLanguages').customGET();
        },
        
        addSaveLanguage: function(language) {
            return Restangular.all('employee/addSaveLanguage').post(language);
        },

        deleteLanguage: function(languageId) {
            return Restangular.one('employee/deleteLanguage', languageId).post();
        }
    };
});
