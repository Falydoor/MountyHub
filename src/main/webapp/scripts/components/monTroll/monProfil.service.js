'use strict';

angular.module('mountyhubApp')
    .factory('MonProfil', function ($resource) {
        return $resource('api/monProfil', {}, {
            get: {method: 'GET'},
            save: {method: 'POST'},
            delete: {method: 'DELETE'},
            refresh: {method: 'PUT'},
            refreshTZ: {url: 'api/monProfil/refreshTZ', method: 'PUT'}
        });
    });
