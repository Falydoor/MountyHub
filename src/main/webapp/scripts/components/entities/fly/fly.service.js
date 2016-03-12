'use strict';

angular.module('mountyhubApp')
    .factory('Fly', function ($resource, DateUtils) {
        return $resource('api/flys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
