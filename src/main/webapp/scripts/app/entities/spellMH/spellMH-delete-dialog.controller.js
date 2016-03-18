'use strict';

angular.module('mountyhubApp')
	.controller('SpellMHDeleteController', function($scope, $uibModalInstance, entity, SpellMH) {

        $scope.spellMH = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SpellMH.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
