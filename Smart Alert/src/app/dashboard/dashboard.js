
angular.module('SmartBankApp')

.run(['validator', 'defaultErrorMessageResolver', '$rootScope', function(validator, defaultErrorMessageResolver, $rootScope) {
    defaultErrorMessageResolver.getErrorMessages().then(function(errorMessages) {
        errorMessages['loginUserNameRequired'] = 'Username is required';
        errorMessages['loginPasswordRequired'] = 'Password is required';
    });
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
        if (fromState.url === "/room/:roomId") {
            $rootScope.customerInvalidate();
        }
        if (fromState.url === "/enterAgentRoom/:roomId") {
            $rootScope.agentInvalidate();
        }
        console.log(fromState.url);
    });
    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
        // console.log('$fromState:', fromState);
        //console.log('$toState:', toState);
    });
}])

.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider

        .state('main', {
        url: "/main",
        parent: 'site',
        views: {
            'site-content@': {
                templateUrl: 'common/main.html',
                controller: 'MainCtrl'
            }
        }
    })

    .state('home', {
        abstract: true,
        parent: 'site',
        views: {
            'site-content@': {
                templateUrl: 'dashboard/home-layout.html',
                controller: 'HomeCtrl'
            }
        }
    })

    .state('home.dashboard', {
            url: "/home",
            views: {
                'home-content@home': {
                    templateUrl: 'dashboard/dashboard.html',
                    controller: 'DashBoardController'
                }
            }
        })
        .state('home.query', {
            url: "/query",
            views: {
                'home-content@home': {
                    templateUrl: 'query/query.html',
                    controller: 'QueryCtrl'
                }
            }
        })
        .state('home.notification', {
            url: "/notification",
            views: {
                'home-content@home': {
                    templateUrl: 'notification/notification.html',
                    controller: 'NotificationController'
                }
            }
        })
        /*.state('home.viewAppointment', {
            url: "/viewAppointment",
            views: {
                'home-content@home': {
                    templateUrl: 'appointment/viewAppointment.html',
                    controller: 'ViewAppointmentController'
                }
            }
        })
        .state('area', {
            url: '/area',
            parent: 'home.addAppointment',
            views: {
                'view-test': {
                    templateUrl: 'appointment/views/selectArea.html',
                    controller: 'AppointmentAreaController'
                }
            }
        })
        .state('topics', {
            url: '/topics',
            parent: 'home.addAppointment',
            views: {
                'view-test': {
                    templateUrl: 'appointment/views/selectTopics.html',
                    controller: 'AppointmentTopicsController'
                }
            },
            params: {
                topicDetails: ''
            }
        })
        .state('time', {
            url: '/time',
            parent: 'home.addAppointment',
            views: {
                'view-test': {
                    templateUrl: 'appointment/views/selectTime.html',
                    controller: 'AppointmentTimeController'
                }
            },
            params: {
                topicDetails: ''
            }
        })
        .state('home.enterRoom', {
            url: "/room/:roomId",
            views: {
                'home-content@home': {
                    templateUrl: 'appointment/live/live.html',
                    controller: 'DiscussionRoomController'
                }
            }
        })
        .state('home.enterAgentRoom', {
            url: "/enterAgentRoom/:roomId",
            views: {
                'home-content@home': {
                    templateUrl: 'appointment/agent-live/agent.html',
                    controller: 'AgentLiveRoomController'
                }
            }
        })
        .state('home.addEmployees', {
            url: "/addEmployees",
            views: {
                'home-content@home': {
                    templateUrl: 'employees/add/add.html',
                    controller: 'AddEmployeesController'
                }
            },
            resolve: {
                AllLanguagesAndAreas: function(Restangular, $q) {
                    return Restangular.all('employee/getAllLanguagesAndAreas').customGET();
                }
            },
            params: {
                employeeDetails: ''
            }
        })
        .state('home.editEmployees', {
            url: "/editEmployees",
            views: {
                'home-content@home': {
                    templateUrl: 'employees/add/add.html',
                    controller: 'AddEmployeesController'
                }
            },
            resolve: {
                AllLanguagesAndAreas: function(Restangular) {
                    return Restangular.all('employee/getAllLanguagesAndAreas').customGET();
                }
            },
            params: {
                employeeDetails: ''
            }
        })
        .state('home.viewEditEmployees', {
            url: "/viewEditEmployees",
            views: {
                'home-content@home': {
                    templateUrl: 'employees/view/view.html',
                    controller: 'ViewEditEmployeesController'

                }
            }
        })
        .state('home.addEditLanguages', {
            url: "/addEditLanguages",
            views: {
                'home-content@home': {
                    templateUrl: 'language/language.html',
                    controller: 'AddEditLanguageController'
                }
            }
        })
        .state('home.addEditAreasTopics', {
            url: "/addEditAreasTopics",
            views: {
                'home-content@home': {
                    templateUrl: 'area-topic/area-topic.html',
                    controller: 'AddEditAreasTopicsController'
                }
            }
        })
        .state('home.allScheduledAppointments', {
            url: "/allScheduledAppointments",
            views: {
                'home-content@home': {
                    templateUrl: 'appointment/view-all/view.html',
                    controller: 'ViewScheduledAppointmentsController'
                }
            }
        })
        .state('home.scheduledAppointments', {
            url: "/scheduledAppointments",
            views: {
                'home-content@home': {
                    templateUrl: 'appointment/schedules/schedules.html',
                    controller: 'AppointmentSchedulesController'
                }
            }
        })
        .state('home.changeAppointmentStatus', {
            url: "/changeAppointmentStatus",
            views: {
                'home-content@home': {
                    templateUrl: 'appointment/agent-live/change-status/change.html',
                    controller: 'ChangeAppointmentStatusController'
                }
            }
        })
        .state('home.employees', {
            url: "/employees",
            views: {
                'home-content@home': {
                    templateUrl: 'employees/add/add.html',
                    controller: 'AddEmployeesController'
                }
            },
            resolve: {
                AllLanguagesAndAreas: function(Restangular, $q) {
                    return Restangular.all('employee/getAllLanguagesAndAreas').customGET();
                }
            },
            params: {
                employeeDetails: 'staff'
            }
        })*/
})

.controller('QueryCtrl', function QueryCtrl($state, $scope, $rootScope, $modal, $stateParams, $window, Restangular, CommonService) {

})

.controller('NotificationController', function NotificationController($state, $scope, $rootScope, $modal, $stateParams, $window, Restangular, CommonService) {

})

.controller('HomeCtrl', function($state, $scope, $rootScope, $modal, $stateParams, $window, Restangular, CommonService) {
    Restangular.all('user/isAuthenticated').customGET().then(function(response) {
        if (response.data === null || response.message === null) {
            $state.go('main');
        } else {
            $rootScope.userSession = response.data;
            $rootScope.apiKey = response.apiKey;
            CommonService.setUserDetails(response.data);
        }
    });
    $scope.logout = function() {
        Restangular.all('user/logout').customGET().then(function(response) {
            $state.go('main');
        });
    };

})

.controller('DashBoardController', function($filter, $scope, $rootScope, $interval, lodash, AreasTopicsService, Restangular, ViewEditEmployeesService, ViewScheduledAppointmentsService, AppointmentSchedulesService, ngTableParams) {
    $scope.staffShow = false;
    $scope.adminShow = false;
    $rootScope.notificationCount = 0;
    $scope.customerShow = false;
    $scope.notification = $interval(function() {  
        Restangular.one('user', 'getNotification').one("1").customGET().then(function(response) {
            if (response) {
              $rootScope.notificationCount = response.length;
              console.log(response);
            } 
            else
            {
                console.log("no notification!!");
            }
        });

           
    }, 5000);
/*    if (lodash.isUndefined($rootScope.userSession)) {
        $scope.scheduledAppointmentLoader = true;
        Restangular.all('user/isAuthenticated').customGET().then(function(response) {
            $scope.scheduledAppointmentLoader = false;
            if (response.data === null || response.message === null) {
                $state.go('main');
            } else {
                $rootScope.userSession = response.data;
                loadDashBoard();
            }
        });
    } else {
        loadDashBoard();
    }*/

    function loadDashBoard() {

        if ($rootScope.userSession.userType === "staff") {
            $scope.staffShow = true;
        } else if ($rootScope.userSession.userType === "admin") {
            $scope.adminShow = true;
        } else {
            $scope.customerShow = true;
        }

        if ($rootScope.userSession.userType === "staff" || $rootScope.userSession.userType === "admin" ||$rootScope.userSession.userType === "user") {
            $scope.dashboardAreaTopicsLoader = true;
            AreasTopicsService.getAppointmentArea().then(function(appointmentAreas, status, headers, config) {
                $scope.dashboardAreaTopicsLoader = false;
                $scope.dashboardAppointmentAreaTable = new ngTableParams({
                    page: 1,
                    count: 5,
                    sorting: {
                        name: 'asc'
                    }
                }, {
                    counts: [],
                    total: 5,
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(appointmentAreas, params.orderBy()) :
                            appointmentAreas;
                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });
        }
        if ($rootScope.userSession.userType === "staff") {
            $scope.scheduledAppointmentLoader = true;
            AppointmentSchedulesService.getScheduledAppointments($rootScope.userSession.accountType).then(function(appointments, status, headers, config) {
                $scope.scheduledAppointmentLoader = false;
                $scope.scheduledAppointmentsTable = new ngTableParams({
                    page: 1,
                    count: 10,
                    sorting: {
                        appointmentDate: 'asc'
                    }
                }, {
                    counts: [],
                    total: appointments.length,
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(appointments, params.orderBy()) :
                            appointments;
                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });
        }

        if ($rootScope.userSession.userType === "admin") {
            $scope.dashboardEmployeesLoader = true;
            ViewEditEmployeesService.getAllEmployees().then(function(employee, status, headers, config) {
                $scope.dashboardEmployeesLoader = false;
                $scope.dashboardEmployeeTable = new ngTableParams({
                    page: 1,
                    count: 5,
                    sorting: {
                        firstname: 'asc'
                    }
                }, {
                    total: 5,
                    counts: [],
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(employee, params.orderBy()) :
                            employee;
                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });


            $scope.dashboardViewScheduledAppointmentLoader = true;
            ViewScheduledAppointmentsService.getAllScheduledAppointments().then(function(appointments, status, headers, config) {
                $scope.dashboardViewScheduledAppointmentLoader = false;
                $scope.dashboardScheduledAppointmentsTable = new ngTableParams({
                    page: 1,
                    count: 5,
                    sorting: {
                        appointmentDate: 'desc'
                    }
                }, {
                    counts: [],
                    total: 5,
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(appointments, params.orderBy()) :
                            appointments;
                        $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                    }
                });
            });
        }

    }

    $scope.getJsonObject = _.memoize(function(languagesList) {
        languagesList = JSON.parse(languagesList);
        return languagesList;
    });
})

.controller('MainCtrl', function($state, $rootScope, $scope, $modal, Restangular) {
    $scope.alerts = [];
    $scope.user = {};
    $scope.user.username = "";
    $scope.user.password = "";
    $scope.loginLoader = false;
    $scope.gotoAnchor = function(anchorId) {
        $scope.pageScroll = anchorId;
        $('html, body').stop().animate({
            scrollTop: $(anchorId).offset().top - 70
        }, 500);
    };

    $scope.login = function(user) {
        $scope.loginLoader = true;
        Restangular.one('user', 'loginSubscriber').one(user.username, user.password).post().then(function(response) {
            $scope.loginLoader = false;
            if (response.message === "failure") {
                $scope.alerts = [{
                    type: 'danger',
                    msg: response.message
                }];
            } else {
                //$rootScope.userSession = response.data;
                //$rootScope.apiKey = response.apiKey;
                $state.go('home.dashboard');
            }
        });
    };

    $scope.closeAlert = function() {
        $scope.alerts.splice(0, 1);
    };

    $scope.createNewAccount = function(data) {
        var data = {};
        data.verification = false;
        data.basicDetails = true;
        data.nextButton = true;
        data.message = '';
        data.userId = -1;
        openCreateNewAccountModal(data);
    };

    function openCreateNewAccountModal(data) {
        var modalInstance = $modal.open({
            templateUrl: 'account/createNewAccount.html',
            controller: 'CreateNewCtrl',
            windowClass: "animated animated fadeIn",
            backdrop: 'static',
            resolve: {
                AccountModalData: function() {
                    return data;
                }
            }
        });

        modalInstance.result.then(function(serverResponse) {
            if (serverResponse.data === false) {
                $scope.alerts = [{
                    type: 'danger',
                    msg: serverResponse.userAccount
                }];
            } else {
                $scope.alerts = [{
                    type: 'success',
                    msg: serverResponse.userAccount
                }];
            }
        });
    }

    $scope.forgotPassword = function() {
        var modalInstance = $modal.open({
            templateUrl: 'account/password/forgot-password.html',
            controller: 'ForgotPasswordCtrl',
            windowClass: "app-modal-window animated fadeIn",
            backdrop: 'static'
        });
    };

    $scope.gotoAnchor('#page-top');
})

.controller('GetUsersCtrl',
    function($log, $scope, userService) {
        $scope.getUserListing = function(movie) {
            var promise = userService.getUsers();
            promise.then(
                function(payload) {
                    $scope.listingData = payload.data;
                },
                function(errorPayload) {
                    $log.error('failure loading movie', errorPayload);
                });
        };
    })

.factory('userService', function($http) {
    return {
        getUsers: function(id) {
            return $http.get('http://localhost:8080/SmartBank-controllers/test/getUsers');
        }
    }
})

.factory('CommonService', function() {
    var userDetails = {};

    return {
        setUserDetails: function(loginUserDetails) {
            userDetails = loginUserDetails;
        },

        getUserDetails: function() {
            return userDetails;
        }
    };
})



.factory('AppointmentService', function(Restangular, CommonService) {

    var appointmentAreas = [];
    var appointmentTimes = [];
    //var allocatedAppointments = [];

    var allocatedAppointments = [{
        date: '26-06-2015',
        time: [{
            timeValue: '9:30 AM',
            empId: 135,
            timeId: 2
        }]
    }];

    /*var appointments = [
      { 'areaId': 2, 'name':'Banking', 'description': 'If you are a homeowner struggling with your loan payments, you may want to learn about our home loan assistance programs. Bank of America is committed to helping homeowners and is a participant.', 'image': 'https://secure.bankofamerica.com/content/images/ContextualSiteGraphics/Marketing/Banners/en_US/Appointments/icon_banking.png', 'details': [{'id': 12, 'name': 'Checking and Savings'},{'id': 13, 'name': 'General Banking'}, {'id': 14, 'name': 'Preferred Rewards'},{'id': 15, 'name': 'Others'}]},
      { 'areaId': 3, 'name':'Loan', 'description': 'If you are a homeowner struggling with your loan payments, you may want to learn about our home loan assistance programs. Bank of America is committed to helping homeowners and is a participant.', 'image': 'https://secure.bankofamerica.com/content/images/ContextualSiteGraphics/Marketing/Banners/en_US/Appointments/icon_hl.png', 'details': [{'id': 22, 'name': 'Mortage'},{'id': 23, 'name': 'Refinance'}, {'id': 24, 'name': 'Home Equity'},{'id': 25, 'name': 'Others'}]},
      { 'areaId': 4, 'name':'Business', 'description': 'If you are a homeowner struggling with your loan payments, you may want to learn about our home loan assistance programs. Bank of America is committed to helping homeowners and is a participant.', 'image': 'https://secure.bankofamerica.com/content/images/ContextualSiteGraphics/Marketing/Banners/en_US/Appointments/icon_smallbizbanking.png', 'details': [{'id': 32, 'name': 'Business Checkings'},{'id': 33, 'name': 'Busniess Credit Cards'}, {'id': 34, 'name': 'Cash Management'},{'id': 35, 'name': 'Others'}]},
      { 'areaId': 5, 'name':'Investment', 'description': 'If you are a homeowner struggling with your loan payments, you may want to learn about our home loan assistance programs. Bank of America is committed to helping homeowners and is a participant.', 'image': 'https://secure.bankofamerica.com/content/images/ContextualSiteGraphics/Marketing/Banners/en_US/Appointments/icon_investment.png', 'details': [{'id': 42, 'name': 'Retirement Planning'},{'id': 43, 'name': 'IRA'}, {'id': 44, 'name': 'Investings'},{'id': 45, 'name': 'Others'}]}
    ];*/

    var languages = [];

    var scheduledAppointmentDetails = [];

    return {
        setAllAppointmentAreaDetails: function(newAppointmentAreas) { //DONE
            appointmentAreas = newAppointmentAreas;
        },

        getAllAppointmentAreaDetails: function() { //DONE
            return appointmentAreas;
        },

        getAllLanguages: function() { //DONE
            return languages;
        },

        setAllLanguages: function(allLanguages) { //DONE
            languages = allLanguages;
        },

        getAllTimes: function() { //DONE 
            return Restangular.all('appointment/getTimes').customGET();
        },

        // setAllAppointmentTimes: function(newAppointmentTimes) { //DONE
        //     appointmentTimes = newAppointmentTimes;
        // },

        getAllAppointmentTimes: function(selectedDate) { //DONE
            return Restangular.all('appointment/getAllocatedTimes').post(selectedDate);
        },

        getAllocatedAppointments: function() { //DONE
            return Restangular.all('appointment/getAllocatedAppointments').customGET();
        },

        // setAllocatedAppointments: function(newAllocatedAppointments) { //DONE
        //     allocatedAppointments = newAllocatedAppointments;
        // },

        submitNewAppointment: function(newAppointmentRequest) {
            return Restangular.all('appointment/addNewAppointment').post(newAppointmentRequest);
        },

        getScheduledAppointmentDetails: function() {
            return Restangular.all('appointment/getScheduledAppointmentDetails/' + CommonService.getUserDetails().id).customGET();
        }

    };
});
