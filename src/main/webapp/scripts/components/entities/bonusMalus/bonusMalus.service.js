'use strict';

angular.module('mountyhubApp')
    .factory('BonusMalus', function ($resource, DateUtils) {
        return $resource('api/bonusMaluss/:id', {}, {
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
