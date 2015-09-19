angular.module('SmartBankApp.employees.add', ['SmartBankApp.employees.services'])

.controller('AddEmployeesController', function AddEmployeesController($log, $scope, $rootScope, $state, $stateParams, notify, lodash, Restangular, AllLanguagesAndAreas, AddEmployeesService, CommonService, AddEmployeesValue) {
    $scope.employeeAddLoader = true;
    if ($stateParams.employeeDetails !== "" && $stateParams.employeeDetails !== "staff") {
        $scope.addEditEmployees = "Edit Employees";
        $scope.data = $stateParams.employeeDetails;
        $scope.availableLanguages = AllLanguagesAndAreas.languages;
        $scope.availableAreas = AllLanguagesAndAreas.appointmentArea;
        $scope.data.areaExpert = JSON.parse($stateParams.employeeDetails.areaExpert);
        $scope.data.languages = JSON.parse($stateParams.employeeDetails.languages);
        angular.forEach($scope.availableLanguages, function(avilableLanguage, avilableLanguageKey) {
            angular.forEach($scope.data.languages, function(language, languageKey) {
                lodash.remove($scope.availableLanguages, language);
            });
        });
        angular.forEach($scope.availableAreas, function(availableArea, availableAreaKey) {
            angular.forEach($scope.data.areaExpert, function(area, areaKey) {
                lodash.remove($scope.availableAreas, area);
            });
        });
        $scope.employeeAddLoader = false;
    } else if ($stateParams.employeeDetails === "staff") {
        $scope.data = {};
        $scope.addEditEmployees = "Profile Details";
        $scope.data.languages = [];
        $scope.data.areaExpert = [];
        $scope.availableLanguages = AllLanguagesAndAreas.languages;
        $scope.availableAreas = AllLanguagesAndAreas.appointmentArea;
        if (lodash.isUndefined($rootScope.userSession)) {
            $scope.employeeAddLoader = true;
            Restangular.all('user/isAuthenticated').customGET().then(function(response) {
                $scope.employeeAddLoader = false;
                if (response.data === null || response.message === null) {
                    $state.go('main');
                } else {
                    $rootScope.userSession = response.data;
                    $rootScope.apiKey = response.apiKey;
                    CommonService.setUserDetails(response.data);
                }
                loadDetails();
            });
        } else {
            loadDetails();
        }
        $scope.employeeAddLoader = false;
    } else {
        $scope.addEditEmployees = "Add Employees";
        $scope.data = {};
        $scope.availableAreas = AllLanguagesAndAreas.appointmentArea;
        $scope.data.areaExpert = [];
        $scope.availableLanguages = AllLanguagesAndAreas.languages;
        $scope.data.languages = [];
        $scope.employeeAddLoader = false;
    }

    $scope.dateOptions = {
        changeYear: true,
        changeMonth: true,
        yearRange: '1900:-0',
        dateFormat: 'dd/mm/yy'
    };

    function loadDetails() {
        $scope.employeeAddLoader = true;
        AddEmployeesService.getEmployeeDetails($rootScope.userSession.accountType).then(function(employeeDetails) {
            $scope.data = employeeDetails;
            $scope.employeeAddLoader = false;
            $scope.data.areaExpert = JSON.parse(employeeDetails.areaExpert);
            $scope.data.languages = JSON.parse(employeeDetails.languages);
            angular.forEach($scope.availableLanguages, function(avilableLanguage, avilableLanguageKey) {
                angular.forEach($scope.data.languages, function(language, languageKey) {
                    lodash.remove($scope.availableLanguages, language);
                });
            });
            angular.forEach($scope.availableAreas, function(availableArea, availableAreaKey) {
                angular.forEach($scope.data.areaExpert, function(area, areaKey) {
                    lodash.remove($scope.availableAreas, area);
                });
            });
        });
    }

    $scope.getCountryStateCity = function(pin) {
        //$scope.employeeAddLoader = true;
        AddEmployeesService.getGeoLocation(pin).success(function(data, status, headers, config) {
            // $scope.employeeAddLoader = false;
            angular.forEach(data.results[0].address_components, function(geoLocation, key) {
                if (lodash.contains(geoLocation.types, 'country')) {
                    $scope.country = geoLocation.long_name;
                } else if (lodash.contains(geoLocation.types, 'administrative_area_level_1')) {
                    $scope.state = geoLocation.long_name;
                } else if (lodash.contains(geoLocation.types, 'administrative_area_level_2')) {
                    $scope.city = geoLocation.long_name;
                }
            });
        }).
        error(function(data, status, headers, config) {
            $scope.city = '';
            $scope.state = '';
            $scope.country = '';
        });
    };

    $scope.addEmployees = function(data) {
        data = angular.copy(data);
        data.languages = JSON.stringify(data.languages);
        data.areaExpert = JSON.stringify(data.areaExpert);
        if ($stateParams.employeeDetails !== "" && $stateParams.employeeDetails !== "staff") {
            $scope.employeeAddLoader = true;
            AddEmployeesService.saveEditedEmployees(data).then(function(saveEditResponse) {
                $scope.employeeAddLoader = false;
                if (saveEditResponse.data > 0) {
                    notify({
                        messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + saveEditResponse.message + '</span>',
                        duration: 5000,
                        classes: 'alert-success',
                        scope: $scope,
                        templateUrl: 'common/notify.html'
                    });
                    $state.go('home.viewEditEmployees');
                } else {
                    notify({
                        messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + saveEditResponse.message + '</span>',
                        duration: 5000,
                        classes: 'alert-danger',
                        scope: $scope,
                        templateUrl: 'common/notify.html'
                    });
                }
            });
        } else if ($stateParams.employeeDetails === "staff") {
            $scope.employeeAddLoader = true;
            AddEmployeesService.saveEditedEmployees(data).then(function(saveEditResponse) {
                $scope.employeeAddLoader = false;
                if (saveEditResponse.data > 0) {
                    notify({
                        messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + saveEditResponse.message + '</span>',
                        duration: 5000,
                        classes: 'alert-success',
                        scope: $scope,
                        templateUrl: 'common/notify.html'
                    });
                    $state.go('home.employees');
                } else {
                    notify({
                        messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + saveEditResponse.message + '</span>',
                        duration: 5000,
                        classes: 'alert-danger',
                        scope: $scope,
                        templateUrl: 'common/notify.html'
                    });
                }
            });
        } else {
            $scope.employeeAddLoader = true;
            AddEmployeesService.addNewEmployees(data).then(function(addNewEmployeeResponse) {
                $scope.employeeAddLoader = false;
                if (addNewEmployeeResponse.data > 0) {
                    notify({
                        messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + addNewEmployeeResponse.message + '</span>',
                        duration: 5000,
                        classes: 'alert-success',
                        scope: $scope,
                        templateUrl: 'common/notify.html'
                    });
                    $state.go('home.viewEditEmployees');
                } else {
                    notify({
                        messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + addNewEmployeeResponse.message + '</span>',
                        duration: 5000,
                        classes: 'alert-danger',
                        scope: $scope,
                        templateUrl: 'common/notify.html'
                    });
                }
            });
        }
    };
});
