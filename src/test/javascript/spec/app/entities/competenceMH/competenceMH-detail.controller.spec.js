'use strict';

describe('Controller Tests', function() {

    describe('CompetenceMH Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompetenceMH;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompetenceMH = jasmine.createSpy('MockCompetenceMH');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'CompetenceMH': MockCompetenceMH
            };
            createController = function() {
                $injector.get('$controller')("CompetenceMHDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:competenceMHUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
