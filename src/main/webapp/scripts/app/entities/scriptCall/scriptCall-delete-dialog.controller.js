'use strict';

angular.module('mountyhubApp')
	.controller('ScriptCallDeleteController', function($scope, $uibModalInstance, entity, ScriptCall) {

        $scope.scriptCall = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ScriptCall.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
