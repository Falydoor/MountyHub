'use strict';

describe('Controller Tests', function() {

    describe('Troll Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTroll, MockUser, MockScriptCall, MockGear;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTroll = jasmine.createSpy('MockTroll');
            MockUser = jasmine.createSpy('MockUser');
            MockScriptCall = jasmine.createSpy('MockScriptCall');
            MockGear = jasmine.createSpy('MockGear');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Troll': MockTroll,
                'User': MockUser,
                'ScriptCall': MockScriptCall,
                'Gear': MockGear
            };
            createController = function() {
                $injector.get('$controller')("TrollDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:trollUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
