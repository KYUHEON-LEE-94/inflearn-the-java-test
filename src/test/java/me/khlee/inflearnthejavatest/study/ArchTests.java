package me.khlee.inflearnthejavatest.study;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchRules;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.Slices;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import me.khlee.inflearnthejavatest.InflearnTheJavaTestApplication;
import org.assertj.core.internal.Classes;
import org.junit.jupiter.api.Test;
import com.tngtech.archunit.core.domain.JavaClasses;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packagesOf = InflearnTheJavaTestApplication.class)
public class ArchTests {

    /**
     * 1. ..domain.. 패키지에 있는 클래스는 ..study.., ..member.., ..domain에서 참조 가능.
     * 2. ..member.. 패키지에 있는 클래스는 ..study..와 ..member..에서만 참조 가능.
     * 3. (반대로) ..domain.. 패키지는 ..member.. 패키지를 참조하지 못한다.
     * ● ..study.. 패키지에 있는 클래스는 ..study.. 에서만 참조 가능.
     * ● 순환 참조 없어야 한다.
     */

    @ArchTest
    //1번
    ArchRule domainPackageRule = classes().that().resideInAPackage("..domain..")
                .should().onlyBeAccessed().byClassesThat()
                .resideInAnyPackage("..study", "..member..", "..domain");

    @ArchTest
    //2번
    ArchRule memberPackageRule = noClasses().that().resideInAPackage("..domain")
                .should().accessClassesThat().resideInAnyPackage("..member..");

    @ArchTest
    //3번
    ArchRule studyPackageRule = noClasses().that().resideOutsideOfPackage("..study")
                .should().accessClassesThat().resideInAPackage("..study");

    @ArchTest
    //4번
    ArchRule freeOfCycles = SlicesRuleDefinition.slices().matching("..inflearnthejavatest.(*)..")
            .should().beFreeOfCycles();
}
