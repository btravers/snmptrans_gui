(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .service('SnmpProcessService', SnmpProcessService);

  function SnmpProcessService($http, $q, $rootScope, config, ngToast) {
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
          ngToast.create({
            className: 'danger',
            content: 'An error occurred when loading servers list'
          });
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
          ngToast.create({
            className: 'danger',
            content: 'An error occurred when loading the SNMP process'
          });
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
          ngToast.create({
            className: 'success',
            content: 'Save SNMP process successfully'
          });
          $rootScope.$emit('update', {
            host: snmpprocess.server.host,
            port: snmpprocess.server.port
          });
        })
        .error(function (response) {
          ngToast.create({
            className: 'danger',
            content: 'An error occurred when saving SNMP process'
          });
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
          ngToast.create({
            className: 'success',
            content: 'Delete SNMP process successfully'
          });
          result.resolve();
        })
        .error(function () {
          ngToast.create({
            className: 'danger',
            content: 'An error occurred during the deletion of the SNMP process'
          });
          result.reject();
        });

      return result.promise;
    }
  }
})();
