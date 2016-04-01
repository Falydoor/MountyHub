'use strict';

describe('Controller Tests', function() {

    describe('BonusMalusType Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBonusMalusType, MockBonusMalus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBonusMalusType = jasmine.createSpy('MockBonusMalusType');
            MockBonusMalus = jasmine.createSpy('MockBonusMalus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BonusMalusType': MockBonusMalusType,
                'BonusMalus': MockBonusMalus
            };
            createController = function() {
                $injector.get('$controller')("BonusMalusTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:bonusMalusTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
