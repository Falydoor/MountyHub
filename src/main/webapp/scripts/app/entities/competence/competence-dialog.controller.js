'use strict';

angular.module('mountyhubApp').controller('CompetenceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Competence', 'CompetenceMH', 'Troll',
        function($scope, $stateParams, $uibModalInstance, entity, Competence, CompetenceMH, Troll) {

        $scope.competence = entity;
        $scope.competencemhs = CompetenceMH.query();
        $scope.trolls = Troll.query();
        $scope.load = function(id) {
            Competence.get({id : id}, function(result) {
                $scope.competence = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:competenceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.competence.id != null) {
                Competence.update($scope.competence, onSaveSuccess, onSaveError);
            } else {
                Competence.save($scope.competence, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
