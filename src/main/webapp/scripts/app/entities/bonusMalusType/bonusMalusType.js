'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bonusMalusType', {
                parent: 'entity',
                url: '/bonusMalusTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BonusMalusTypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bonusMalusType/bonusMalusTypes.html',
                        controller: 'BonusMalusTypeController'
                    }
                },
                resolve: {
                }
            })
            .state('bonusMalusType.detail', {
                parent: 'entity',
                url: '/bonusMalusType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BonusMalusType'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bonusMalusType/bonusMalusType-detail.html',
                        controller: 'BonusMalusTypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BonusMalusType', function($stateParams, BonusMalusType) {
                        return BonusMalusType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('bonusMalusType.new', {
                parent: 'bonusMalusType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bonusMalusType/bonusMalusType-dialog.html',
                        controller: 'BonusMalusTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('bonusMalusType', null, { reload: true });
                    }, function() {
                        $state.go('bonusMalusType');
                    })
                }]
            })
            .state('bonusMalusType.edit', {
                parent: 'bonusMalusType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bonusMalusType/bonusMalusType-dialog.html',
                        controller: 'BonusMalusTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BonusMalusType', function(BonusMalusType) {
                                return BonusMalusType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bonusMalusType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('bonusMalusType.delete', {
                parent: 'bonusMalusType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/bonusMalusType/bonusMalusType-delete-dialog.html',
                        controller: 'BonusMalusTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BonusMalusType', function(BonusMalusType) {
                                return BonusMalusType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('bonusMalusType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
