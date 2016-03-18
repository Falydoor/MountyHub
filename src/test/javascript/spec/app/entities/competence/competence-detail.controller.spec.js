'use strict';

describe('Controller Tests', function() {

    describe('Competence Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCompetence, MockCompetenceMH, MockTroll;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCompetence = jasmine.createSpy('MockCompetence');
            MockCompetenceMH = jasmine.createSpy('MockCompetenceMH');
            MockTroll = jasmine.createSpy('MockTroll');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Competence': MockCompetence,
                'CompetenceMH': MockCompetenceMH,
                'Troll': MockTroll
            };
            createController = function() {
                $injector.get('$controller')("CompetenceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mountyhubApp:competenceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
