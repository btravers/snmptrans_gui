(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .factory('SnmpProcessFactory', SnmpProcessFactory);

  function SnmpProcessFactory($rootScope, SnmpProcessService, ngToast) {
    var SnmpProcess = function () {
      var vm = this;

      vm.id = null;
      vm.writers = [];
      vm.queries = [];
      vm.server = {};

      vm.addWriter = addWriter;
      vm.removeWriter = removeWriter;
      vm.addQuery = addQuery;
      vm.removeQuery = removeQuery;
      vm.saveSnmpProcess = saveSnmpProcess;

      function addWriter() {
        vm.writers.push({
          '@class': null,
          settings: {}
        });
      }

      function removeWriter(index) {
        vm.writers.splice(index, 1);
      }

      function addQuery() {
        vm.queries.push({
          obj: null,
          resultAlias: null,
          attr: [],
          typeName: null
        });
      }

      function removeQuery(index) {
        vm.queries.splice(index, 1);
      }

      function saveSnmpProcess() {
        for (var i = vm.queries.length-1; i >= 0; i--) {
          if (!vm.queries[i].obj) {
            vm.queries.splice(i, 1);
          }

          for (var j = vm.queries[i].attr.length-1; j>=0; j--) {
            if (!vm.queries[i].attr[j]) {
              vm.queries[i].attr.splice(j, 1);
            }
          }
        }

        for (var i = vm.writers.length-1; i >= 0; i--) {
          if (!vm.writers[i]['@class']) {
            vm.writers.splice(i, 1);
          }
        }

        SnmpProcessService.pushSnmpProcess(vm).then(function() {
          ngToast.create({
            className: 'success',
            content: 'Save SNMP process successfully'
          });

          $rootScope.$emit('update', {
            host: vm.server.host,
            port: vm.server.port
          });
        }, function (error) {
          ngToast.create({
            className: 'danger',
            content: 'An error occurred when saving SNMP process'
          });
          // TODO display error message
        });
      }
    };

    return SnmpProcess;
  }
})();
