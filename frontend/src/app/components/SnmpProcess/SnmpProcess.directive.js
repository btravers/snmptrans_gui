(function () {
    'use strict';

    angular
        .module('SnmptransGui')
        .directive('snmpProcess', snmpProcess);

    function SnmpProcess() {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                process: '=process'
            },
            templateUrl: 'app/components/SnmpProcess/SnmpProcess.html'
        };
    }
})();
