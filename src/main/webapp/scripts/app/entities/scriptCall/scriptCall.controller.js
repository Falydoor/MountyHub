'use strict';

angular.module('mountyhubApp')
    .controller('ScriptCallController', function ($scope, $state, ScriptCall, ParseLinks) {

        $scope.scriptCalls = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            ScriptCall.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.scriptCalls = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.scriptCall = {
                name: null,
                type: null,
                dateCalled: null,
                url: null,
                successful: false,
                id: null
            };
        };
    });
