angular.module('SmartBankApp.appointment', [])

.controller('AppointmentController', function AppointmentController($scope, $rootScope, $http, $state, Restangular, AppointmentService) {
    $scope.appointmentMainLoader = true;
    $rootScope.wizardLoader = false;
    Restangular.all('appointment/getAppointmentDetails').customGET().then(function(response) {
        if (!_.isEmpty(response)) {
            $scope.appointmentMainLoader = false;
            $rootScope.appointmentWizard = 'area';
            AppointmentService.setAllAppointmentAreaDetails(response.appointmentArea);
            AppointmentService.setAllLanguages(response.languages);
            //            AppointmentService.setAllocatedAppointments(response.appointments);
            $scope.appointmentAreas = response.appointmentArea;
            $state.go('area');
        }
    });
    $scope.selectAppointmentArea = function(appointmentAreaId) {
        $rootScope.appointmentWizard = 'topics';
        $state.go('topics', {
            topicDetails: {
                areaId: appointmentAreaId,
                topics: ''
            }
        });
    };
})

.controller('ViewAppointmentController', function ViewAppointmentController($scope, $rootScope, $state, AppointmentService) {
    $scope.appointmentViewLoader = true;
    $scope.scheduledAppointments = [];
    AppointmentService.getScheduledAppointmentDetails().then(function(response) {
        $scope.appointmentViewLoader = false;
        if (!_.isEmpty(response.data)) {
            $scope.scheduledAppointments = response.data;
        }
    });

    $scope.enterRoom = function(appointment) {
        $rootScope.discussionRoomAppointmentDetails = appointment;
        $state.go('home.enterRoom', {
            roomId: appointment.id
        });
    };
})

.controller('AppointmentAreaController', function AppointmentAreaController($scope, $rootScope, $state, AppointmentService) {
    $scope.selectAppointmentTopics = function(appointmentAreaId) {
        $rootScope.appointmentWizard = 'topics';
        $state.go('topics', {
            topicDetails: {
                areaId: appointmentAreaId,
                topics: ''
            }
        });
    };
})

.controller('AppointmentTopicsController', function AppointmentTopicsController($scope, $rootScope, $state, $stateParams, lodash, AppointmentService) {
    $rootScope.wizardLoader = false;
    $scope.data = {};
    $scope.data.appointmentAreaDetails = lodash.filter(AppointmentService.getAllAppointmentAreaDetails(), {
        areaId: $stateParams.topicDetails.areaId
    })[0];

    $scope.data.languages = AppointmentService.getAllLanguages();

    if ($stateParams.topicDetails.topicsSelected !== undefined) {
        $scope.selectedTopics = $stateParams.topicDetails.topicsSelected;
    } else {
        $scope.selectedTopics = {};
    }

    if ($stateParams.topicDetails.language !== undefined) {
        $scope.language = $stateParams.topicDetails.language.id;
    } else {
        var languageSelected = lodash.filter($scope.data.languages, {
            languageName: "English"
        })[0];
        $scope.language = languageSelected.id;
    }

    $scope.isAnyTopicsSelected = !_.contains($scope.selectedTopics, true);
    $scope.selectAppointmentTime = function() {
        $rootScope.appointmentWizard = 'time';
        var languageSelected = lodash.filter($scope.data.languages, {
            id: $scope.language
        })[0];
        $state.go('time', {
            topicDetails: {
                areaId: $stateParams.topicDetails.areaId,
                topics: $scope.selectedTopics,
                language: languageSelected
            }
        });
    };

    $scope.isTopicsSelected = function() {
        $scope.isAnyTopicsSelected = !_.contains($scope.selectedTopics, true);
    };

    $scope.selectAppointmentArea = function(appointmentAreaId) {
        $rootScope.appointmentWizard = 'area';
        $state.go('area');
    };
})

.controller('AppointmentTimeController', function AppointmentTimeController($scope, $rootScope, $state, $stateParams, notify, lodash, Restangular, AppointmentService, CommonService) {
    $rootScope.wizardLoader = false;
    $scope.appointmentMainLoader = true;
    $scope.appointmentTime = {};
    $scope.appointmentTime.date = '';
    $scope.appointmentTime.time = '';
    $scope.finish = false;
    $scope.timeLoader = true;
    $scope.appointmentTime.date = new Date();

    AppointmentService.getAllTimes().then(function(appointmentTimes) {
        if (!_.isEmpty(appointmentTimes)) {
            $scope.appointmentMainLoader = false;
            $scope.appointmentTimes = appointmentTimes;
            $scope.timeLoader = false;
        }
    });

    $scope.onDateSelect = function(selectedDate, oldDate) {
        $scope.appointmentTime.time = '';
        $scope.appointmentTime.date = selectedDate;
        $scope.finish = false;
        $scope.timeLoader = true;
        AppointmentService.getAllAppointmentTimes(selectedDate).then(function(appointmentTimes) {
            angular.forEach($scope.appointmentTimes, function(appointmentTime, key) {
                if (appointmentTime !== undefined) {
                    if (lodash.indexOf(appointmentTimes, appointmentTime.id) !== -1) {
                        appointmentTime.selected = 1;
                    } else {
                        appointmentTime.selected = 0;
                    }
                    var selectedDateMoment = moment(selectedDate).format('YYYY-MM-DD');
                    var datTime = selectedDateMoment + ' ' + appointmentTime.timeValue;
                    var dateTimeObject = moment(datTime, "YYYY-MM-DD h:mm A").toDate();
                    var currentTimeObject = moment().toDate();
                    if (dateTimeObject < currentTimeObject) {
                        appointmentTime.selected = 1;
                    }
                }
            });
            $scope.timeLoader = false;
        });
    };

    $scope.onDateSelect($scope.appointmentTime.date);

    $scope.beforeRender = function($view, $dates, $leftDate, $upDate, $rightDate) {
        var threeMonthsLater = moment().add(3, 'months');
        for (var index = 0; index < $dates.length; index++) {
            $dates[index].selectable = moment($dates[index].utcDateValue).isBetween(moment().subtract(1, 'days'), threeMonthsLater);
        }
    };


    $scope.selectedDate = '';
    $scope.areaId = $stateParams.topicDetails.areaId;
    $scope.setIndex = function(time) {
        if ($scope.appointmentTimes[time].selected !== 1) {
            $scope.selectedDate = time;
        }
    };

    $scope.selectAppointmentTopics = function() {
        $rootScope.appointmentWizard = 'topics';
        $state.go('topics', {
            topicDetails: {
                areaId: $stateParams.topicDetails.areaId,
                topicsSelected: $stateParams.topicDetails.topics,
                language: $stateParams.topicDetails.language
            }
        });
    };

    $scope.addAppointment = function() {
        $rootScope.wizardLoader = true;
        AppointmentService.submitNewAppointment(getNewAppointmentRequest()).then(function(addNewAppointmentResponse) {
            if (addNewAppointmentResponse.data) {
                notify({
                    messageTemplate: '<span><b style="font-size: 14px;">Success :</b> ' + addNewAppointmentResponse.message + '</span>',
                    duration: 5000,
                    classes: 'alert-success',
                    scope: $scope,
                    templateUrl: 'common/notify.html'
                });
                $state.go('home.viewAppointment', {});
            } else {
                notify({
                    messageTemplate: '<span><b style="font-size: 14px;">Failure :</b> ' + addNewAppointmentResponse.message + '</span>',
                    duration: 5000,
                    classes: 'alert-danger',
                    scope: $scope,
                    templateUrl: 'common/notify.html'
                });
            }
            $rootScope.wizardLoader = false;
        });
    };

    $scope.selectedTime = function(selectedTime) {
        var index = lodash.findIndex($scope.appointmentTimes, selectedTime);
        if ($scope.appointmentTimes[index].selected !== 1) {
            $scope.appointmentTime.time = $scope.appointmentTimes[index];
            if (isAppointmentDateAndTimeSelected()) {
                $scope.finish = true;
            }
        }
    };

    function isAppointmentDateAndTimeSelected() {
        return ($scope.appointmentTime.date !== '' && $scope.appointmentTime.time !== '');
    }

    function getNewAppointmentRequest() {
        var newAppointmentRequest = {};
        newAppointmentRequest.areaId = $stateParams.topicDetails.areaId;
        newAppointmentRequest.userId = CommonService.getUserDetails().id;
        newAppointmentRequest.date = $scope.appointmentTime.date.getTime();
        newAppointmentRequest.language = $stateParams.topicDetails.language.id;
        newAppointmentRequest.time = $scope.appointmentTime.time.id;
        newAppointmentRequest.topics = getSelectedTopics();
        return newAppointmentRequest;
    }

    function getSelectedTopics() {
        var topicsSelected = [];
        lodash.each($stateParams.topicDetails.topics, function(isTopicSelected, selectedTopicKey) {
            if (isTopicSelected) {
                topicsSelected.push(selectedTopicKey);
            }
        });
        return topicsSelected;
    }
});
