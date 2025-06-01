package com.nawiedk.profile.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class HexagonalArchitectureTest {

    private static final String BASE_PACKAGE = "com.nawiedk.profile";
    private static final String DOMAIN_PACKAGE = BASE_PACKAGE + ".domain..";
    private static final String APPLICATION_PACKAGE = BASE_PACKAGE + ".application..";
    private static final String ADAPTER_IN_PACKAGE = BASE_PACKAGE + ".adapter.in..";
    private static final String ADAPTER_OUT_PACKAGE = BASE_PACKAGE + ".adapter.out..";
    private static final String CONFIG_PACKAGE = BASE_PACKAGE + ".config..";

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(BASE_PACKAGE);
    }

    @Test
    void domainLayerShouldNotDependOnOtherLayers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage(DOMAIN_PACKAGE)
                .should().dependOnClassesThat().resideInAnyPackage(
                        APPLICATION_PACKAGE,
                        ADAPTER_IN_PACKAGE,
                        ADAPTER_OUT_PACKAGE,
                        CONFIG_PACKAGE
                );

        rule.check(importedClasses);
    }

    @Test
    void applicationLayerShouldOnlyDependOnDomainLayer() {
        ArchRule rule = classes()
                .that().resideInAPackage(APPLICATION_PACKAGE)
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        APPLICATION_PACKAGE,
                        DOMAIN_PACKAGE,
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.springframework..",
                        "lombok.."
                );

        rule.check(importedClasses);
    }

    @Test
    void adaptersShouldNotDependOnEachOther() {
        ArchRule inboundAdaptersRule = noClasses()
                .that().resideInAPackage(ADAPTER_IN_PACKAGE)
                .should().dependOnClassesThat().resideInAPackage(ADAPTER_OUT_PACKAGE);

        ArchRule outboundAdaptersRule = noClasses()
                .that().resideInAPackage(ADAPTER_OUT_PACKAGE)
                .should().dependOnClassesThat().resideInAPackage(ADAPTER_IN_PACKAGE);

        inboundAdaptersRule.check(importedClasses);
        outboundAdaptersRule.check(importedClasses);
    }

    @Test
    void portsShouldOnlyBeInterfaces() {
        ArchRule inboundPortsRule = classes()
                .that().resideInAPackage(APPLICATION_PACKAGE + "port.in..")
                .and().haveSimpleNameEndingWith("Port")
                .should().beInterfaces()
                .allowEmptyShould(true);

        ArchRule outboundPortsRule = classes()
                .that().resideInAPackage(APPLICATION_PACKAGE + "port.out..")
                .and().haveSimpleNameEndingWith("Port")
                .should().beInterfaces()
                .allowEmptyShould(true);

        inboundPortsRule.check(importedClasses);
        outboundPortsRule.check(importedClasses);
    }

    @Test
    void layeredArchitectureShouldBeRespected() {
        ArchRule rule = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Domain").definedBy(DOMAIN_PACKAGE)
                .layer("Application").definedBy(APPLICATION_PACKAGE)
                .layer("Adapter-In").definedBy(ADAPTER_IN_PACKAGE)
                .layer("Adapter-Out").definedBy(ADAPTER_OUT_PACKAGE)
                .layer("Config").definedBy(CONFIG_PACKAGE)

                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter-In", "Adapter-Out")
                .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter-In", "Adapter-Out", "Config")
                .whereLayer("Adapter-In").mayNotBeAccessedByAnyLayer()
                .whereLayer("Adapter-Out").mayNotBeAccessedByAnyLayer()
                .whereLayer("Config").mayNotBeAccessedByAnyLayer();

        rule.check(importedClasses);
    }

    @Test
    void inboundAdaptersShouldOnlyUseInboundPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage(ADAPTER_IN_PACKAGE)
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        DOMAIN_PACKAGE,
                        APPLICATION_PACKAGE + "port.in..",
                        ADAPTER_IN_PACKAGE,
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.springframework..",
                        "lombok.."
                );

        rule.check(importedClasses);
    }

    @Test
    void outboundAdaptersShouldOnlyUseOutboundPorts() {
        ArchRule rule = classes()
                .that().resideInAPackage(ADAPTER_OUT_PACKAGE)
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        DOMAIN_PACKAGE,
                        APPLICATION_PACKAGE + "port.out..",
                        ADAPTER_OUT_PACKAGE,
                        "java..",
                        "javax..",
                        "jakarta..",
                        "org.springframework..",
                        "lombok.."
                );

        rule.check(importedClasses);
    }

    @Test
    void commandsShouldResideInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Command")
                .should().resideInAPackage(APPLICATION_PACKAGE + "port.in.command..");

        rule.check(importedClasses);
    }

    @Test
    void mappersShouldNotResideInDomainLayer() {
        ArchRule rule = noClasses()
                .that().haveSimpleNameEndingWith("Mapper")
                .should().resideInAPackage(DOMAIN_PACKAGE);

        rule.check(importedClasses);
    }

    @Test
    void servicesShouldResideInApplicationLayer() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage(APPLICATION_PACKAGE + "service..");

        rule.check(importedClasses);
    }
} 