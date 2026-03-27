package lab.coder.colly.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchitectureIsolationTest {

    @Test
    void domain_layer_must_not_depend_on_adapter_layer() {
        var classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("lab.coder.colly");

        noClasses()
            .that().resideInAnyPackage("..domain..domain..")
            .should().dependOnClassesThat().resideInAnyPackage("..adapter..")
            .check(classes);
    }

    @Test
    void user_domain_must_not_directly_depend_on_order_domain() {
        var classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("lab.coder.colly");

        noClasses()
            .that().resideInAnyPackage("..domain.user..")
            .should().dependOnClassesThat().resideInAnyPackage("..domain.order..")
            .check(classes);
    }
}
