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
        set: '=set'
      },
      templateUrl: 'app/components/query-set/query-set.html'
    };
  }
})();
