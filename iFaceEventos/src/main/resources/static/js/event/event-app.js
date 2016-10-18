var myapp = angular.module('event', []);

myapp.controller('createEventController', ['$scope', '$log', 'eventService', function ($scope, $log, eventService) {
    $scope.event = {};
    $scope.formSubmit = function () {
        if(! $scope.createEvent.$valid) {
            $scope.createEvent.eventName.$setDirty();
            $scope.createEvent.eventDescription.$setDirty();
            $scope.createEvent.eventDate.$setDirty();
            $scope.createEvent.eventTime.$setDirty();
            $scope.createEvent.eventPlace.$setDirty();
        } else {
            $log.log('teste');
            eventService.createEvent($scope.event);
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


myapp.service('eventService', ['$http', '$log', function ($http, $log) {
    this.createEvent = function (event) {
        $http({
            url: '/event/create2',
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            data: event
        }).success(function (data) {
            $log.log(data);
        });
    }
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