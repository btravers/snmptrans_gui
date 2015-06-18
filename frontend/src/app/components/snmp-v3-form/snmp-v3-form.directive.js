(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('snmpV3Form', snmpV3Form);

  function snmpV3Form() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        agent: '=agent'
      },
      templateUrl: 'app/components/snmp-v3-form/snmp-v3-form.html'
    };
  }
})();
