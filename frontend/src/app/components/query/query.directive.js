(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('query', query);

  function query() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        process: '=process',
        query: '=query',
        snmpForm: '=snmpForm'
      },
      controller: controller,
      templateUrl: 'app/components/query/query.html'
    };

    function controller($scope, FormErrorHandler) {
      $scope.addAttr = addAttr;
      $scope.removeAttr = removeAttr;
      $scope.showErrorMessage = showErrorMessage;

      function showErrorMessage(key) {
        return FormErrorHandler.existError(key);
      }

      function addAttr() {
        $scope.query.attr.push(null);
      }

      function removeAttr(index) {
        $scope.query.attr.splice(index, 1);
        $scope.snmpForm.$setDirty();
      }
    }
  }
})();
