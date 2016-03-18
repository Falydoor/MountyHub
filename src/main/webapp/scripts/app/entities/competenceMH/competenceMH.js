'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('competenceMH', {
                parent: 'entity',
                url: '/competenceMHs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CompetenceMHs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/competenceMH/competenceMHs.html',
                        controller: 'CompetenceMHController'
                    }
                },
                resolve: {
                }
            })
            .state('competenceMH.detail', {
                parent: 'entity',
                url: '/competenceMH/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'CompetenceMH'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/competenceMH/competenceMH-detail.html',
                        controller: 'CompetenceMHDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CompetenceMH', function($stateParams, CompetenceMH) {
                        return CompetenceMH.get({id : $stateParams.id});
                    }]
                }
            })
            .state('competenceMH.new', {
                parent: 'competenceMH',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/competenceMH/competenceMH-dialog.html',
                        controller: 'CompetenceMHDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    name: null,
                                    pa: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('competenceMH', null, { reload: true });
                    }, function() {
                        $state.go('competenceMH');
                    })
                }]
            })
            .state('competenceMH.edit', {
                parent: 'competenceMH',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/competenceMH/competenceMH-dialog.html',
                        controller: 'CompetenceMHDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CompetenceMH', function(CompetenceMH) {
                                return CompetenceMH.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('competenceMH', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('competenceMH.delete', {
                parent: 'competenceMH',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/competenceMH/competenceMH-delete-dialog.html',
                        controller: 'CompetenceMHDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['CompetenceMH', function(CompetenceMH) {
                                return CompetenceMH.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('competenceMH', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
