'use strict';

describe('Controller Tests', function() {

    describe('BonusMalus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBonusMalus, MockTroll;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBonusMalus = jasmine.createSpy('MockBonusMalus');
            MockTroll = jasmine.createSpy('MockTroll');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BonusMalus': MockBonusMalus,
                'Troll': MockTroll
            };
            createController = function() {
                $injector.get('$controller')("BonusMalusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:bonusMalusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
