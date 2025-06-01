package com.nawiedk.profile.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchUnitTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.nawiedk.profile");
    }

    @Test
    void domainLayerShouldOnlyDependOnJdkAndDomainItselfAndValidation() {
        ArchRule rule = classes()
                .that().resideInAPackage("..domain..")
                .should().onlyAccessClassesThat()
                .resideInAnyPackage("..domain..", "com.nawiedk.profile.validation..", "java..", "jakarta.validation..");
        rule.check(importedClasses);
    }

    @Test
    void applicationServicesShouldResideInApplicationServicePackage() {
        ArchRule rule = classes()
                .that().areAnnotatedWith(Service.class)
                .or().haveNameMatching(".*Service")
                .should().resideInAPackage("..application.service..");
        rule.check(importedClasses);
    }

    @Test
    void applicationServicesShouldOnlyBeAccessedByControllersOrOtherApplicationServices() {
        ArchRule rule = classes()
                .that().resideInAPackage("..application.service..")
                .should().onlyBeAccessed().byAnyPackage(
                        "..application.service..",
                        "..adapter.in.web.."
                );
        rule.check(importedClasses);
    }


    @Test
    void controllersShouldResideInAdapterInWebPackage() {
        ArchRule rule = classes()
                .that().areAnnotatedWith(RestController.class)
                .should().resideInAPackage("..adapter.in.web");
        rule.check(importedClasses);
    }

    @Test
    void controllersShouldOnlyDependOnAllowedPackages() {
        ArchRule rule = classes()
                .that().resideInAPackage("..adapter.in.web..")
                .and().areAnnotatedWith(RestController.class)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "..adapter.in.web..",
                        "..application.port.in..",
                        "com.nawiedk.profile.domain..",
                        "com.nawiedk.profile.validation..",
                        "java..",
                        "org.springframework.http..",
                        "org.springframework.web..",
                        "jakarta.validation..",
                        "lombok.."
                );
        rule.check(importedClasses);
    }

    @Test
    void persistenceAdaptersShouldResideInAdapterOutPersistencePackage() {
        ArchRule rule = classes()
                .that().haveNameMatching(".*PersistenceAdapter")
                .should().resideInAPackage("..adapter.out.persistence.adapterimpl..");
        rule.check(importedClasses);
    }

    @Test
    void persistenceLayerShouldOnlyBeAccessedByApplicationServicesOrOtherPersistenceComponents() {
        ArchRule rule = classes()
                .that().resideInAPackage("..adapter.out.persistence..")
                .should().onlyBeAccessed().byAnyPackage(
                        "..application.service..",
                        "..adapter.out.persistence.."
                );
        rule.check(importedClasses);
    }


    @Test
    void layeredArchitectureShouldBeRespected() {
        ArchRule rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("AdapterInWeb").definedBy("..adapter.in.web..")
                .layer("AdapterOutPersistence").definedBy("..adapter.out.persistence..")
                .layer("Application").definedBy("..application..")
                .layer("Domain").definedBy("..domain..")
                .layer("Validation").definedBy("..validation..")

                .withOptionalLayers(true)

                .ignoreDependency(JavaClass.Predicates.resideInAnyPackage("com.nawiedk.profile.."),
                        JavaClass.Predicates.resideInAnyPackage("java..", "org.slf4j.."))
                .ignoreDependency(JavaClass.Predicates.resideInAnyPackage("com.nawiedk.profile.."),
                        JavaClass.Predicates.resideInAnyPackage("org.springframework.."))
                .ignoreDependency(JavaClass.Predicates.resideInAnyPackage("com.nawiedk.profile.."),
                        JavaClass.Predicates.resideInAnyPackage("jakarta.."))
                .ignoreDependency(JavaClass.Predicates.resideInAnyPackage("com.nawiedk.profile.."),
                        JavaClass.Predicates.resideInAnyPackage("lombok.."))


                .whereLayer("AdapterInWeb").mayNotBeAccessedByAnyLayer()
                .whereLayer("AdapterInWeb").mayOnlyAccessLayers("Application", "Domain", "Validation")

                .whereLayer("AdapterOutPersistence").mayOnlyBeAccessedByLayers("Application")
                .whereLayer("AdapterOutPersistence").mayOnlyAccessLayers("Domain", "Validation", "Application")

                .whereLayer("Application").mayOnlyBeAccessedByLayers("AdapterInWeb", "AdapterOutPersistence")
                .whereLayer("Application").mayOnlyAccessLayers("Domain", "Validation", "AdapterOutPersistence")

                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "AdapterInWeb", "AdapterOutPersistence")
                .whereLayer("Domain").mayOnlyAccessLayers("Validation")

                .whereLayer("Validation").mayOnlyBeAccessedByLayers("Domain", "Application", "AdapterInWeb", "AdapterOutPersistence");

        rule.check(importedClasses);
    }

    @Test
    void noClassesShouldUseFieldInjection() {
        ArchRule rule = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
        rule.check(importedClasses);
    }
}
