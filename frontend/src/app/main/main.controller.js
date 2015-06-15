(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .controller('Main', Main);

  function Main($scope, $rootScope, config, SnmpProcessService, SnmpProcessFactory) {

    $scope.list = [];
    $scope.SnmpProcess = null;
    $scope.BlankSnmpProcess = null;

    $scope.updateList = updateList;
    $scope.displaySnmpProcess = displaySnmpProcess;
    $scope.deleteSnmpProcess = deleteSnmpProcess;
    $scope.pushSnmpProcess = pushSnmpProcess;
    $scope.printServer = printServer;
    $scope.createBlankSnmpProcess = createBlankSnmpProcess;
    $scope.removeBlankSnmpProcess = removeBlankSnmpProcess;

    $rootScope.$on('update', function (event, data) {
      $scope.updateList();
      if (data.host && data.port) {
        $scope.displaySnmpProcess(data.host, data.port);
      }
    });

    init();

    function init() {
      $scope.updateList();
      setInterval($scope.updateList, config.getReloadInterval());
    }

    function updateList() {
      SnmpProcessService.findAllSnmpProcesses().then(function (list) {
        $scope.list = list;
      });
    }

    function displaySnmpProcess(host, port) {
      SnmpProcessService.getSnmpProcess(host, port).then(function (snmpprocess) {
        $scope.SnmpProcess = new SnmpProcessFactory();

        $scope.SnmpProcess.server = snmpprocess.server;
        $scope.SnmpProcess.querySet = snmpprocess.querySet;
        $scope.SnmpProcess.outputwriter = snmpprocess.outputwriter;

        $scope.BlankSnmpProcess = null;
      });
    }

    function deleteSnmpProcess(host, port) {
      SnmpProcessService.deleteSnmpProcess(host, port);
    }

    function pushSnmpProcess() {
      if ($scope.SnmpProcess) {
        SnmpProcessService.pushSnmpProcess($scope.SnmpProcess);
      } else if ($scope.BlankSnmpProcess) {
        SnmpProcessService.pushSnmpProcess($scope.BlankSnmpProcess);
      }
    }

    function printServer(host, port) {
      if (host.length < 16) {
        return host + ':' + port;
      } else {
        var res = '';

        for (var i = 0; i < 13; i++) {
          res += host[i];
        }

        res += '...:' + port;

        return res;
      }
    }

    function createBlankSnmpProcess() {
      $scope.BlankSnmpProcess = new SnmpProcessFactory();

      $scope.BlankSnmpProcess.server = {};
      $scope.BlankSnmpProcess.querySet = [];
      $scope.BlankSnmpProcess.outputwriter = {};

      $scope.SnmpProcess = null;
    }

    function removeBlankSnmpProcess() {
      $scope.BlankSnmpProcess = null;
    }

  }
})();

