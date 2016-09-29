angular.module('loginComponents', [])

.directive('login', function() {
  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    templateUrl: '../../login-form.html',
    replace: true
  };
})
