'use strict';

angular.module('mountyhubApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


