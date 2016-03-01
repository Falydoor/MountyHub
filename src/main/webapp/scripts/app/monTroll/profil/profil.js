'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('profil', {
                parent: 'entity',
                url: '/profil',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Profil de mon Troll'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/monTroll/profil/profil.html',
                        controller: 'ProfilController'
                    }
                },
                resolve: {
                    troll: function (Profil) {
                        return Profil.get().$promise;
                    }
                }
            });
    });
