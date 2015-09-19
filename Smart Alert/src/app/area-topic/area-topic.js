 angular.module('SmartBankApp.areasTopics', ['SmartBankApp.areasTopics.services'])

 .controller('AddEditAreasTopicsController', function AddEditAreasTopicsController($filter, $scope, $state, $modal, $stateParams, notify, lodash, ngTableParams, AreasTopicsService) {
     $scope.data = {};
     $scope.appointmentAreaTable;
     $scope.areaLoader = true;
     $scope.topicName = '';
     $scope.showTopicErrorMessage = false;
     $scope.data.appointmentAreaTopics = [];
     $scope.isEdited = false;

     $scope.reset = function() {
         $scope.data = {};
         $scope.buttonLabel = "Add Area"
         $scope.addEditAreasTopics = "Add Area and Topics";
         $scope.addEditAreasTopicsLabel = "Enter Area and its related topics";
         $scope.data.appointmentAreaTopics = [];
         $scope.data.name = '';
         $scope.data.description = '';
         $scope.isEdited = false;
         $scope.errorMessage = "";
         $scope.showTopicErrorMessage = false;
     };

     $scope.reset();
     AreasTopicsService.getAppointmentArea().then(function(appointmentAreas, status, headers, config) {
         $scope.areaLoader = false;
         $scope.allAppointmentAreas = appointmentAreas;
         reloadLanguageTable();
     });

     $scope.addNewTopic = function(topicName) {
         if (lodash.findIndex($scope.data.appointmentAreaTopics, {
                 'topicName': topicName
             }) === -1 && !lodash.isEmpty(topicName)) {
             $scope.data.appointmentAreaTopics.push({
                 'topicName': topicName
             });
             $scope.topicName = '';
             $scope.showTopicErrorMessage = false;
             $scope.errorMessage = '';
         } else if (lodash.isEmpty(topicName)) {
             $scope.showTopicErrorMessage = true;
             $scope.errorMessage = "Topic cannot be empty!!!"
         } else if (lodash.findIndex($scope.data.appointmentAreaTopics, {
                 'topicName': topicName
             }) >= 0) {
             $scope.showTopicErrorMessage = true;
             $scope.errorMessage = "Topic already added!!!";
         }
     };

     $scope.deleteTopic = function(topic) {
         lodash.pull($scope.data.appointmentAreaTopics, topic);
     };

     $scope.editAreaTopic = function(area) {
         $scope.data.appointmentAreaTopics = [];
         $scope.isEdited = true;
         $scope.buttonLabel = "Edit Area"
         $scope.addEditAreasTopics = "Edit Area and Topics";
         $scope.addEditAreasTopicsLabel = "Edit Area and its related topics";
         angular.forEach(area.appointmentAreaTopics, function(topic, key) {
             $scope.data.appointmentAreaTopics.push(topic);
         });
         $scope.data.areaId = area.areaId;
         $scope.data.name = area.name;
         $scope.data.description = area.description;
         $scope.buttonLabel = "Save"
     };

     $scope.isDisabledOrNot = function() {
         if ($scope.data.name.length > 0 && $scope.data.description.length > 0 && $scope.data.appointmentAreaTopics.length > 0) {
             return false;
         } else {
             return true;
         }
     };

     $scope.addSaveAreas = function() {
         console.log(angular.toJson($scope.data));
         // var language = {};
         // language.languageName = $scope.language;
         // if ($scope.languageId !== "") {
         //     language.id = $scope.languageId;
         // }
         $scope.areaLoader = true;
         AreasTopicsService.addSaveAreasTopics($scope.data).then(function(addSaveAreasTopicsResponse, status, headers, config) {

             if (addSaveAreasTopicsResponse.data !== "") {
                 AreasTopicsService.getAppointmentArea().then(function(appointmentAreas, status, headers, config) {
                     $scope.areaLoader = false;
                     notify({
                         messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + addSaveAreasTopicsResponse.message + '</span>',
                         duration: 5000,
                         classes: 'alert-success',
                         scope: $scope,
                         templateUrl: 'common/notify.html'
                     });
                     $scope.allAppointmentAreas = appointmentAreas;
                     $scope.appointmentAreaTable.reload();
                     $scope.reset();
                 });
                 $scope.appointmentAreaTable.reload();
             } else {
                 $scope.areaLoader = false;
                 notify({
                     messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + addSaveAreasTopicsResponse.message + '</span>',
                     duration: 5000,
                     classes: 'alert-danger',
                     scope: $scope,
                     templateUrl: 'common/notify.html'
                 });
             }
         });


         // AreasTopicsService.addSaveLanguage(language).then(function(addSaveLanguageResponse, status, headers, config) {
         //    
         // });
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
                 AreasTopicsService.deleteLanguage(languageId).then(function(serverResponse) {
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
         $scope.appointmentAreaTable = new ngTableParams({
             page: 1,
             count: 10,
             sorting: {
                 name: 'asc'
             }
         }, {
             total: $scope.allAppointmentAreas.length,
             getData: function($defer, params) {
                 var orderedData = params.sorting() ?
                     $filter('orderBy')($scope.allAppointmentAreas, params.orderBy()) :
                     $scope.allAppointmentAreas;
                 $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
             }
         });
     }
 });
