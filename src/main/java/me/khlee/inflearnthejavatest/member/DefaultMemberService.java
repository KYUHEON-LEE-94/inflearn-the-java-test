package me.khlee.inflearnthejavatest.member;

import me.khlee.inflearnthejavatest.domain.Member;
import me.khlee.inflearnthejavatest.domain.Study;
import me.khlee.inflearnthejavatest.study.StudyService;

import java.util.Optional;

public class DefaultMemberService implements MemberService{

    StudyService studyService;

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.empty();
    }

    @Override
    public void validate(Long memberId) {
        studyService.notify();
    }

    @Override
    public void notify(Study newStudy) {
        studyService.notify();
    }

    @Override
    public void notify(Member member) {

    }
}
