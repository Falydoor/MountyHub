'use strict';

angular.module('mountyhubApp')
	.controller('CompetenceMHDeleteController', function($scope, $uibModalInstance, entity, CompetenceMH) {

        $scope.competenceMH = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            CompetenceMH.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
