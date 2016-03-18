'use strict';

describe('Controller Tests', function() {

    describe('SpellMH Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSpellMH;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSpellMH = jasmine.createSpy('MockSpellMH');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'SpellMH': MockSpellMH
            };
            createController = function() {
                $injector.get('$controller')("SpellMHDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:spellMHUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
