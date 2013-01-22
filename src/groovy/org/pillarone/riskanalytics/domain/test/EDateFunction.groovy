package org.pillarone.riskanalytics.domain.test

import org.apache.poi.ss.formula.OperationEvaluationContext
import org.apache.poi.ss.formula.eval.NumberEval
import org.apache.poi.ss.formula.eval.ValueEval
import org.apache.poi.ss.formula.functions.FreeRefFunction
import org.apache.poi.ss.usermodel.DateUtil

class EDateFunction implements FreeRefFunction {
    ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        def startValue = args[0].getInnerValueEval().numberValue
        Date startDate = DateUtil.getJavaDate(startValue)
        def calendar = Calendar.getInstance()
        calendar.time = startDate
        calendar.add(Calendar.MONTH, args[1].numberValue as int)
        return new NumberEval(DateUtil.getExcelDate(calendar.time))
    }
}