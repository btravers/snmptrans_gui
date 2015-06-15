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

      vm.addOutputWriter = addOutputWriter;
      vm.removeOutputWriter = removeOutputWriter;
      vm.addQuerySet = addQuerySet;
      vm.removeQuerySet = removeQuerySet;
      vm.saveSnmpProcess = saveSnmpProcess;

      function addOutputWriter() {

      }

      function removeOutputWriter() {

      }

      function addQuerySet() {

      }

      function removeQuerySet() {

      }

      function saveSnmpProcess() {

      }
    };

    return SnmpProcess;
  }
})();
