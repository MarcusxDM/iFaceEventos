angular.module('creationComponents', [])

.directive('create', function() {
  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    templateUrl: '../../create-form.html',
    replace: true
  };
})
