 angular.module('SmartBankApp.language', ['SmartBankApp.language.services'])
 
 .controller('AddEditLanguageController', function AddEditLanguageController($filter, $scope, $state, $modal, $stateParams, notify, lodash, ngTableParams, LanguageService) {
     $scope.languagesTable;
     $scope.languageLoader = true;
     $scope.reset = function() {
         $scope.buttonLabel = "Add"
         $scope.addEditLanguage = "Add Language";
         $scope.addEditLanguageLabel = "Enter and add the language name";
         $scope.languageId = "";
         $scope.language = "";
     };
     $scope.reset();
     LanguageService.getAllLanguages().then(function(languages, status, headers, config) {
         $scope.allLanguages = languages;
         $scope.languageLoader = false;
         reloadLanguageTable();
     });

     $scope.editLanguage = function(language) {
         $scope.addEditLanguage = "Edit Language";
         $scope.addEditLanguageLabel = "Edit and save the language name";
         $scope.language = language.languageName;
         $scope.languageId = language.id;
         $scope.buttonLabel = "Save"
     };
     $scope.addSaveLanguage = function() {
         var language = {};
         language.languageName = $scope.language;
         if ($scope.languageId !== "") {
             language.id = $scope.languageId;
         }
         $scope.languageLoader = true;
         LanguageService.addSaveLanguage(language).then(function(addSaveLanguageResponse, status, headers, config) {
            $scope.languageLoader = false;
             if (addSaveLanguageResponse.data !== "") {
                 notify({
                     messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + addSaveLanguageResponse.message + '</span>',
                     duration: 5000,
                     classes: 'alert-success',
                     scope: $scope,
                     templateUrl: 'common/notify.html'
                 });
                 $scope.allLanguages = addSaveLanguageResponse.data;
                 $scope.languagesTable.reload();
                 $scope.reset();
             } else {
                 notify({
                     messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + addSaveLanguageResponse.message + '</span>',
                     duration: 5000,
                     classes: 'alert-danger',
                     scope: $scope,
                     templateUrl: 'common/notify.html'
                 });
             }
         });
     };

     $scope.deleteLanguage = function(languageId) {
         deleteLanguage(languageId);
     };

     function deleteLanguage(languageId) {
         var modalInstance = $modal.open({
             templateUrl: 'common/yes-no/yes-no.html',
             controller: function($scope, $modalInstance) {
                 $scope.ok = function() {
                     $modalInstance.close('yes');
                 };

                 $scope.cancel = function() {
                     $modalInstance.dismiss('no');
                 };
             },
             size: 'sm',
             windowClass: "center-modal animated animated fadeIn",
             backdrop: 'static'
         });

         modalInstance.result.then(function(response) {
             if (response === 'yes') {
                 $scope.languageLoader = true;
                 LanguageService.deleteLanguage(languageId).then(function(serverResponse) {
                     $scope.languageLoader = false;
                     if (serverResponse.data !== "") {
                         notify({
                             messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + serverResponse.message + '</span>',
                             duration: 5000,
                             classes: 'alert-success',
                             scope: $scope,
                             templateUrl: 'common/notify.html'
                         });
                         $scope.allLanguages = serverResponse.data;
                         $scope.languagesTable.reload();
                         $scope.reset();
                     } else {
                         notify({
                             messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + serverResponse.message + '</span>',
                             duration: 5000,
                             classes: 'alert-danger',
                             scope: $scope,
                             templateUrl: 'common/notify.html'
                         });
                     }
                 });
             }
         });
     }

     function reloadLanguageTable() {
         $scope.languagesTable = new ngTableParams({
             page: 1,
             count: 10,
             sorting: {
                 languageName: 'asc'
             }
         }, {
             total: $scope.allLanguages.length,
             getData: function($defer, params) {
                 var orderedData = params.sorting() ?
                     $filter('orderBy')($scope.allLanguages, params.orderBy()) :
                     $scope.allLanguages;
                 $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
             }
         });
     }
 });
