var myapp = angular.module('event', []);

myapp.controller('createEventController', ['$scope', '$log', 'eventService', function ($scope, $log, eventService) {
    $scope.event = {};
    $scope.name='';
    $scope.description = '';
    $scope.date = '';
    $scope.location = '';
    $scope.formSubmit = function () {
        if(! $scope.createEvent.$valid) {
            $scope.createEvent.name.$setDirty();
            $scope.createEvent.eventDescription.$setDirty();
            $scope.createEvent.eventDate.$setDirty();
            $scope.createEvent.eventTime.$setDirty();
            $scope.createEvent.eventPlace.$setDirty();
        } else {
            $scope.date = $scope.event.date;
        }
    };

    $scope.clear = function () {
        $scope.event.name = '';
        $scope.event.description = '';
        $scope.event.date = '';
        $scope.event.time = '';
        $scope.event.place = '';
        $scope.createEvent.eventName.$setPristine();
        $scope.createEvent.eventDescription.$setPristine();
        $scope.createEvent.eventDate.$setPristine();
        $scope.createEvent.eventTime.$setPristine();
        $scope.createEvent.eventPlace.$setPristine();
    }
}]);

myapp.controller('eventMenuController', ['$scope', '$log', '$http', function ($scope, $log, $http) {
    $scope.events = [];
    $scope.q ='';
    $scope.loadEvents = function (event) {
        $http({
            url: '/event/get-associated',
            method: 'GET'
        }).success(function (data) {
            $scope.events = data;
        });
    };
    $scope.loadEvents();
}]);

myapp.controller('hostEventController', ['$scope', '$log', '$http', '$location', function ($scope, $log, $http, $location) {
    $log.log("aaaaa");
    $scope.eventId = $location.search().eventId;
    $log.log($scope.eventId);
    $scope.event = {};
    $scope.loadEvent = function (event) {
        $http({
            url: 'event/get?eventid=' + $scope.eventId,
            method: 'GET'
        }).success(function (data) {
            $scope.event = data;
            $log.log(data);
        });
    };
    $scope.loadEvent();
}]);

$('.datepicker').pickadate({
    selectMonths: true, // Creates a dropdown to control month
    selectYears: 15, // Creates a dropdown of 15 years to control year
    format: 'dd/mm/yyyy'
});

$('.timepicker').pickatime({
    autoclose: false,
    twelvehour: false
});