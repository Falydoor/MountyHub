'use strict';

angular.module('mountyhubApp').controller('BonusMalusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BonusMalus', 'Troll',
        function($scope, $stateParams, $uibModalInstance, entity, BonusMalus, Troll) {

        $scope.bonusMalus = entity;
        $scope.trolls = Troll.query();
        $scope.load = function(id) {
            BonusMalus.get({id : id}, function(result) {
                $scope.bonusMalus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:bonusMalusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.bonusMalus.id != null) {
                BonusMalus.update($scope.bonusMalus, onSaveSuccess, onSaveError);
            } else {
                BonusMalus.save($scope.bonusMalus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
