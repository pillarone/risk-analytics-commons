package org.pillarone.riskanalytics.domain.test

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.XSSFClientAnchor
import org.apache.poi.xssf.usermodel.XSSFComment
import org.apache.poi.xssf.usermodel.XSSFDrawing
import org.grails.plugins.excelimport.ImportCellCollector

/**
 * If a cell is violating its propertyConfiguration a comment is added to the cell.
 *
 * @author stefan.kunz (at) intuitive-collaboration (dot) com
 */
class CommentInvalidCellsImportCellCollector implements ImportCellCollector  {

    static Log log = LogFactory.getLog(CommentInvalidCellsImportCellCollector)
    private boolean hasFailure = false

    @Override
    void reportCell(Cell cell, Object propertyConfiguration) {
        try {
            log.debug "Reporting cell $cell, config: $propertyConfiguration"
            hasFailure = true
            addComment(cell, "Validation error: $propertyConfiguration")
        } catch (e) {
            log.error "EXCEPTION CAUGHT $e"
        }
    }

    void addComment(Cell cell, String text) {
        cell.getSheet().getWorkbook()
        XSSFDrawing drawing = cell.getSheet().createDrawingPatriarch() as XSSFDrawing
        XSSFComment comment = drawing.createCellComment(new XSSFClientAnchor())
        comment.author = "PillarOne"
        comment.column = cell.columnIndex
        comment.row = cell.rowIndex
        comment.setString text
    }

    boolean validationFailuresCollected() {
        hasFailure
    }
}