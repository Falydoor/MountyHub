'use strict';

angular.module('mountyhubApp')
    .controller('TrollController', function ($scope, $state, Troll, ParseLinks) {

        $scope.trolls = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Troll.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.trolls = result;
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
            $scope.troll = {
                number: null,
                name: null,
                race: null,
                birthDate: null,
                x: null,
                y: null,
                z: null,
                attack: null,
                dodge: null,
                damage: null,
                regeneration: null,
                hitPoint: null,
                currentHitPoint: null,
                view: null,
                rm: null,
                mm: null,
                armor: null,
                turn: null,
                weight: null,
                focus: null,
                attackP: null,
                dodgeP: null,
                damageP: null,
                regenerationP: null,
                hitPointP: null,
                attackM: null,
                dodgeM: null,
                damageM: null,
                regenerationM: null,
                hitPointM: null,
                viewP: null,
                rmP: null,
                mmP: null,
                armorP: null,
                weightP: null,
                viewM: null,
                rmM: null,
                mmM: null,
                armorM: null,
                weightM: null,
                id: null
            };
        };
    });
