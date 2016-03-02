'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('monProfil', {
                parent: 'entity',
                url: '/monProfil',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Profil de mon Troll'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/monTroll/monProfil/monProfil.html',
                        controller: 'MonProfilController'
                    }
                },
                resolve: {
                    troll: function (MonProfil) {
                        return MonProfil.get().$promise;
                    }
                }
            });
    });
