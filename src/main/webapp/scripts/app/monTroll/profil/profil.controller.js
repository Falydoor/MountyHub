'use strict';

angular.module('mountyhubApp')
    .controller('ProfilController', function ($scope, Profil, troll) {
        $scope.troll = troll;
        $scope.caracs = [
            {n: "Attaque", p: "attack", d: "D6"},
            {n: "Esquive", p: "dodge", d: "D6"},
            {n: "Dégâts", p: "damage", d: "D3"},
            {n: "Point de Vie", p: "hitPoint", d: " PV"},
            {n: "Régénération", p: "regeneration", d: "D3"},
            {n: "Armure", p: "armor", d: "D3"},
            {n: "Vue", p: "view", d: "cases"},
            {n: "Résistance à la Magie", p: "rm", d: "points"},
            {n: "Maîtrise de la Magie", p: "mm", d: "points"}
        ];

        $scope.addTroll = function () {
            Profil.save({number: $scope.troll.number, restrictedPassword: $scope.troll.restrictedPassword});
        };

        $scope.addSign = function (v) {
            return v >= 0 ? '+' + v : v;
        };

        $scope.addContext = function (v) {
            if (v > 0) {
                return ' success';
            } else if (v < 0) {
                return 'danger';
            }
        };
    });
