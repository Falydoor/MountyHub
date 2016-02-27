'use strict';

angular.module('mountyhubApp')
    .factory('ScriptCall', function ($resource, DateUtils) {
        return $resource('api/scriptCalls/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateCalled = DateUtils.convertDateTimeFromServer(data.dateCalled);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
