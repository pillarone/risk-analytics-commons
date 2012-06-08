package org.pillarone.riskanalytics.domain.test

import org.pillarone.riskanalytics.core.initialization.StandaloneConfigLoader
import grails.util.Environment

/**
 * Common base class for unit test using a spreadsheet for their parameterization/expected values.
 *
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
abstract class SpreadsheetUnitTest extends GroovyTestCase {

    private List<SpreadsheetImporter> importers = []
    private boolean checkedForValidationErrors = false

    /** This method has to be called by all derived tests. Apply the log4j configuration from the Config.groovy
     *  and init importers. */
    final void setUp() {
        StandaloneConfigLoader.loadLog4JConfig(Environment.current.name)
        initSpreadsheets(getSpreadsheetNames())
        doSetUp()
    }

    /**
     * This method has been added as setUp() can't be overwritten to make sure it is called by every derived test class.
     */
    void doSetUp() {
    }

    /**
     * Make sure that at least one importer is checked for a valid file. This method fails if a test case does not call
     * manageValidationErrors()
     */
    @Override
    protected void tearDown() {
        super.tearDown()
        if (!checkedForValidationErrors) {
            assertTrue "manageValidationErrors was never called", false
        }
    }

    /**
     * Test should fail if the underlying spreadsheet contains a validation error.
     * @param importer
     */
    protected void manageValidationErrors(SpreadsheetImporter importer) {
        checkedForValidationErrors = true
        importer.writeValidationErrors()
        assertTrue "Validation errors occured, details available in ${importer.getFileNameValidationErrors()}", importer.validFile()
    }
    
    /** Including file extension (.xlsx) but without path */
    abstract List<String> getSpreadsheetNames();

    /** Initialize importers by using the provided fileNames in a test case */
    void initSpreadsheets(List<String> fileNames) {
        for (String fileName : fileNames) {
            importers << new SpreadsheetImporter(fileName)
        }
    }

    List<SpreadsheetImporter> getImporters() {
        importers
    }

    boolean getCheckedForValidationErrors() {
        return checkedForValidationErrors
    }

    void setCheckedForValidationErrors(boolean checkedForValidationErrors) {
        this.checkedForValidationErrors = checkedForValidationErrors
    }
}
