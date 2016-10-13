angular.module('indexComponents', [])

  .directive('opc', function() {
    return {
      restrict: 'E',
      transclude: true,
      scope: {},
      controller: function($scope, $element) {
          $scope.showUserCreation = false;
          $scope.showLogin = true;

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
      templateUrl: 'js/index/opc.html',
      replace: true
    };
  });
