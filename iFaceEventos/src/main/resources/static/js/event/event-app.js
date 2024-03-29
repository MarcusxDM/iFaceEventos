var myapp = angular.module('event', ['ui.calendar']);

myapp.controller('createEventController', ['$scope', '$log', function ($scope, $log) {
    $scope.event = {};
    $scope.name='';
    $scope.description = '';
    $scope.date = '';
    $scope.location = '';

    $scope.clear = function () {
        $scope.name = '';
        $scope.description = '';
        $scope.time = '';
        $scope.place = '';
        $scope.createEvent.name.$setPristine();
        $scope.createEvent.description.$setPristine();
        $scope.createEvent.date.$setPristine();
        $scope.createEvent.time.$setPristine();
        $scope.createEvent.place.$setPristine();
    }
}]);


myapp.controller('editEventController', ['$scope', '$log', '$http', '$location', function ($scope, $log, $http, $location) {
    $scope.event = {};
    $scope.name='';
    $scope.description = '';
    $scope.date = '';
    $scope.location = '';
    $scope.formSubmit = function () {
        if(! $scope.createEvent.$valid) {
            $scope.createEvent.name.$setDirty();
            $scope.createEvent.description.$setDirty();
            $scope.createEvent.date.$setDirty();
            $scope.createEvent.time.$setDirty();
            $scope.createEvent.location.$setDirty();
        } else {
            $scope.date = $scope.event.date;
        }
    };

    $scope.clear = function () {
        $scope.name = '';
        $scope.description = '';
        $scope.time = '';
        $scope.location = '';
    };
    $scope.event1 = [];
    $scope.eventId = $location.path();
    $scope.id = $scope.eventId.substr(1);
    var formatDate = function (date) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [day, month, year].join('/');
    };
    var formatDateTime = function(date) {
        var d = new Date(date), hours = '' + d.getHours(), minutes = '' + d.getMinutes();

        if (hours.length < 2) hours = '0' + hours;
        if (minutes.length < 2) minutes = '0' + minutes;

        return [hours, minutes].join(':');
    };
    $scope.loadEvent = function (event) {
        $http({
            url: '/event/get?eventId='+$scope.id,
            method: 'GET'
        }).success(function (data) {
            $log.log(data);
            $scope.event1 = data;
            $scope.eventId = $scope.event1.id;
            $scope.name = $scope.event1.name;
            $scope.description = $scope.event1.description;
            $scope.date = formatDate(data.date);
            $scope.time = formatDateTime(data.date);
            $scope.location = $scope.event1.location;
            $scope.id = $scope.event1.id;
            $scope.createEvent.name.$setDirty();
            $scope.createEvent.description.$setDirty();
            $scope.createEvent.date.$setDirty();
            $scope.createEvent.time.$setDirty();
            $scope.createEvent.location.$setDirty();
        });
    };

    $scope.loadEvent();
}]);
myapp.controller('eventMenuController', ['$scope', '$log', '$http', '$timeout', function ($scope, $log, $http, $timeout) {
    $scope.events = [];
    $scope.allEvents = [];
    $scope.q='';
    $scope.host = {};
    $scope.loadEvents = function (event) {
        $http({
            url: '/event/get-associated',
            method: 'GET'
        }).success(function (data) {
            $scope.events = data;
            $scope.host = $scope.events[0].host;
            $log.log($scope.host);
            for (var i = 0; i < $scope.events.length; ++i ) {
                $scope.addCalendarEvent($scope.events[i], 0, '#00f');
            }
            $('#calendar').fullCalendar('refetchEvents');
            // $timeout(function () {
            //     for (var i = 0; i < $scope.events.length; ++i ) {
            //         $scope.addCalendarEvent($scope.events[i], 0, '#00f');
            //     }
            //     $('#calendar').fullCalendar('refetchEvents');
            // }, 500)
        });
    };
    $scope.loadAllEvents = function () {
        $http({
            url: '/event/get-all',
            method: 'GET'
        }).success(function (data) {
            $scope.allEvents = data;
            $timeout(function () {
                for (var i = 0; i < $scope.allEvents.length; ++i ) {
                    if($scope.allEvents[i].host.username != $scope.host.username) {
                        $scope.addCalendarEvent($scope.allEvents[i], 1, '#000');
                    }
                }
                $('#calendar').fullCalendar('refetchEvents');
            }, 500)
        });
    };
    $scope.loadEvents();
    $scope.loadAllEvents();

    $scope.uiConfig = {
        calendar:{
            height: 550,
            header:{
                left: 'title',
                center: '',
                right: 'today prev,next'
            }
        }
    };

    var date = new Date(),
        d = date.getDate(),
        m = date.getMonth(),
        y = date.getFullYear();
    $scope.eventSources = [{
        events: []
    }, {
        events: []
    }];

    $scope.addCalendarEvent = function (event, index, color) {
        $scope.eventSources[index].events.push({
            title: event.name,
            start: new Date(event.date),
            url: '/event/profile-host?eventId='+event.id,
            stick: true,
            color: color
        });
    };

}]);

myapp.controller('hostEventController', ['$scope', '$log', '$http', '$location', function ($scope, $log, $http, $location) {
    $scope.eventId = $location.path();
    $log.log("aaaa " + $scope.eventId);
    $scope.id = $scope.eventId.substr(1);
    $scope.event = {};
    $scope.loadEvent = function (event) {
        $http({
            url: '/event/get?eventId=' + $scope.id,
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
    format: 'dd/mm/yyyy',
    min: new Date(),
    clear: '',
    onStart: function() {
        var date = new Date();
        this.set('select', [date.getFullYear(), date.getMonth(), date.getDate()]);
    }
});

$('.timepicker').pickatime({
    autoclose: false,
    twelvehour: false
});