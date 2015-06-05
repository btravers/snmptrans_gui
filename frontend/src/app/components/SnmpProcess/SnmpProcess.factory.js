(function () {
    'use strict';

    angular
        .module('SnmptransGui')
        .factory('SnmpProcessFactory', SnmpProcessFactory);

    function SnmpProcessFactory() {
        var SnmpProcess = function () {
            var vm = this;

            vm.outputwriter = [];
            vm.querySets = [];
            vm.server = {};
        }

        return new SnmpProcess();
    }
})();