(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .service('FormErrorHandler', FormErrorHandler);

  function FormErrorHandler() {

    this.setErrors = setErrors;
    this.existError = existError;

    function setErrors(errors) {
      this.errors = {};
      angular.forEach(errors, function (error) {
        this[error.field] = error.message;
      }, this.errors);
    }

    function existError(key) {
      if (!this.errors) {
        return false;
      }

      return key in this.errors;
    }

  }
})();
