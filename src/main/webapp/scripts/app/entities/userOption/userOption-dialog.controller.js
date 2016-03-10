'use strict';

angular.module('mountyhubApp').controller('UserOptionDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserOption', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, UserOption, User) {

        $scope.userOption = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            UserOption.get({id : id}, function(result) {
                $scope.userOption = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:userOptionUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.userOption.id != null) {
                UserOption.update($scope.userOption, onSaveSuccess, onSaveError);
            } else {
                UserOption.save($scope.userOption, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
