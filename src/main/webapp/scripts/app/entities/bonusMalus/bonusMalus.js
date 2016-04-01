'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bonusMalus', {
                parent: 'entity',
                url: '/bonusMaluss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BonusMaluss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bonusMalus/bonusMaluss.html',
                        controller: 'BonusMalusController'
                    }
                },
                resolve: {
                }
            })
            .state('bonusMalus.detail', {
                parent: 'entity',
                url: '/bonusMalus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BonusMalus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bonusMalus/bonusMalus-detail.html',
                        controller: 'BonusMalusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BonusMalus', function($stateParams, BonusMalus) {
                        return BonusMalus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('bonusMalus.new', {
                parent: 'bonusMalus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bonusMalus/bonusMalus-dialog.html',
                        controller: 'BonusMalusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    effect: null,
                                    realEffect: null,
                                    duration: null,
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
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('bonusMalus', null, { reload: true });
                    }, function() {
                        $state.go('bonusMalus');
                    })
                }]
            })
            .state('bonusMalus.edit', {
                parent: 'bonusMalus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bonusMalus/bonusMalus-dialog.html',
                        controller: 'BonusMalusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BonusMalus', function(BonusMalus) {
                                return BonusMalus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bonusMalus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('bonusMalus.delete', {
                parent: 'bonusMalus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bonusMalus/bonusMalus-delete-dialog.html',
                        controller: 'BonusMalusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BonusMalus', function(BonusMalus) {
                                return BonusMalus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bonusMalus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
