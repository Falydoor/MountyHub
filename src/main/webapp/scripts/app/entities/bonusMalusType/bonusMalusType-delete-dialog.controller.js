'use strict';

angular.module('mountyhubApp')
	.controller('BonusMalusTypeDeleteController', function($scope, $uibModalInstance, entity, BonusMalusType) {

        $scope.bonusMalusType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BonusMalusType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
