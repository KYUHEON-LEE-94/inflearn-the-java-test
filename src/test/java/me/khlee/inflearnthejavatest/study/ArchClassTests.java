package me.khlee.inflearnthejavatest.study;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;
import me.khlee.inflearnthejavatest.InflearnTheJavaTestApplication;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packagesOf = InflearnTheJavaTestApplication.class)
public class ArchClassTests {

    public class  ArchTests{
        @ArchTest
        ArchRule controllerClassRule = classes().that().haveSimpleNameEndingWith("Controller")
                .should().accessClassesThat().haveSimpleNameEndingWith("service")
                .orShould().accessClassesThat().haveSimpleNameEndingWith("Repository");


        @ArchTest
        ArchRule repositoryClassRule = noClasses().that().haveSimpleNameEndingWith("Repository")
                .should().accessClassesThat().haveSimpleNameEndingWith("Service");

        @ArchTest
        ArchRule studyClassesRule = classes().that().haveSimpleNameStartingWith("Study") //study로 시작하는데.
                .and().areNotEnums() // Enu,이 아니고
                .and().areNotAnnotatedWith(Entity.class) //@Entity를 사용하지 않고
                .should().resideInAPackage("..study.."); // study 패키지 안에 있어야한다.
    }
}
