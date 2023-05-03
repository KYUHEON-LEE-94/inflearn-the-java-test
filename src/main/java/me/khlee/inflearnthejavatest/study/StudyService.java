package me.khlee.inflearnthejavatest.study;

import me.khlee.inflearnthejavatest.domain.Member;
import me.khlee.inflearnthejavatest.domain.Study;
import me.khlee.inflearnthejavatest.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;

    private final StudyRepository repository;


    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null;
        assert  repository != null;
        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Optional<Member> member = memberService.findById(memberId);
        study.setOwner(member.orElseThrow(()-> new IllegalArgumentException("Member doesn't exist for id: ")));
        Study newStudy = repository.save(study);
        //알림을 준다는걸로 가정
        memberService.notify(newStudy);
       // memberService.notify(member.get());
        return newStudy;
    }

    public Study openStudy(Study study){
        study.open();
        Study openStudy = repository.save(study);
        memberService.notify(openStudy);
        return  openStudy;


    }
}
