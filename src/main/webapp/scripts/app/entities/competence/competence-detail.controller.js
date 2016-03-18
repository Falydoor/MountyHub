'use strict';

angular.module('mountyhubApp')
    .controller('CompetenceDetailController', function ($scope, $rootScope, $stateParams, entity, Competence, CompetenceMH, Troll) {
        $scope.competence = entity;
        $scope.load = function (id) {
            Competence.get({id: id}, function(result) {
                $scope.competence = result;
            });
        };
        var unsubscribe = $rootScope.$on('mountyhubApp:competenceUpdate', function(event, result) {
            $scope.competence = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
