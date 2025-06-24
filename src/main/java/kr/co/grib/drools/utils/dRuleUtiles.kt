package kr.co.grib.drools.utils

import kr.co.grib.drools.api.gRules.dto.RuleDto

object dRuleUtiles {
    //<editor-fold desc="rule 평가">
    fun setEvaluationRules(
        rule: RuleDto,
        data: Map<String, Any>
    ): Boolean {
        if (rule.conditions.isNullOrEmpty()){
            return false
        }

        for (condition in rule.conditions) {
            val actualValue = data[condition.function] ?: return false

            val matching = when(rule.type) {
                "string" -> actualValue.toString() == condition.value.toString()
                "default" -> {
                    val functionVale = (actualValue as Number).toDouble()
                    val compareValue = (condition.value as Number).toDouble()
                    getCompareValue(functionVale, compareValue, condition.operator)
                }
//                "range" -> {
//                    val functionValue = (actualValue as Number).toDouble()
//                    val minValue = (condition.value1 as Number).toDouble()
//                    val maxValue = (condition.value2 as Number).toDouble()
//
//                }


                else -> false
            }

            if (!matching) return false
        }
        return true
    }
    //</editor-fold desc="rule 평가">

    //<editor-fold desc="rule 평가">
    fun getCompareValue(
        functionValue: Double,
        compareValue: Double,
        operation: String
    ): Boolean {
        return when(operation) {
            "greater then" -> functionValue > compareValue
            "greater then or equal to" -> functionValue >= compareValue
            "less then" -> functionValue < compareValue
            "less then or eqaul to" -> functionValue <= compareValue
            "equal to" -> functionValue == compareValue
            else ->  false
        }
    }

    //</editor-fold desc="rule 평가">
}