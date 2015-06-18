(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('snmpV2cForm', snmpV2cForm);

  function snmpV2cForm() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        agent: '=agent'
      },
      templateUrl: 'app/components/snmp-v2c-form/snmp-v2c-form.html'
    };
  }
})();
