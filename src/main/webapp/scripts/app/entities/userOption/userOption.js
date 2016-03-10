'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userOption', {
                parent: 'entity',
                url: '/userOptions',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserOptions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userOption/userOptions.html',
                        controller: 'UserOptionController'
                    }
                },
                resolve: {
                }
            })
            .state('userOption.detail', {
                parent: 'entity',
                url: '/userOption/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'UserOption'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userOption/userOption-detail.html',
                        controller: 'UserOptionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserOption', function($stateParams, UserOption) {
                        return UserOption.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userOption.new', {
                parent: 'userOption',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userOption/userOption-dialog.html',
                        controller: 'UserOptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    value: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userOption', null, { reload: true });
                    }, function() {
                        $state.go('userOption');
                    })
                }]
            })
            .state('userOption.edit', {
                parent: 'userOption',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userOption/userOption-dialog.html',
                        controller: 'UserOptionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserOption', function(UserOption) {
                                return UserOption.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userOption', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('userOption.delete', {
                parent: 'userOption',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/userOption/userOption-delete-dialog.html',
                        controller: 'UserOptionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['UserOption', function(UserOption) {
                                return UserOption.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userOption', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
