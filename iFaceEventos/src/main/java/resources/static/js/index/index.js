angular.module('index', ['indexComponents', 'loginComponents', 'creationComponents'])

.controller('validateCtrl', function($scope) {
    $scope.username = '';
    $scope.password = '';
    $scope.email = '';
});
