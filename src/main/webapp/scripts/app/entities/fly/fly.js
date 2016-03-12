'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('fly', {
                parent: 'entity',
                url: '/flys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Flys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fly/flys.html',
                        controller: 'FlyController'
                    }
                },
                resolve: {
                }
            })
            .state('fly.detail', {
                parent: 'entity',
                url: '/fly/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Fly'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/fly/fly-detail.html',
                        controller: 'FlyDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Fly', function($stateParams, Fly) {
                        return Fly.get({id : $stateParams.id});
                    }]
                }
            })
            .state('fly.new', {
                parent: 'fly',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/fly/fly-dialog.html',
                        controller: 'FlyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    number: null,
                                    old: null,
                                    here: false,
                                    type: null,
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('fly', null, { reload: true });
                    }, function() {
                        $state.go('fly');
                    })
                }]
            })
            .state('fly.edit', {
                parent: 'fly',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/fly/fly-dialog.html',
                        controller: 'FlyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Fly', function(Fly) {
                                return Fly.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fly', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('fly.delete', {
                parent: 'fly',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/fly/fly-delete-dialog.html',
                        controller: 'FlyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Fly', function(Fly) {
                                return Fly.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('fly', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
