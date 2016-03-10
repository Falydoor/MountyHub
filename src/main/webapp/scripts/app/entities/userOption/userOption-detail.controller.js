'use strict';

angular.module('mountyhubApp')
    .controller('UserOptionDetailController', function ($scope, $rootScope, $stateParams, entity, UserOption, User) {
        $scope.userOption = entity;
        $scope.load = function (id) {
            UserOption.get({id: id}, function(result) {
                $scope.userOption = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:userOptionUpdate', function(event, result) {
            $scope.userOption = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
