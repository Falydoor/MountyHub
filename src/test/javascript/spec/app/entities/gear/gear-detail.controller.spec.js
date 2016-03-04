'use strict';

describe('Controller Tests', function() {

    describe('Gear Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGear, MockTroll;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGear = jasmine.createSpy('MockGear');
            MockTroll = jasmine.createSpy('MockTroll');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Gear': MockGear,
                'Troll': MockTroll
            };
            createController = function() {
                $injector.get('$controller')("GearDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:gearUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
