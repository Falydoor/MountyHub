'use strict';

angular.module('mountyhubApp')
    .controller('CompetenceMHDetailController', function ($scope, $rootScope, $stateParams, entity, CompetenceMH) {
        $scope.competenceMH = entity;
        $scope.load = function (id) {
            CompetenceMH.get({id: id}, function(result) {
                $scope.competenceMH = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:competenceMHUpdate', function(event, result) {
            $scope.competenceMH = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
