'use strict';

angular.module('mountyhubApp')
    .factory('SpellMH', function ($resource, DateUtils) {
        return $resource('api/spellMHs/:id', {}, {
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
