'use strict';

angular.module('mountyhubApp')
    .controller('GearController', function ($scope, $state, Gear, ParseLinks) {

        $scope.gears = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Gear.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.gears = result;
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
            $scope.gear = {
                number: null,
                weared: false,
                type: null,
                identified: false,
                name: null,
                template: null,
                description: null,
                weight: null,
                attack: null,
                attackM: null,
                dodge: null,
                dodgeM: null,
                damage: null,
                damageM: null,
                regeneration: null,
                hitPoint: null,
                view: null,
                rm: null,
                mm: null,
                armor: null,
                armorM: null,
                turn: null,
                protection: null,
                id: null
            };
        };
    });
