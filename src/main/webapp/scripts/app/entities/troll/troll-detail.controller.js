'use strict';

angular.module('mountyhubApp')
    .controller('TrollDetailController', function ($scope, $rootScope, $stateParams, entity, Troll, User, ScriptCall, Gear, Fly, Competence) {
        $scope.troll = entity;
        $scope.load = function (id) {
            Troll.get({id: id}, function(result) {
                $scope.troll = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:trollUpdate', function(event, result) {
            $scope.troll = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
