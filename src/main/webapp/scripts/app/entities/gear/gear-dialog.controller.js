'use strict';

angular.module('mountyhubApp').controller('GearDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Gear', 'Troll',
        function($scope, $stateParams, $uibModalInstance, entity, Gear, Troll) {

        $scope.gear = entity;
        $scope.trolls = Troll.query();
        $scope.load = function(id) {
            Gear.get({id : id}, function(result) {
                $scope.gear = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:gearUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.gear.id != null) {
                Gear.update($scope.gear, onSaveSuccess, onSaveError);
            } else {
                Gear.save($scope.gear, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
