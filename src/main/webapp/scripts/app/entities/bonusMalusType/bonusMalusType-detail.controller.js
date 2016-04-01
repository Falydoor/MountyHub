'use strict';

angular.module('mountyhubApp')
    .controller('BonusMalusTypeDetailController', function ($scope, $rootScope, $stateParams, entity, BonusMalusType, BonusMalus) {
        $scope.bonusMalusType = entity;
        $scope.load = function (id) {
            BonusMalusType.get({id: id}, function(result) {
                $scope.bonusMalusType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:bonusMalusTypeUpdate', function(event, result) {
            $scope.bonusMalusType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
