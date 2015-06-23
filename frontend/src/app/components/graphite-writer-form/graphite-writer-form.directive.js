(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('graphiteWriterForm', graphiteWriterForm);

  function graphiteWriterForm() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        writer: '=writer'
      },
      controller: controller,
      templateUrl: 'app/components/graphite-writer-form/graphite-writer-form.html'
    };

    function controller ($scope, FormErrorHandler) {
      $scope.showErrorMessage = showErrorMessage;

      function showErrorMessage(key) {
        return FormErrorHandler.existError(key);
      }
    }
  }
})();
