'use strict';

angular.module('mountyhubApp').controller('FlyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Fly', 'Troll',
        function($scope, $stateParams, $uibModalInstance, entity, Fly, Troll) {

        $scope.fly = entity;
        $scope.trolls = Troll.query();
        $scope.load = function(id) {
            Fly.get({id : id}, function(result) {
                $scope.fly = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:flyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.fly.id != null) {
                Fly.update($scope.fly, onSaveSuccess, onSaveError);
            } else {
                Fly.save($scope.fly, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
