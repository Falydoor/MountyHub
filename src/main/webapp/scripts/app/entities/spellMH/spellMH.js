'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('spellMH', {
                parent: 'entity',
                url: '/spellMHs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SpellMHs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/spellMH/spellMHs.html',
                        controller: 'SpellMHController'
                    }
                },
                resolve: {
                }
            })
            .state('spellMH.detail', {
                parent: 'entity',
                url: '/spellMH/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SpellMH'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/spellMH/spellMH-detail.html',
                        controller: 'SpellMHDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SpellMH', function($stateParams, SpellMH) {
                        return SpellMH.get({id : $stateParams.id});
                    }]
                }
            })
            .state('spellMH.new', {
                parent: 'spellMH',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/spellMH/spellMH-dialog.html',
                        controller: 'SpellMHDialogController',
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
                        $state.go('spellMH', null, { reload: true });
                    }, function() {
                        $state.go('spellMH');
                    })
                }]
            })
            .state('spellMH.edit', {
                parent: 'spellMH',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/spellMH/spellMH-dialog.html',
                        controller: 'SpellMHDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SpellMH', function(SpellMH) {
                                return SpellMH.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('spellMH', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('spellMH.delete', {
                parent: 'spellMH',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/spellMH/spellMH-delete-dialog.html',
                        controller: 'SpellMHDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SpellMH', function(SpellMH) {
                                return SpellMH.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('spellMH', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
