'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('troll', {
                parent: 'entity',
                url: '/trolls',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Trolls'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/troll/trolls.html',
                        controller: 'TrollController'
                    }
                },
                resolve: {
                }
            })
            .state('troll.detail', {
                parent: 'entity',
                url: '/troll/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Troll'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/troll/troll-detail.html',
                        controller: 'TrollDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Troll', function($stateParams, Troll) {
                        return Troll.get({id : $stateParams.id});
                    }]
                }
            })
            .state('troll.new', {
                parent: 'troll',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/troll/troll-dialog.html',
                        controller: 'TrollDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                                    level: null,
                                    kill: null,
                                    death: null,
                                    restrictedPassword: null,
                                    deleted: false,
                                    hidden: false,
                                    invisible: false,
                                    intangible: false,
                                    strain: null,
                                    pa: null,
                                    dla: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('troll', null, { reload: true });
                    }, function() {
                        $state.go('troll');
                    })
                }]
            })
            .state('troll.edit', {
                parent: 'troll',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/troll/troll-dialog.html',
                        controller: 'TrollDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Troll', function(Troll) {
                                return Troll.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('troll', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('troll.delete', {
                parent: 'troll',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/troll/troll-delete-dialog.html',
                        controller: 'TrollDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Troll', function(Troll) {
                                return Troll.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('troll', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
