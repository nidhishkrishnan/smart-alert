 angular.module('SmartBankApp.agent', ['SmartBankApp.agent.services'])

 .controller('AgentLiveRoomController', function AgentLiveRoomController($scope, $stateParams, $interval, $rootScope, $state, lodash, AgentDiscussionRoomService, Restangular, CommonService) {

     $scope.liveRoomLoader = true;
     $scope.appointmentDetails = {};
     $scope.text = {};
     $scope.allMessages = {};
     $scope.allMessages.chatMessages = [];
     $scope.allMessages.fileSharingMessages = [];
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

     // if (_video) {
     //            var patCanvas = document.querySelector('#snapshot');
     //            if (!patCanvas) return;

     //            patCanvas.width = _video.width;
     //            patCanvas.height = _video.height;
     //            var ctxPat = patCanvas.getContext('2d');

     //            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
     //            ctxPat.putImageData(idata, 0, 0);

     //            sendSnapshotToServer(patCanvas.toDataURL());

     //            patData = idata;
     //        }




     $scope.snap = function() {
         var video = $("#incoming-video video").get(0);
         var scale = 1.00;
         var $output = $("#output");
         var canvas = document.createElement("canvas");
         canvas.width = video.videoWidth * scale;
         canvas.height = video.videoHeight * scale;
         canvas.getContext('2d').drawImage(video, 0, 0, canvas.width, canvas.height);
         var img = document.createElement("img");
         console.log(canvas.width, canvas.height);
         img.src = canvas.toDataURL();
         img.style.display = 'block';
         img.style.width = 484 + "px";
         img.style.height = 320 + "px";
         img.width = 484;
         img.height = 320;
         //$output.prepend(img);



         var myWindow = window.open('', '', 'width=480,height=310');
         var doc = myWindow.document;
         doc.open();
         doc.write('<img src="' + img.src + '" width="468" height="294"/>');
         doc.close();

         // window.open($("#output"), "image", "width=" + img.style.width + ",height=" + img.style.height + ",resizable=1'");

     };

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
         AgentDiscussionRoomService.getChatHistory($stateParams.roomId).then(function(response) {
             angular.forEach(response, function(value, key) {
                 if (value.messageType === 0) {
                     $scope.allMessages.chatMessages.push(value);
                 } else {
                     $scope.allMessages.fileSharingMessages.push(value);
                 }
             });
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
         });
     }
     $scope.messages = [];
     var instance = null;

     $rootScope.agentInvalidate = function() {
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
         KandyAPI.Phone.makeCall($scope.appointmentDetails.userEmailId, true);
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

     $scope.startCapture = function()
     {
        $("#incoming-video video").attr("id","incomingVideo");
         $scope.virec = new VIRecorder.initVIRecorder({
                 recorvideodsize: 0.4, // recorded video dimentions are 0.4 times smaller than the original
                 webpquality: 0.7, // chrome and opera support webp imags, this is about the aulity of a frame
                 framerate: 15, // recording frame rate 
                 videotagid: "incomingVideo",
                 videoWidth: "640",
                 videoHeight: "480",
             },
             function() {
                 //success callback. this will fire if browsers supports 
             },
             function(err) {
                 //onerror callback, this will fire if browser does not support
                 console.log(err.code + " , " + err.name);
             }
         );
         $scope.virec.startCapture(); // this will start recording video and the audio 
                //startCountDown(null);
     }

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
                         var chat = {
                             incoming: 1,
                             agentId: $rootScope.userSession.id,
                             message: msg.message.text,
                             name: 'Customer',
                             fileName: '',
                             mimeType: '',
                             timestamp: msg.timestamp,
                             userId: msg.sender.user_id,
                             appointmentId: $stateParams.roomId,
                             messageType: 0
                         }
                         AgentDiscussionRoomService.saveChatHistory(chat).then(function(response) {
                             $scope.textMessageCount++;
                             $scope.allMessages.chatMessages.push(chat);
                             $scope.removeDuplicates($scope.allMessages.chatMessages);
                         });
                     } else if (msg.messageType == 'chat' && msg.contentType === 'file') {
                         console.log("file message object", msg);
                         var chat = {
                             incoming: 1,
                             message: KandyAPI.Phone.buildFileUrl(msg.message.content_uuid),
                             name: 'Customer',
                             fileName: msg.message.content_name,
                             mimeType: msg.message.mimeType,
                             timestamp: msg.timestamp,
                             agentId: $rootScope.userSession.id,
                             userId: msg.sender.user_id,
                             appointmentId: $stateParams.roomId,
                             messageType: 1
                         }
                         AgentDiscussionRoomService.saveChatHistory(chat).then(function(response) {
                             $scope.fileSharingMessageCount++;
                             $scope.allMessages.fileSharingMessages.push(chat);
                             $scope.removeDuplicates($scope.allMessages.fileSharingMessages);
                         });

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
         KandyAPI.Phone.sendIm($scope.appointmentDetails.userEmailId, text.messageText, function() {
                 $scope.$apply(function() {
                     var dateTime = new Date().getTime();
                     var chat = {
                         incoming: 0,
                         message: text.messageText,
                         name: $rootScope.userSession.name,
                         fileName: '',
                         mimeType: '',
                         timestamp: dateTime,
                         agentId: $rootScope.userSession.id,
                         userId: $scope.appointmentDetails.userEmailId.substring(0, $scope.appointmentDetails.userEmailId.indexOf("@")),
                         appointmentId: $stateParams.roomId,
                         messageType: 0
                     }
                     AgentDiscussionRoomService.saveChatHistory(chat).then(function(response) {
                         $scope.sendingTextMessage = false;
                         $scope.allMessages.chatMessages.push(chat);
                         $scope.removeDuplicates($scope.allMessages.chatMessages);
                         $scope.text = {};
                         $scope.text.messageText = "";
                     });
                 });
             },

             function() {
                 console.log('IM send failed');
             });
     };

     $scope.sendFileAttachment = function() {
         var file = $('#userFileUploader')[0].files[0];
         var filename = $('#userFileUploader').val().split('\\').pop();
         $scope.sendingFileMessage = true;
         KandyAPI.Phone.sendImWithFile($scope.appointmentDetails.userEmailId, file, function(response) {
                 $scope.sendingFileMessage = false;
                 $scope.$apply(function() {
                     var dateTime = new Date().getTime();
                     var chat = {
                         incoming: 0,
                         message: KandyAPI.Phone.buildFileUrl(response.message.content_uuid),
                         name: $rootScope.userSession.name,
                         fileName: response.message.content_name,
                         mimeType: response.message.mimeType,
                         timestamp: dateTime,
                         agentId: $rootScope.userSession.id,
                         userId: $scope.appointmentDetails.userEmailId.substring(0, $scope.appointmentDetails.userEmailId.indexOf("@")),
                         appointmentId: $stateParams.roomId,
                         messageType: 1
                     }
                     AgentDiscussionRoomService.saveChatHistory(chat).then(function(response) {
                         $scope.allMessages.fileSharingMessages.push(chat);
                         $scope.removeDuplicates($scope.allMessages.fileSharingMessages);
                         $('#userFileUploader').val('');
                         $('#userFileSubmit').attr('disabled', true);
                     });
                 });
             },
             function() {
                 alert('IM send failed');
             });
     };
 });
