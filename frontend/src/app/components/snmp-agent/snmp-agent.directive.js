(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('snmpAgent', snmpAgent);

  function snmpAgent() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        agent: '=agent'
      },
      controller: controller,
      templateUrl: 'app/components/snmp-agent/snmp-agent.html'
    };

    function controller ($scope, FormErrorHandler) {
      $scope.showErrorMessage = showErrorMessage;

      function showErrorMessage(key) {
        return FormErrorHandler.existError(key);
      }
    }
  }
})();
