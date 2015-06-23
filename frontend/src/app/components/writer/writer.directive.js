(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('writer', writer);

  function writer() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        writer: '=writer',
        process: '=process',
        index: '=index'
      },
      controller: controller,
      templateUrl: 'app/components/writer/writer.html'
    };

    function controller ($scope, FormErrorHandler) {
      $scope.showErrorMessage = showErrorMessage;

      function showErrorMessage(key) {
        return FormErrorHandler.existError(key);
      }
    }
  }
})();
