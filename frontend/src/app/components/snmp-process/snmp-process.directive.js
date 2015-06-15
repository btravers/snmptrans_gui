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
        process: '=process'
      },
      templateUrl: 'app/components/snmp-process/snmp-process.html'
    };
  }
})();
