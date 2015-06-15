(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('writer', writer);

  function writer() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        writer: '=writer'
      },
      templateUrl: 'app/components/writer/writer.html'
    };
  }
})();
