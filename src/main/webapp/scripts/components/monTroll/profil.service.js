'use strict';

angular.module('mountyhubApp')
    .factory('Profil', function ($resource) {
        return $resource('api/profil', {}, {
            'get': {method: 'GET'},
            'save': {method: 'POST'}
        });
    });
