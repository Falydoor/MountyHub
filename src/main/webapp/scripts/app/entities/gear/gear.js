'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('gear', {
                parent: 'entity',
                url: '/gears',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Gears'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gear/gears.html',
                        controller: 'GearController'
                    }
                },
                resolve: {
                }
            })
            .state('gear.detail', {
                parent: 'entity',
                url: '/gear/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Gear'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gear/gear-detail.html',
                        controller: 'GearDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Gear', function($stateParams, Gear) {
                        return Gear.get({id : $stateParams.id});
                    }]
                }
            })
            .state('gear.new', {
                parent: 'gear',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/gear/gear-dialog.html',
                        controller: 'GearDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    weared: false,
                                    type: null,
                                    identified: false,
                                    name: null,
                                    template: null,
                                    description: null,
                                    weight: null,
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
                        $state.go('gear', null, { reload: true });
                    }, function() {
                        $state.go('gear');
                    })
                }]
            })
            .state('gear.edit', {
                parent: 'gear',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/gear/gear-dialog.html',
                        controller: 'GearDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Gear', function(Gear) {
                                return Gear.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('gear', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('gear.delete', {
                parent: 'gear',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/gear/gear-delete-dialog.html',
                        controller: 'GearDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Gear', function(Gear) {
                                return Gear.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('gear', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
