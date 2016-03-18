'use strict';

angular.module('mountyhubApp')
    .controller('SpellMHDetailController', function ($scope, $rootScope, $stateParams, entity, SpellMH) {
        $scope.spellMH = entity;
        $scope.load = function (id) {
            SpellMH.get({id: id}, function(result) {
                $scope.spellMH = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:spellMHUpdate', function(event, result) {
            $scope.spellMH = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
