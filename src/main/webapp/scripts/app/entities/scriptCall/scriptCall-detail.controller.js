'use strict';

angular.module('mountyhubApp')
    .controller('ScriptCallDetailController', function ($scope, $rootScope, $stateParams, entity, ScriptCall, Troll) {
        $scope.scriptCall = entity;
        $scope.load = function (id) {
            ScriptCall.get({id: id}, function(result) {
                $scope.scriptCall = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:scriptCallUpdate', function(event, result) {
            $scope.scriptCall = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
