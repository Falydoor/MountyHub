'use strict';

angular.module('mountyhubApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('competence', {
                parent: 'entity',
                url: '/competences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Competences'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/competence/competences.html',
                        controller: 'CompetenceController'
                    }
                },
                resolve: {
                }
            })
            .state('competence.detail', {
                parent: 'entity',
                url: '/competence/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Competence'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/competence/competence-detail.html',
                        controller: 'CompetenceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Competence', function($stateParams, Competence) {
                        return Competence.get({id : $stateParams.id});
                    }]
                }
            })
            .state('competence.new', {
                parent: 'competence',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/competence/competence-dialog.html',
                        controller: 'CompetenceDialogController',
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
                        $state.go('competence', null, { reload: true });
                    }, function() {
                        $state.go('competence');
                    })
                }]
            })
            .state('competence.edit', {
                parent: 'competence',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/competence/competence-dialog.html',
                        controller: 'CompetenceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Competence', function(Competence) {
                                return Competence.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('competence', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('competence.delete', {
                parent: 'competence',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/competence/competence-delete-dialog.html',
                        controller: 'CompetenceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Competence', function(Competence) {
                                return Competence.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('competence', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
