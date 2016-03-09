'use strict';

angular.module('mountyhubApp')
    .controller('MonProfilController', function ($scope, MonProfil, troll) {
        $scope.troll = troll;
        var total = function (v, vp, vm) {
            return v + vp + vm;
        };
        var diceThreeAverage = function (v, vp, vm) {
            return v * 2 + vp + vm;
        };

        var diceSixAverage = function (v, vp, vm) {
            return v * 3.5 + vp + vm;
        };
        // n: Name to display, p: Name of the property, d: Dice, a: Average method, s: Suffix
        $scope.caracs = [
            {n: "Attaque", p: "attack", d: " D6", a: diceSixAverage},
            {n: "Esquive", p: "dodge", d: " D6", a: diceSixAverage},
            {n: "Dégâts", p: "damage", d: " D3", a: diceThreeAverage},
            {n: "Point de Vie", p: "hitPoint", d: " PV", a: total, s: true},
            {n: "Régénération", p: "regeneration", d: " D3", a: diceThreeAverage},
            {n: "Armure", p: "armor", d: " D3", a: diceThreeAverage},
            {n: "Vue", p: "view", d: " cases", a: total, s: true},
            {n: "Résistance à la Magie", p: "rm", d: " points", a: total, s: true},
            {n: "Maîtrise de la Magie", p: "mm", d: " points", a: total, s: true}
        ];

        var callbackSuccess = function (troll) {
            $scope.troll = troll;
        };

        $scope.addTroll = function () {
            MonProfil.save({
                number: $scope.troll.number,
                restrictedPassword: $scope.troll.restrictedPassword
            }, callbackSuccess);
        };

        $scope.deleteTroll = function () {
            MonProfil.delete({}, callbackSuccess);
        };

        $scope.addSign = function (v) {
            return v >= 0 ? '+' + v : v;
        };

        $scope.addContext = function (v) {
            if (v > 0) {
                return ' success';
            } else if (v < 0) {
                return ' danger';
            }
        };

        $scope.refreshProfil = function (refreshType) {
            MonProfil.refresh(refreshType, callbackSuccess);
        };

    });
