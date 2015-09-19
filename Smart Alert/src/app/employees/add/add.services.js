angular.module('SmartBankApp.employees.services', [])

.factory("AddEmployeesService", function($http, Restangular) {
    return {
        getGeoLocation: function(pin) {
            return $http.get('http://maps.googleapis.com/maps/api/geocode/json?address=' + pin + '&sensor=true');
        },

        addNewEmployees: function(employee) {
            return Restangular.all('employee/addNewEmployees').post(employee);
        },

        saveEditedEmployees: function(employee) {
            return Restangular.all('employee/saveEditedEmployees').post(employee);
        },

        getAllLanguagesAndAreas: function() {
            return Restangular.all('employee/getAllLanguagesAndAreas').customGET();
        },
        
        getEmployeeDetails: function(userId) {
            return Restangular.all('user/getEmployeeDetails/'+userId).customGET();
        }

    };
})

.value("AddEmployeesValue", {

    allLangs: [
        'Assamese',
        'Bengali',
        'English',
        'Bhojpuri',
        'Gujarati',
        'Hindi',
        'Kannada',
        'Kashmiri',
        'Konkani',
        'Malayalam',
        'Marathi',
        'Nepali',
        'Odia',
        'Punjabi',
        'Rajasthani',
        'Sanskrit',
        'Santali',
        'Sindhi',
        'Tamil',
        'Telugu',
        'Urdu'
    ],

    contactsModels: {
        "MOB": "",
        "FAX": "",
        "PHO": "",
        "EMA": "",
        "SKY": "",
        "GOP": "",
        "FCB": "",
        "LKN": "",
        "TWT": ""
    },

    allBusinessTypes: ["STRAD", "PRTNR", "CPTY", "CPUB"],

    allContacts: [{
        name: "Email Address",
        show: true,
        visibleAsOption: false,
        placeholder: "Email Address",
        image: "assets/img/email_24.png",
        label: "",
        model: "EMA"
    }, {
        name: "Phone Number",
        show: false,
        visibleAsOption: true,
        placeholder: "Phone Number",
        image: "assets/img/phone_24.png",
        label: "",
        model: "PHO"
    }, {
        name: "Mobile Number",
        show: false,
        visibleAsOption: true,
        placeholder: "Mobile Number",
        image: "assets/img/phone_24.png",
        label: "",
        model: "MOB"
    }, {
        name: "Fax Number",
        show: false,
        visibleAsOption: true,
        placeholder: "Fax Number",
        image: "assets/img/fax_24.png",
        label: "",
        model: "FAX"
    }, {
        name: "Skype",
        show: false,
        visibleAsOption: true,
        placeholder: "Skype Id",
        image: "assets/img/skype_24.gif",
        label: "",
        model: "SKY"
    }, {
        name: "Twitter",
        show: false,
        visibleAsOption: true,
        placeholder: "Username",
        image: "assets/img/twitter_24.png",
        label: "http://twitter.com/",
        model: "TWT"
    }, {
        name: "LinkedIn",
        show: false,
        visibleAsOption: true,
        placeholder: "Username",
        image: "assets/img/linkedin_24.png",
        label: "http://linkedin.com/",
        model: "LKN"
    }, {
        name: "Facebook",
        show: false,
        visibleAsOption: true,
        placeholder: "Username",
        image: "assets/img/facebook_24.png",
        label: "http://facebook.com/",
        model: "FCB"
    }, {
        name: "Google+",
        show: false,
        visibleAsOption: true,
        placeholder: "Username",
        image: "assets/img/googleplus_24.png",
        label: "http://plus.google.com/",
        model: "GOP"
    }],

    selectedContacts: [{
        name: "Email Address",
        visible: true,
        placeholder: "Email Address",
        image: "assets/img/email_24.png",
        label: "",
        model: "EMA"
    }],

    availableContacts: [{
        name: "Phone Number",
        placeholder: "Phone Number",
        image: "assets/img/phone_24.png",
        label: "",
        model: "PHO"
    }, {
        name: "Mobile Number",
        placeholder: "Mobile Number",
        image: "assets/img/phone_24.png",
        label: "",
        model: "MOB"
    }, {
        name: "Fax Number",
        placeholder: "Fax Number",
        image: "assets/img/fax_24.png",
        label: "",
        model: "FAX"
    }, {
        name: "Skype",
        placeholder: "Skype Id",
        image: "assets/img/skype_24.gif",
        label: "",
        model: "SKY"
    }, {
        name: "Twitter",
        placeholder: "Username",
        image: "assets/img/twitter_24.png",
        label: "http://twitter.com/",
        model: "TWT"
    }, {
        name: "LinkedIn",
        placeholder: "Username",
        image: "assets/img/linkedin_24.png",
        label: "http://linkedin.com/",
        model: "LKN"
    }, {
        name: "Facebook",
        placeholder: "Username",
        image: "assets/img/facebook_24.png",
        label: "http://facebook.com/",
        model: "FCB"
    }, {
        name: "Google+",
        placeholder: "Username",
        image: "assets/img/googleplus_24.png",
        label: "http://plus.google.com/",
        model: "GOP"
    }]
});
