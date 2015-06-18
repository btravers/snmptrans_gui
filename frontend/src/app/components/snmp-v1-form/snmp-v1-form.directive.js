(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('snmpV1Form', snmpV1Form);

  function snmpV1Form() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        agent: '=agent'
      },
      templateUrl: 'app/components/snmp-v1-form/snmp-v1-form.html'
    };
  }
})();
