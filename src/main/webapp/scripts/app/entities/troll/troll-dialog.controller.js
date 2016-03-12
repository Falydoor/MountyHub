'use strict';

angular.module('mountyhubApp').controller('TrollDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Troll', 'User', 'ScriptCall', 'Gear', 'Fly',
        function($scope, $stateParams, $uibModalInstance, entity, Troll, User, ScriptCall, Gear, Fly) {

        $scope.troll = entity;
        $scope.users = User.query();
        $scope.scriptcalls = ScriptCall.query();
        $scope.gears = Gear.query();
        $scope.flys = Fly.query();
        $scope.load = function(id) {
            Troll.get({id : id}, function(result) {
                $scope.troll = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:trollUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.troll.id != null) {
                Troll.update($scope.troll, onSaveSuccess, onSaveError);
            } else {
                Troll.save($scope.troll, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForBirthDate = {};

        $scope.datePickerForBirthDate.status = {
            opened: false
        };

        $scope.datePickerForBirthDateOpen = function($event) {
            $scope.datePickerForBirthDate.status.opened = true;
        };
        $scope.datePickerForDla = {};

        $scope.datePickerForDla.status = {
            opened: false
        };

        $scope.datePickerForDlaOpen = function($event) {
            $scope.datePickerForDla.status.opened = true;
        };
}]);
