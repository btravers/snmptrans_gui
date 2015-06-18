(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .service('SnmpProcessService', SnmpProcessService);

  function SnmpProcessService($http, $q, config) {
    this.findAllSnmpProcesses = findAllSnmpProcesses;
    this.getSnmpProcess = getSnmpProcess;
    this.pushSnmpProcess = pushSnmpProcess;
    this.deleteSnmpProcess = deleteSnmpProcess;

    function findAllSnmpProcesses() {
      var req = {
        method: 'GET',
        url: 'snmpprocess/all'
      };

      req.url = config.getUrl() + req.url;

      var list = $q.defer();

      $http(req)
        .success(function (response) {
          list.resolve(response);
        })
        .error(function () {
          list.reject();
        });

      return list.promise;
    }

    function getSnmpProcess(host, port) {
      var req = {
        method: 'GET',
        url: 'snmpprocess',
        params: {
          host: host,
          port: port
        }
      };

      req.url = config.getUrl() + req.url;

      var snmpprocess = $q.defer();

      $http(req)
        .success(function (response) {
          snmpprocess.resolve(response);
        })
        .error(function () {
          snmpprocess.reject();
        });

      return snmpprocess.promise;
    }

    function pushSnmpProcess(snmpprocess) {
      var req = {
        method: 'POST',
        url: 'snmpprocess',
        data: snmpprocess
      };

      req.url = config.getUrl() + req.url;

      var result = $q.defer();

      $http(req)
        .success(function () {
          result.resolve();
        })
        .error(function (response) {
          result.reject(response);
        });

      return result.promise;
    }

    function deleteSnmpProcess(host, port) {
      var req = {
        method: 'DELETE',
        url: 'snmpprocess',
        params: {
          host: host,
          port: port
        }
      };

      req.url = config.getUrl() + req.url;

      var result = $q.defer();

      $http(req)
        .success(function () {
          result.resolve();
        })
        .error(function () {
          result.reject();
        });

      return result.promise;
    }
  }
})();
