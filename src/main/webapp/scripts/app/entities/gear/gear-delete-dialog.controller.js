'use strict';

angular.module('mountyhubApp')
	.controller('GearDeleteController', function($scope, $uibModalInstance, entity, Gear) {

        $scope.gear = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Gear.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
