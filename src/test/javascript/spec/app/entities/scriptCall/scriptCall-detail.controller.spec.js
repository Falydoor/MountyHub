'use strict';

describe('Controller Tests', function() {

    describe('ScriptCall Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScriptCall, MockTroll;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScriptCall = jasmine.createSpy('MockScriptCall');
            MockTroll = jasmine.createSpy('MockTroll');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ScriptCall': MockScriptCall,
                'Troll': MockTroll
            };
            createController = function() {
                $injector.get('$controller')("ScriptCallDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:scriptCallUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
