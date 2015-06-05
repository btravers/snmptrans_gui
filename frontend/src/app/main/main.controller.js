(function () {
    'use strict';

    angular
        .module('SnmptransGui')
        .controller('Main', Main);

    function Main($scope, $rootScope, SnmpProcessService) {

        $scope.list = [];
        $scope.SnmpProcess = null;
        $scope.BlankSnmpProcess = null;

        $scope.updateList = updateList;
        $scope.displaySnmpProcess = displaySnmpProcess;
        $scope.deleteSnmpProcess = deleteSnmpProcess;
        $scope.pushSnmpProcess = pushSnmpProcess;
        $scope.printServer = printServer;

        $rootScope.$on('update', function (event, data) {
            $scope.updateList();
            if (data.host && data.port) {
                $scope.displaySnmpProcess(data.host, data.port);
            }
        });

        function updateList() {
            SnmpProcessService.findAllSnmpProcesses().then(function(list) {
                $scope.list = list;
            });
        }

        function displaySnmpProcess(host, port) {
            SnmpProcessService.getSnmpProcess(host, port).then(function(snmpprocess) {
                $scope.SnmpProcess = snmpprocess;
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

                for (var i=0; i<13; i++) {
                    res += host[i];
                }

                res += '...:' + port;

                return res;
            }
        }

    }
})();

