'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('spell', {
                parent: 'entity',
                url: '/spells',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Spells'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/spell/spells.html',
                        controller: 'SpellController'
                    }
                },
                resolve: {
                }
            })
            .state('spell.detail', {
                parent: 'entity',
                url: '/spell/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Spell'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/spell/spell-detail.html',
                        controller: 'SpellDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Spell', function($stateParams, Spell) {
                        return Spell.get({id : $stateParams.id});
                    }]
                }
            })
            .state('spell.new', {
                parent: 'spell',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/spell/spell-dialog.html',
                        controller: 'SpellDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    percent: null,
                                    percentBonus: null,
                                    level: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('spell', null, { reload: true });
                    }, function() {
                        $state.go('spell');
                    })
                }]
            })
            .state('spell.edit', {
                parent: 'spell',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/spell/spell-dialog.html',
                        controller: 'SpellDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Spell', function(Spell) {
                                return Spell.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('spell', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('spell.delete', {
                parent: 'spell',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/spell/spell-delete-dialog.html',
                        controller: 'SpellDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Spell', function(Spell) {
                                return Spell.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('spell', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
