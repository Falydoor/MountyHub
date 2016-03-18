'use strict';

angular.module('mountyhubApp').controller('SpellMHDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpellMH',
        function($scope, $stateParams, $uibModalInstance, entity, SpellMH) {

        $scope.spellMH = entity;
        $scope.load = function(id) {
            SpellMH.get({id : id}, function(result) {
                $scope.spellMH = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:spellMHUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.spellMH.id != null) {
                SpellMH.update($scope.spellMH, onSaveSuccess, onSaveError);
            } else {
                SpellMH.save($scope.spellMH, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
