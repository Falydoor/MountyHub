'use strict';

angular.module('mountyhubApp')
    .controller('MainController', function ($scope, Principal, AddTroll) {
        Principal.identity().then(function (account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            $scope.addTroll = function () {
                AddTroll.query();
            };
        });
    });
