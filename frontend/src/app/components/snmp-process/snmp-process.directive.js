(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('snmpProcess', snmpProcess);

  function snmpProcess() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        process: '=process',
        snmpForm: '=snmpForm'
      },
      controller: controller,
      templateUrl: 'app/components/snmp-process/snmp-process.html'
    };
  }

  function controller ($scope, FormErrorHandler) {
    $scope.showErrorMessage = showErrorMessage;

    function showErrorMessage(key) {
      return FormErrorHandler.existError(key);
    }
  }
})();
