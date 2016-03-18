'use strict';

angular.module('mountyhubApp')
    .factory('Spell', function ($resource, DateUtils) {
        return $resource('api/spells/:id', {}, {
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
