'use strict';

angular.module('mountyhubApp')
	.controller('FlyDeleteController', function($scope, $uibModalInstance, entity, Fly) {

        $scope.fly = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Fly.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
