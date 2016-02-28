'use strict';

angular.module('mountyhubApp')
    .factory('Troll', function ($resource, DateUtils) {
        return $resource('api/trolls/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthDate = DateUtils.convertDateTimeFromServer(data.birthDate);
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    })
    .factory('AddTroll', function ($resource, DateUtils) {
        return $resource('api/addTroll', {}, {
            'query': {method: 'GET', params: {number: 1, restrictedPassword: 'a'}}
        });
    });
