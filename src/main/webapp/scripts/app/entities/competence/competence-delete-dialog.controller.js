'use strict';

angular.module('mountyhubApp')
	.controller('CompetenceDeleteController', function($scope, $uibModalInstance, entity, Competence) {

        $scope.competence = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Competence.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
