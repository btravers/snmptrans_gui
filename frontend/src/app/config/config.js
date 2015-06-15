(function () {
  'use strict';

  angular
    .module('SnmptransGui')
    .service('config', config);

  function config($location) {
    var url;
    var reloadInterval = 60000;

    this.getUrl = getUrl;
    this.getReloadInterval = getReloadInterval;

    function getUrl() {
      if(!url) {
        if ($location.port() == 3000) {
          url = $location.protocol() + '://' + $location.host() + ':8080/';
        } else {
          url = '';
        }
      }

      return url;
    }

    function getReloadInterval() {
      return reloadInterval;
    }
  }
})();
