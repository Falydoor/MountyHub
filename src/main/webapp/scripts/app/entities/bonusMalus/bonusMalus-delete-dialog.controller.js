'use strict';

angular.module('mountyhubApp')
	.controller('BonusMalusDeleteController', function($scope, $uibModalInstance, entity, BonusMalus) {

        $scope.bonusMalus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BonusMalus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
