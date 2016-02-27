 'use strict';

angular.module('mountyhubApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-mountyhubApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-mountyhubApp-params')});
                }
                return response;
            }
        };
    });
