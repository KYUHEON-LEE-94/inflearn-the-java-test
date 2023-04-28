package me.khlee.inflearnthejavatest.member;

import me.khlee.inflearnthejavatest.domain.Member;
import me.khlee.inflearnthejavatest.domain.Study;

import java.util.Optional;

public interface MemberService {

    Optional <Member> findById(Long memberId) throws  MemberNotFoundException;

    void validate(Long memberId);

    void notify(Study newStudy);

    void notify(Member member);
}
