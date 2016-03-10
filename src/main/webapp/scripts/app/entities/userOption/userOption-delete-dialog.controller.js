'use strict';

angular.module('mountyhubApp')
	.controller('UserOptionDeleteController', function($scope, $uibModalInstance, entity, UserOption) {

        $scope.userOption = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserOption.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
