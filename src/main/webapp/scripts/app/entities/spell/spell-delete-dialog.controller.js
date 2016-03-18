'use strict';

angular.module('mountyhubApp')
	.controller('SpellDeleteController', function($scope, $uibModalInstance, entity, Spell) {

        $scope.spell = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Spell.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
