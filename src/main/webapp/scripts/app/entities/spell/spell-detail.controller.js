'use strict';

angular.module('mountyhubApp')
    .controller('SpellDetailController', function ($scope, $rootScope, $stateParams, entity, Spell, SpellMH, Troll) {
        $scope.spell = entity;
        $scope.load = function (id) {
            Spell.get({id: id}, function(result) {
                $scope.spell = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:spellUpdate', function(event, result) {
            $scope.spell = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
