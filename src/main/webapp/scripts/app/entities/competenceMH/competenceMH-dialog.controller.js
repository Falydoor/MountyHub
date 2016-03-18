'use strict';

angular.module('mountyhubApp').controller('CompetenceMHDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompetenceMH',
        function($scope, $stateParams, $uibModalInstance, entity, CompetenceMH) {

        $scope.competenceMH = entity;
        $scope.load = function(id) {
            CompetenceMH.get({id : id}, function(result) {
                $scope.competenceMH = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:competenceMHUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.competenceMH.id != null) {
                CompetenceMH.update($scope.competenceMH, onSaveSuccess, onSaveError);
            } else {
                CompetenceMH.save($scope.competenceMH, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
