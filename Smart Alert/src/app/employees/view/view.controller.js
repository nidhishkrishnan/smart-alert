angular.module('SmartBankApp.employees.view', ['SmartBankApp.employees.edit.services'])

.controller('ViewEditEmployeesController', function ViewEditEmployeesController($scope, $state, $stateParams, $modal, $filter, notify, lodash, Restangular, ViewEditEmployeesValue, ViewEditEmployeesService, ngTableParams) {
    $scope.employeeViewLoader = true;
    ViewEditEmployeesService.getAllEmployees().then(function(employee, status, headers, config) {
        $scope.employeeViewLoader = false;
        $scope.employeeTable = new ngTableParams({
            page: 1,
            count: 10,
            sorting: {
                firstname: 'asc'
            }
        }, {
            total: employee.length,
            getData: function($defer, params) {
                var orderedData = params.sorting() ?
                    $filter('orderBy')(employee, params.orderBy()) :
                    employee;
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    });

    $scope.getJsonObject = _.memoize(function(languagesList) {
        languagesList = JSON.parse(languagesList);
        return languagesList;
    });

    $scope.editEmployee = function(employee) {
        console.log(JSON.stringify(employee));
        $state.go('home.editEmployees', {
            employeeDetails: employee
        });
    };

    $scope.deleteEmployee = function(employeeId) {
        deleteEmployee(employeeId);
    };

    function deleteEmployee(employeeId) {
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
                $scope.employeeViewLoader = true;
                ViewEditEmployeesService.deleteEmployee(employeeId).then(function(serverResponse) {
                    $scope.employeeViewLoader = false;
                    if (serverResponse.data > 0) {
                        notify({
                            messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + serverResponse.message + '</span>',
                            duration: 5000,
                            classes: 'alert-success',
                            scope: $scope,
                            templateUrl: 'common/notify.html'
                        });
                        $state.go($state.current.name, $state.params, {
                            reload: true
                        });
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
});
