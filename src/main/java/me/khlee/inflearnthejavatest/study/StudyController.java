package me.khlee.inflearnthejavatest.study;

import lombok.RequiredArgsConstructor;
import me.khlee.inflearnthejavatest.domain.Study;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StudyController {

    final StudyRepository studyRepository;

    @GetMapping("/study/{id}")
    public Study getSTudy(@PathVariable Long id){
        return studyRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException(("study no fount for '" + id+"'")));
    }

    @PostMapping("/study")
    public Study createStudy(@RequestBody Study study){
        return studyRepository.save(study);
    }

}
