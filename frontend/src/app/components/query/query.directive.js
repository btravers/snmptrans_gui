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
        query: '=query'
      },
      controller: controller,
      templateUrl: 'app/components/query/query.html'
    };

    function controller($scope) {
      $scope.addAttr = addAttr;
      $scope.removeAttr = removeAttr;

      function addAttr() {
        $scope.query.attr.push(null);
      }

      function removeAttr(index) {
        $scope.query.attr.splice(index, 1);
      }
    }
  }
})();
