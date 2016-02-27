'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('scriptCall', {
                parent: 'entity',
                url: '/scriptCalls',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ScriptCalls'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scriptCall/scriptCalls.html',
                        controller: 'ScriptCallController'
                    }
                },
                resolve: {
                }
            })
            .state('scriptCall.detail', {
                parent: 'entity',
                url: '/scriptCall/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ScriptCall'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scriptCall/scriptCall-detail.html',
                        controller: 'ScriptCallDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ScriptCall', function($stateParams, ScriptCall) {
                        return ScriptCall.get({id : $stateParams.id});
                    }]
                }
            })
            .state('scriptCall.new', {
                parent: 'scriptCall',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scriptCall/scriptCall-dialog.html',
                        controller: 'ScriptCallDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    type: null,
                                    dateCalled: null,
                                    url: null,
                                    successful: false,
                                    body: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('scriptCall', null, { reload: true });
                    }, function() {
                        $state.go('scriptCall');
                    })
                }]
            })
            .state('scriptCall.edit', {
                parent: 'scriptCall',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scriptCall/scriptCall-dialog.html',
                        controller: 'ScriptCallDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ScriptCall', function(ScriptCall) {
                                return ScriptCall.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scriptCall', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('scriptCall.delete', {
                parent: 'scriptCall',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scriptCall/scriptCall-delete-dialog.html',
                        controller: 'ScriptCallDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ScriptCall', function(ScriptCall) {
                                return ScriptCall.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scriptCall', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
