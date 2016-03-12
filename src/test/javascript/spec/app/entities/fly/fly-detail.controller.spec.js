'use strict';

describe('Controller Tests', function() {

    describe('Fly Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFly, MockTroll;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFly = jasmine.createSpy('MockFly');
            MockTroll = jasmine.createSpy('MockTroll');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Fly': MockFly,
                'Troll': MockTroll
            };
            createController = function() {
                $injector.get('$controller')("FlyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:flyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
