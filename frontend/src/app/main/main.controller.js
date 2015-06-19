(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .controller('Main', Main);

  function Main($scope, $rootScope, $http, $modal, FileUploader, config, SnmpProcessService, SnmpProcessFactory, ngToast) {

    $scope.list = [];
    $scope.SnmpProcess = null;

    $scope.updateList = updateList;
    $scope.displaySnmpProcess = displaySnmpProcess;
    $scope.deleteSnmpProcess = deleteSnmpProcess;
    $scope.duplicateSnmpProcess = duplicateSnmpProcess;
    $scope.printServer = printServer;
    $scope.createSnmpProcess = createSnmpProcess;
    $scope.downloadSnmpProcess = downloadSnmpProcess;
    $scope.openUploader = openUploader;

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
      }, function () {
        ngToast.create({
          className: 'danger',
          content: 'An error occurred when loading servers list'
        });
      });
    }

    function displaySnmpProcess(host, port) {
      SnmpProcessService.getSnmpProcess(host, port).then(function (snmpprocess) {
        $scope.SnmpProcess = new SnmpProcessFactory();

        $scope.SnmpProcess.id = snmpprocess.id;
        $scope.SnmpProcess.server = snmpprocess.server;
        $scope.SnmpProcess.queries = snmpprocess.queries;
        $scope.SnmpProcess.writers = snmpprocess.writers;
      }, function () {
        ngToast.create({
          className: 'danger',
          content: 'An error occurred when loading the SNMP process'
        });
      });
    }

    function deleteSnmpProcess(host, port) {
      SnmpProcessService.deleteSnmpProcess(host, port).then(function () {
        ngToast.create({
          className: 'success',
          content: 'Delete SNMP process successfully'
        });

        $scope.updateList();
        $scope.SnmpProcess = null;
      }, function() {
        ngToast.create({
          className: 'danger',
          content: 'An error occurred during the deletion of the SNMP process'
        });
      });
    }

    function duplicateSnmpProcess(host, port) {
      SnmpProcessService.getSnmpProcess(host, port).then(function (snmpprocess) {
        $scope.SnmpProcess = new SnmpProcessFactory();

        $scope.SnmpProcess.server = {};
        $scope.SnmpProcess.queries = snmpprocess.queries;
        $scope.SnmpProcess.writers = snmpprocess.writers;
      });
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

    function createSnmpProcess() {
      $scope.SnmpProcess = new SnmpProcessFactory();

      $scope.SnmpProcess.server = {};
      $scope.SnmpProcess.queries = [];
      $scope.SnmpProcess.writers = [];
    }

    function downloadSnmpProcess(host, port) {
      var req = {
        url: config.getUrl() + 'snmpprocess/download',
        method: 'GET',
        params: {
          host: host,
          port: port
        },
        headers: {
          'Content-type': 'application/json'
        },
        responseType: 'arraybuffer'
      };

      $http(req)
        .success(function (data) {
          var file = new Blob([data], {
            type: 'application/json'
          });

          var fileURL = URL.createObjectURL(file);
          var serverFile = document.createElement('a');
          serverFile.href = fileURL;
          serverFile.target = '_blank';
          serverFile.download = host + ':' + port + '.json';
          document.body.appendChild(serverFile);
          serverFile.click();
        })
        .error(function () {

        });
    }

    function openUploader() {
      var modal = $modal.open({
        templateUrl: 'app/components/uploader/uploader.html',
        controller: function ($scope, $modalInstance, config, updateList) {

          var url = config.getUrl() + 'snmpprocess/upload';

          $scope.uploader = new FileUploader({
            url: url
          });

          $scope.uploader.onCompleteAll = function () {
            updateList();
          };

          $scope.cancel = function () {
            $modalInstance.dismiss();
          };

        }, resolve: {
          updateList: function () {
            return $scope.updateList;
          }
        }
      });

      modal.result.then(function () {

      }, function () {

      });
    }

  }
})();

