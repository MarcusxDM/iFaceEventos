angular.module('indexComponents', [])

  .directive('opc', function() {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      controller: function($scope, $element) {
          $scope.showUserCreation = false;
          $scope.showLogin = false;

          $scope.showUserCreationForms = function(){
              if($scope.showUserCreation == false){
                  $scope.showUserCreation = true;
                  $scope.showLogin = false;
              }else{
                  $scope.showUserCreation = false;
                  $scope.showLogin = false;
              }
          };

          $scope.showLoginForms = function(){
              if($scope.showLogin == false){
                  $scope.showUserCreation = false;
                  $scope.showLogin = true;
              }else{
                  $scope.showUserCreation = false;
                  $scope.showLogin = false;
              }
          };
      },
      template:
        '<div>' +
            '<p><a href="" ng-click="showUserCreationForms()">User creation</a><p>' +
            '<div class="check-element animate-show" ng-show="showUserCreation">' +
                '<create></create>' +
            '</div>' +
            '<p><a href="" ng-click="showLoginForms()">Login</a><p>' +
            '<div class="check-element animate-show" ng-show="showLogin">' +
                '<login></login>' +
            '</div>' +
        '</div>',
      replace: true
    };
  });
