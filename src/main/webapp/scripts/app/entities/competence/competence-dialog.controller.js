'use strict';

angular.module('mountyhubApp').controller('CompetenceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Competence', 'CompetenceMH', 'Troll',
        function($scope, $stateParams, $uibModalInstance, $q, entity, Competence, CompetenceMH, Troll) {

        $scope.competence = entity;
        $scope.competencemhs = CompetenceMH.query({filter: 'competence-is-null'});
        $q.all([$scope.competence.$promise, $scope.competencemhs.$promise]).then(function() {
            if (!$scope.competence.competenceMH || !$scope.competence.competenceMH.id) {
                return $q.reject();
            }
            return CompetenceMH.get({id : $scope.competence.competenceMH.id}).$promise;
        }).then(function(competenceMH) {
            $scope.competencemhs.push(competenceMH);
        });
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
