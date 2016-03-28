'use strict';

angular.module('mountyhubApp')
    .controller('BonusMalusDetailController', function ($scope, $rootScope, $stateParams, entity, BonusMalus, Troll) {
        $scope.bonusMalus = entity;
        $scope.load = function (id) {
            BonusMalus.get({id: id}, function(result) {
                $scope.bonusMalus = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:bonusMalusUpdate', function(event, result) {
            $scope.bonusMalus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
