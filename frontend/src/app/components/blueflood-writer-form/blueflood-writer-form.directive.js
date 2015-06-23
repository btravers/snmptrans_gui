(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('bluefloodWriterForm', bluefloodWriterForm);

  function bluefloodWriterForm() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        writer: '=writer'
      },
      controller: controller,
      templateUrl: 'app/components/blueflood-writer-form/blueflood-writer-form.html'
    };

    function controller ($scope, FormErrorHandler) {
      $scope.showErrorMessage = showErrorMessage;

      function showErrorMessage(key) {
        return FormErrorHandler.existError(key);
      }
    }
  }
})();
