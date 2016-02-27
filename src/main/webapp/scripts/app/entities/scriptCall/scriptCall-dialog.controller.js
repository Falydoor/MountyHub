'use strict';

angular.module('mountyhubApp').controller('ScriptCallDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScriptCall', 'Troll',
        function($scope, $stateParams, $uibModalInstance, entity, ScriptCall, Troll) {

        $scope.scriptCall = entity;
        $scope.trolls = Troll.query();
        $scope.load = function(id) {
            ScriptCall.get({id : id}, function(result) {
                $scope.scriptCall = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mountyhubApp:scriptCallUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.scriptCall.id != null) {
                ScriptCall.update($scope.scriptCall, onSaveSuccess, onSaveError);
            } else {
                ScriptCall.save($scope.scriptCall, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDateCalled = {};

        $scope.datePickerForDateCalled.status = {
            opened: false
        };

        $scope.datePickerForDateCalledOpen = function($event) {
            $scope.datePickerForDateCalled.status.opened = true;
        };
}]);
