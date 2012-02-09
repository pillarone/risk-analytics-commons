package org.pillarone.riskanalytics.domain.test

import java.text.SimpleDateFormat
import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.ExcelImportUtils

/**
 * Handles import of spreadsheets for SpreadsheetUnitTest providing a simplified API to test writers.
 * Spreadsheets have to be placed in path and spreadsheet containing a validation error will be written to the subdirectory
 * validationFailed.
 *
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
class SpreadsheetImporter extends AbstractExcelImporter {

    private static final String path = "test/data/spreadsheets/"
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmm")
    private String fileName
    private String fileNameValidationErrors
    CommentInvalidCellsImportCellCollector cellCollector = new CommentInvalidCellsImportCellCollector()

    SpreadsheetImporter(String fileName) {
        this(path, fileName)
    }

    SpreadsheetImporter(String path, String fileName) {
        super(path + fileName)
        this.fileName = fileName
    }

    /**
     * @param config mapping for spreadsheet columns to keys of returned map
     * @param propertyConfigurationMap
     * @return List of rows containing a map object with the keys specified in config and applying validation rules
     *          provided in propertyConfigurationMap. Any validation errors are collected within cellCollector.
     */
    List columns(Map config, Object propertyConfigurationMap) {
        ExcelImportUtils.columns(workbook, config, cellCollector, propertyConfigurationMap)
    }
    
    Map cells(Map config) {
        ExcelImportUtils.cells(workbook, config)
    }

    /**
     * If a spreadsheet contains a validation error a copy of it is persisted in the place specified by
     * getFileNameValidationErrors()
     */
    void writeValidationErrors() {
        if (cellCollector.validationFailuresCollected()) {
            workbook.write(new FileOutputStream(getFileNameValidationErrors()))
        }
    }

    /**
     * @return true if no validation error occured
     */
    boolean validFile() {
        !cellCollector.validationFailuresCollected()
    }
    
    String getFileName() {
        path + fileName
    }

    /**
     * @return copy file to subdirectory validationfailed and add a timestamp
     */
    String getFileNameValidationErrors() {
        if (fileNameValidationErrors == null) {
            String timeStamp = formatter.format(System.currentTimeMillis())
            int fileExtensionLength = fileName.contains(".xlsx") ? 5 : 4
            String fileNameWOExtension = fileName.substring(0, fileName.size() - fileExtensionLength)
            fileNameValidationErrors = "$path/validationfailed/${fileNameWOExtension}_${timeStamp}.xlsx"
        }
        fileNameValidationErrors
    }
}
