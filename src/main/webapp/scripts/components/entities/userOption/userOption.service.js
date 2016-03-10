'use strict';

angular.module('mountyhubApp')
    .factory('UserOption', function ($resource, DateUtils) {
        return $resource('api/userOptions/:id', {}, {
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
