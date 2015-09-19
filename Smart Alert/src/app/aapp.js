angular.module('SmartBankApp', [
    'templates-app',
    'templates-common',
    'SmartBankApp.query',
    'SmartBankApp.notification',
    'SmartBank.form.controls',
    'SmartBankApp.appointment',
    'SmartBankApp.account',
    'SmartBankApp.employees',
    'SmartBankApp.language',
    'SmartBankApp.areasTopics',
    'SmartBankApp.viewSchedules',
    'SmartBankApp.live',
    'SmartBankApp.agent',
    'SmartBankApp.changeStatus',
    'SmartBankApp.employees.schedules',
    'ui.router',
    'jcs-autoValidate',
    'ui.bootstrap',
    'ngLodash',
    'ui.bootstrap.datetimepicker',
    'restangular',
    'cgNotify',
    'ui.date',
    'ngTable',
    'ngListSelect',
    'mediaPlayer'
   // 'lk-google-picker'
])

.config(function($stateProvider, $httpProvider, $urlRouterProvider, RestangularProvider) {
        $httpProvider.defaults.useXDomain = true;
        $httpProvider.defaults.withCredentials = true;
        delete $httpProvider.defaults.headers.common["X-Requested-With"];
        $httpProvider.defaults.headers.common["Accept"] = "application/json";
        $httpProvider.defaults.headers.common["Content-Type"] = "application/json";

        RestangularProvider.setBaseUrl('/SmartAlert/');
        $urlRouterProvider.otherwise("/main");
        $stateProvider.state('site', {
            abstract: true
        })
    })
    /* .run(function($rootScope, $state, $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
        $rootScope.$on("$stateChangeError", console.log.bind(console));
        $rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;
        });
    })*/

.filter('limitInBetween', function() {
    return function(items, begin, end) {
        if (items !== undefined) {
            return items.slice(begin, end);
        }
    }
})

.filter('capitalizeword', function() {
    return function(input, scope) {
        if (input !== null || input !== undefined)
            input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
});
