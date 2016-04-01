'use strict';

angular.module('mountyhubApp').controller('BonusMalusTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BonusMalusType', 'BonusMalus',
        function($scope, $stateParams, $uibModalInstance, entity, BonusMalusType, BonusMalus) {

        $scope.bonusMalusType = entity;
        $scope.bonusmaluss = BonusMalus.query();
        $scope.load = function(id) {
            BonusMalusType.get({id : id}, function(result) {
                $scope.bonusMalusType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:bonusMalusTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.bonusMalusType.id != null) {
                BonusMalusType.update($scope.bonusMalusType, onSaveSuccess, onSaveError);
            } else {
                BonusMalusType.save($scope.bonusMalusType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
