'use strict';

angular.module('mountyhubApp').controller('SpellDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Spell', 'SpellMH', 'Troll',
        function($scope, $stateParams, $uibModalInstance, entity, Spell, SpellMH, Troll) {

        $scope.spell = entity;
        $scope.spellmhs = SpellMH.query();
        $scope.trolls = Troll.query();
        $scope.load = function(id) {
            Spell.get({id : id}, function(result) {
                $scope.spell = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:spellUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.spell.id != null) {
                Spell.update($scope.spell, onSaveSuccess, onSaveError);
            } else {
                Spell.save($scope.spell, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
