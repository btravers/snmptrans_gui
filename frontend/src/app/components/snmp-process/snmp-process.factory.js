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
      vm.querySets = [];
      vm.server = {};

      vm.addWriter = addWriter;
      vm.removeWriter = removeWriter;
      vm.addQuerySet = addQuerySet;
      vm.removeQuerySet = removeQuerySet;
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

      function addQuerySet() {
        vm.querySets.push({
          description: null,
          queries: []
        });
      }

      function removeQuerySet(index) {
        vm.querySets.splice(index, 1);
      }

      function saveSnmpProcess() {
        for (var i = vm.querySets.length-1; i >= 0; i--) {
          for (var j = vm.querySets[i].queries.length-1; j >= 0; j--) {
            if (!vm.querySets[i].queries[j].oid) {
              vm.querySets[i].queries.splice(j, 1);
            }
          }

          if (!vm.querySets[i].queries || !vm.querySets[i].queries.length) {
            vm.querySets.splice(i, 1);
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
