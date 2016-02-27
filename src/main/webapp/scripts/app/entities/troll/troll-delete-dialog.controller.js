'use strict';

angular.module('mountyhubApp')
	.controller('TrollDeleteController', function($scope, $uibModalInstance, entity, Troll) {

        $scope.troll = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Troll.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
