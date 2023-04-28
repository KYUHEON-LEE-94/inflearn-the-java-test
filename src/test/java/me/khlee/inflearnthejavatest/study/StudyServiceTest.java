package me.khlee.inflearnthejavatest.study;

import me.khlee.inflearnthejavatest.domain.Member;
import me.khlee.inflearnthejavatest.domain.Study;
import me.khlee.inflearnthejavatest.member.MemberNotFoundException;
import me.khlee.inflearnthejavatest.member.MemberService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class) //이걸 선언해줘야 @Mock을 선언해서 객체 생성이됨
@ActiveProfiles("test")
@Testcontainers
public class StudyServiceTest {

    static Logger logger = LoggerFactory.getLogger(StudyServiceTest.class);

    @Mock
    MemberService memberService;
    @Autowired
    StudyRepository studyRepository;

    @Container
    //특정 DB에 특정 설정을 하는 방법
    private static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_DB", "studytest")
            //특정 http Path를 기다리는 방법
            .waitingFor(Wait.forHttp("/hello"));

    @BeforeAll
    static  void  beforeAll(){
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
        postgreSQLContainer.followOutput(logConsumer);
    }

    //testDB에 쌓이더라도 충돌을 최대한 줄이는 방법
    @BeforeEach
    void beforeEach() {
        System.out.println("=====");
        //랜덤하게 매핑된 포트를 참조하는 방법
        System.out.println(postgreSQLContainer.getMappedPort(5432));
        System.out.println(postgreSQLContainer.getLogs());
        studyRepository.deleteAll();

    }


    @Test
    void createNewStudy(@Mock MemberService memberService,
                        @Mock StudyRepository studyRepository) throws MemberNotFoundException {


        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("@.com");

        //Mock객체로 stubbing한것
        //*1L이 아닌 다른 객체로 하면 반환이 안됨
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member)) //처음은 정상 리턴
                .thenThrow(new RuntimeException())// 두번째는 예외
                .thenReturn(Optional.empty())//세번째는 비어있는값
        ;

        Optional<Member> byId = memberService.findById(1L);
        assertEquals("@.com", byId.get().getEmail());

        assertThrows(RuntimeException.class, (() -> {
            memberService.findById(2L);
        }));

        assertEquals(Optional.empty(), memberService.findById(4L));


    }

    @Test
    void createNewStudy2(@Mock MemberService memberService,
                         @Mock StudyRepository studyRepository) throws MemberNotFoundException {
        //Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "테스트");

        Member member = new Member();
        member.setId(1L);
        member.setEmail("테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        //When
        studyService.createNewStudy(1L, study);

        //Then
        assertEquals(member, study.getOwner());
        then(memberService).should(times(1)).notify(study);
        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy(@Mock MemberService memberService,
                   @Mock StudyRepository studyRepository) {

        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바 테스트");

        given(studyRepository.save(study)).willReturn(study);

        studyService.openStudy(study);

        assertEquals(StudyStatus.OPEN, study.getStatus());
        assertNotNull(study.getOpenDateTime());
        then(memberService).should().notify(study);
    }
}
