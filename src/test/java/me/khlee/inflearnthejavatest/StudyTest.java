package me.khlee.inflearnthejavatest;

import me.khlee.inflearnthejavatest.domain.Study;
import me.khlee.inflearnthejavatest.study.StudyService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

//@ExtendWith(FindSlowTestExtension.class) //확장하는 방법1
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {

    int value = 1;

    //확장하는 방법2
    @RegisterExtension
    static FindSlowTestExtension findSlowTestExtension = new FindSlowTestExtension(1000L);

    @Order(2)
    @FastTest
    @DisplayName("스터디 만들기 😊")
    void create_new_Study() {
        System.out.println(this);
        System.out.println("createFirst:  " + value++);
        Study actual = new Study(1);
        assertThat(actual.getLimit()).isGreaterThan(0);

    }

    @Order(1)
//    @SlowTest
    @Test
    void create_new_study_again() throws InterruptedException {
        Thread.sleep(1005L);
        System.out.println(this);
        System.out.println("createTwo: " + value++);
    }

    @Order(3)
    @DisplayName("스터디 또만들어")
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition}/{totalRepetition}")
    void repeat(RepetitionInfo repetitionInfo){
        System.out.println("test"+ repetitionInfo.getCurrentRepetition() +"/" + repetitionInfo.getTotalRepetitions());
    }

    @Order(4)
    @DisplayName("스터디 만들게")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    void paramterrizedTest(@AggregateWith(StudyAggregator.class) StudyService study){
        System.out.println(study);
    }

    static class StudyAggregator implements ArgumentsAggregator{
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            Study study = new Study(accessor.getInteger(0), accessor.getString(1));
            return study;
        }
    }

    static  class  StudyConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
            assertEquals(StudyService.class,targetType, "Can only convert to Study");
            return new Study(Integer.parseInt(source.toString()));
        }
    }

    //TODO @testInstance를 하게되면 static이 아니어도 됨
    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("beforeEach");
    }

    @AfterEach
    void afterEach() {
        System.out.println("afterEach");
    }

}