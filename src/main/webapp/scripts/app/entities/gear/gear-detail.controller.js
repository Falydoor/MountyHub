'use strict';

angular.module('mountyhubApp')
    .controller('GearDetailController', function ($scope, $rootScope, $stateParams, entity, Gear, Troll) {
        $scope.gear = entity;
        $scope.load = function (id) {
            Gear.get({id: id}, function(result) {
                $scope.gear = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:gearUpdate', function(event, result) {
            $scope.gear = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
