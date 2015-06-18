(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('querySet', querySet);

  function querySet() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        set: '=set',
        process: '=process',
        index: '=index'
      },
      templateUrl: 'app/components/query-set/query-set.html',
      controller: controller
    };

    function controller($scope) {

      $scope.removeQuery = removeQuery;
      $scope.addQuery = addQuery;

      function removeQuery(index) {
        $scope.set.queries.splice(index, 1);
      }

      function addQuery() {
        $scope.set.queries.push({
          oid: null,
          description: null
        });
      }

    }
  }
})();
