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
      templateUrl: 'app/components/snmp-agent/snmp-agent.html'
    };
  }
})();
