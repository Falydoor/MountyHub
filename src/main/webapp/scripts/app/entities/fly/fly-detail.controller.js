'use strict';

angular.module('mountyhubApp')
    .controller('FlyDetailController', function ($scope, $rootScope, $stateParams, entity, Fly, Troll) {
        $scope.fly = entity;
        $scope.load = function (id) {
            Fly.get({id: id}, function(result) {
                $scope.fly = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:flyUpdate', function(event, result) {
            $scope.fly = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
