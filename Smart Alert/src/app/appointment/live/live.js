 angular.module('SmartBankApp.live', ['SmartBankApp.live.services'])

 .controller('DiscussionRoomController', function DiscussionRoomController($scope, $stateParams, $interval, $rootScope, $state, lodash, DiscussionRoomService, Restangular, CommonService) {
     $scope.liveRoomLoader = true;
     $scope.appointmentDetails = {};
     $scope.text = {};
     $scope.text.messageText = "";
     $scope.callId;
     $scope.callButtonShow = true;
     $scope.endButtonShow = false;
     $scope.incomingButtonShow = false;
     $scope.rejectButtonShow = false;
     $scope.holdButtonShow = false;
     $scope.resumeButtonShow = false;
     $scope.textMessageCount = 0;
     $scope.fileSharingMessageCount = 0;
     $scope.sendingTextMessage = false;
     $scope.sendingFileMessage = false;
     $scope.allMessages = {};
     $scope.allMessages.chatMessages = [];
     $scope.allMessages.fileSharingMessages = [];

     Restangular.all('appointment/getAppoinmentDetails/' + $stateParams.roomId).customGET().then(function(appointmentDetails) {
         $scope.appointmentDetails = appointmentDetails[0];
        // $rootScope.invalidate();
         if ($rootScope.userSession === undefined) {

             Restangular.all('user/isAuthenticated').customGET().then(function(response) {
                 if (response.data === null || response.message === null) {
                     $state.go('main');
                 } else {
                     $rootScope.userSession = response.data;
                     $rootScope.apiKey = response.apiKey;
                     CommonService.setUserDetails(response.data);
                 }
                 console.log("after get appointment:::", $rootScope.userSession);
                 authenticate();
             });
         } else {
             authenticate();
         }
     });



     function authenticate() {
         KandyAPI.Phone.login($rootScope.apiKey, $rootScope.userSession.username + "@smartbank.techmahindra.com", $rootScope.userSession.password);
         KandyAPI.Phone.setup({
             remoteVideoContainer: $('#incoming-video')[0],
             localVideoContainer: $('#outgoing-video')[0],
             listeners: {
                 loginsuccess: onLoginSuccess,
                 loginfailed: onLoginFailed,
                 callinitiated: onCallInitiate,
                 callincoming: onCallIncoming,
                 callinitiatefailed: onCallInitiateFail,
                 oncall: onCall,
                 callanswered: onCallAnswer,
                 callended: onCallTerminate,
                 callendedfailed: onCallEndedFailed,
                 callrejected: onCallRejected
             }
         });
     }
     $scope.messages = [];
     var instance = null;

     $rootScope.customerInvalidate = function() {
         if (instance !== null) {
             KandyAPI.Phone.logout(function() {
                 clearInterval(instance);
             });
             instance = null;
         }
     };

     $scope.removeDuplicates = function(array) {
         var uniqueList = lodash.uniq(array, function(item, key, a) {
             return item.timestamp;
         });
         array = uniqueList;
     };

     $scope.startCall = function() {
         KandyAPI.Phone.makeCall($scope.appointmentDetails.agentEmailId, true);
     };

     $scope.endCall = function() {
         KandyAPI.Phone.endCall($scope.callId);
     };

     $scope.holdCall = function() {
         KandyAPI.Phone.holdCall($scope.callId);
         $scope.resumeButtonShow = true;
         $scope.holdButtonShow = false;
     };
     $scope.answerCall = function() {
         KandyAPI.Phone.answerCall($scope.callId, true);
     };
     $scope.rejectCall = function() {
         KandyAPI.Phone.rejectCall($scope.callId);
     };

     $scope.resumeCall = function() {
         KandyAPI.Phone.unHoldCall($scope.callId);
         $scope.resumeButtonShow = false;
         $scope.holdButtonShow = true;
     };

     function onCallInitiate(call) {
         $scope.callId = call.getId();
         $scope.callButtonShow = false;
         $scope.endButtonShow = true;
         $scope.rejectButtonShow = false;
         $scope.holdButtonShow = false;
         $scope.resumeButtonShow = false;
         //$audioRingIn[0].pause();
         $scope.outgoing.play();
         $scope.incomming.pause();
         $scope.incomming.setVolume(1.0);
         $scope.outgoing.setVolume(1.0);
         //$('#username-calling').text('Calling ' + $('#user_to_call').val());
         //UIState.callinitialized();
     }

     function onCall(call) {
         console.debug('oncall');
         $scope.outgoing.pause();
         //$audioRingOut[0].pause();
     }

     function onCallTerminate(call) {
         $scope.callId = null;
         $scope.callButtonShow = true;
         $scope.endButtonShow = false;
         $scope.incomingButtonShow = false;
         $scope.rejectButtonShow = false;
         $scope.holdButtonShow = false;
         $scope.resumeButtonShow = false;
         $scope.outgoing.pause();
         $scope.incomming.pause();
     }

     function onCallInitiateFail() {
         $scope.outgoing.pause();
         $scope.callButtonShow = true;
         $scope.endButtonShow = false;
         $scope.incomingButtonShow = false;
         $scope.rejectButtonShow = false;
         $scope.holdButtonShow = false;
     }

     function onCallEndedFailed() {
         $scope.callId = null;
     }

     function onCallAnswer(call) { // okay
         $scope.callId = call.getId();
         $scope.outgoing.pause();
         $scope.incomming.pause();

         $scope.callButtonShow = false;
         $scope.endButtonShow = true;
         $scope.incomingButtonShow = false;
         $scope.rejectButtonShow = false;
         $scope.holdButtonShow = true;
     }

     function onCallIncoming(call, isAnonymous) {
         $scope.incomming.play();
         $scope.outgoing.pause();
         $scope.callId = call.getId();
         $scope.callButtonShow = false;
         $scope.endButtonShow = false;
         $scope.incomingButtonShow = true;
         $scope.rejectButtonShow = true;
     }

     function onCallRejected() { //okay
         $scope.callId = null;
         $scope.incomming.pause();
         $scope.callButtonShow = true;
         $scope.endButtonShow = false;
         $scope.rejectButtonShow = false;
         $scope.incomingButtonShow = false;
     }

     function receiveMessages() {
         KandyAPI.Phone.getIm(function(data) {
                 data.messages.forEach(function(msg) {
                     if (msg.messageType == 'chat' && msg.contentType === 'text' && msg.message.mimeType == 'text/plain') {
                         $scope.textMessageCount++;
                         $scope.allMessages.chatMessages.push({
                             incoming: true,
                             message: msg.message.text,
                             name: "Bank Agent",
                             timestamp: msg.timestamp
                         });
                         $scope.removeDuplicates($scope.allMessages.chatMessages);
                     } else if (msg.messageType == 'chat' && msg.contentType === 'file') {
                         $scope.fileSharingMessageCount++;
                         $scope.allMessages.fileSharingMessages.push({
                             incoming: true,
                             fileName: msg.message.content_name,
                             path: KandyAPI.Phone.buildFileUrl(msg.message.content_uuid),
                             name: "Bank Agent",
                             mimeType: msg.message.mimeType,
                             timestamp: msg.timestamp
                         });
                         $scope.removeDuplicates($scope.allMessages.fileSharingMessages);
                     } else {
                         console.log('received ' + msg.messageType + ': ');
                     }
                 });
             },

             function() {
                 console.log('error recieving IMs');
             });
     };

     $scope.downloadFile = function(fileName, filePath, fileMimeType) {
         getAndSaveUrl(filePath, fileName, fileMimeType);
     };

     $scope.fileSharingTabWhenSelect = function() {
         $scope.fileSharingMessageCount = 0;
     };

     $scope.textMessageTabWhenSelect = function() {
         $scope.textMessageCount = 0;
     };

     function onLoginSuccess() {

         KandyAPI.Phone.updatePresence(0);
         $scope.$apply(function() {
             $scope.liveRoomLoader = false;
         });
         instance = setInterval(function() {
             $scope.$apply(receiveMessages);
         }, 1000);
     }

     function onLoginFailed() {

     }

     function getAndSaveUrl(url, name, mime) {
         var xhr = new XMLHttpRequest();
         xhr.open('GET', url, true);
         xhr.responseType = 'blob';
         xhr.onload = function(e) {
             if (this.status == 200) {
                 var blob = new Blob([this.response], {
                     type: mime
                 });
                 saveAs(blob, name);
             }
         };
         xhr.send();
     }

     $scope.sendMessage = function(text) {
         $scope.sendingTextMessage = true;
         KandyAPI.Phone.sendIm($scope.appointmentDetails.agentEmailId, text.messageText, function() {
                 $scope.$apply(function() {
                     $scope.sendingTextMessage = false;
                     $scope.allMessages.chatMessages.push({
                         incoming: false,
                         message: text.messageText,
                         name: $rootScope.userSession.name,
                         timestamp: new Date().getTime()
                     });
                     $scope.removeDuplicates($scope.allMessages.chatMessages);
                     $scope.text = {};
                     $scope.text.messageText = "";
                 });
             },
             function() {
                 console.log('IM send failed');
             });
     };

     $scope.sendFileAttachment = function() {
         $scope.sendingFileMessage = true;
         var file = $('#userFileUploader')[0].files[0];
         var filename = $('#userFileUploader').val().split('\\').pop();
         KandyAPI.Phone.sendImWithFile($scope.appointmentDetails.agentEmailId, file, function(response) {
                 $scope.$apply(function() {
                     $scope.sendingFileMessage = false;
                     $scope.allMessages.fileSharingMessages.push({
                         incoming: false,
                         fileName: response.message.content_name,
                         path: KandyAPI.Phone.buildFileUrl(response.message.content_uuid),
                         name: $rootScope.userSession.name,
                         mimeType: response.message.mimeType,
                         timestamp: new Date().getTime()
                     });
                     $scope.removeDuplicates($scope.allMessages.fileSharingMessages);
                     $('#userFileUploader').val('');
                     $('#userFileSubmit').attr('disabled', true);
                 });
             },
             function() {
                 console.log('IM send failed');
             });
     };
 });
