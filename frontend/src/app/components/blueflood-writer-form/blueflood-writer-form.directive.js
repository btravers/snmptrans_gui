(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .directive('bluefloodWriterForm', bluefloodWriterForm);

  function bluefloodWriterForm() {
    return {
      restrict: 'E',
      replace: true,
      scope: {
        writer: '=writer'
      },
      templateUrl: 'app/components/blueflood-writer-form/blueflood-writer-form.html'
    };
  }
})();
