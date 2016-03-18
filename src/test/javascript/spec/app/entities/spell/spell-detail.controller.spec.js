'use strict';

describe('Controller Tests', function() {

    describe('Spell Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSpell, MockSpellMH, MockTroll;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSpell = jasmine.createSpy('MockSpell');
            MockSpellMH = jasmine.createSpy('MockSpellMH');
            MockTroll = jasmine.createSpy('MockTroll');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Spell': MockSpell,
                'SpellMH': MockSpellMH,
                'Troll': MockTroll
            };
            createController = function() {
                $injector.get('$controller')("SpellDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:spellUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
