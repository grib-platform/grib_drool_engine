package kr.co.grib.drools.api.HRules.rules

import kr.co.grib.drools.api.HRules.define.Operator
import kr.co.grib.drools.api.HRules.dto.Conditions
import kr.co.grib.drools.api.HRules.dto.RuleDto
import kr.co.grib.drools.api.HRules.dto.RuleResult

//<editor-fold desc="without Drools Rule Definition">
object HRuleFile {
    //기본 String 비교
    val stringMatchRule  = RuleDto(
        ruleName = "Status Match ON",
        type = "string",
        conditions = listOf(
            Conditions(
                function = "Status",
                operator = Operator.MATCH,
                value = "ON"
            )
        ),
        result = RuleResult(
            code = "STAUS_ON",
            messsage = "상태가 ON 입니다"
        )
    )
    //기본 default
    val greaterThanRule =  RuleDto(
        ruleName = "Temperature Greater Than 30",
        type = "default",
        conditions = listOf(
            Conditions(
                function = "Temperature",
                operator = Operator.GREATER_THAN,
                value = 30
            )
        ),
        result = RuleResult(
            code = "TEMP_GT_30",
            messsage = "온도가 30도 초과 입니다."
        )
    )

    val greaterThanOrEqualRule = RuleDto(
        ruleName = "Temperature Greater Than or Equal 25",
        type = "default",
        conditions = listOf(
            Conditions(
                function = "Temperature",
                operator = Operator.GREATER_THAN_OR_EQUAL,
                value = 25
            )
        ),
        result = RuleResult(
            code = "TEMP_GTE_25",
            messsage = "온도가 25도 이상입니다."
        )
    )

    val lessThanRule = RuleDto(
        ruleName =  "Temperature Less Than 15",
        type = "default",
        conditions = listOf(
            Conditions (
                function = "Temperature",
                operator = Operator.LESS_THAN,
                value =  15
            )
        ),
        result = RuleResult(
            code = "TEMP_LT_15",
            messsage = "온도가 15도 미만 입니다."
        )
    )

    val lessThanOrEqualRule = RuleDto(
        ruleName = "Temperature Less Than Or Equal 10",
        type = "default",
        conditions =  listOf(
            Conditions(
                function = "Temperature",
                operator = Operator.LESS_THAN_OR_EQUAL,
                value = 10
            )
        ),
        result = RuleResult(
            code = "TEMP_LET_10",
            messsage = "온도가 10도 이하 입니다. "
        )
    )

    val equalToRule = RuleDto(
        ruleName = "Valve Status Equal to 1",
        type = "default",
        conditions = listOf(
            Conditions(
                function = "Valve",
                operator = Operator.EQUAL_TO,
                 value = 1
            )
        ),
        result = RuleResult(
            code = "VALVE_EQ_1",
            messsage = "밸브 값이 1 입니다. "
        )
    )

    //범위 range
    val insideRangeRule = RuleDto(
        ruleName = "Temperature Inside 20 to 30",
        type = "range",
        conditions = listOf(
            Conditions(
                function = "Temperature",
                operator = Operator.INSIDE,
                value1 =  20,
                value2 =  30
            )
        ),
        result = RuleResult(
            code = "TEMP_IN_RANGE",
            messsage = "온도가 20도 이상 30도 이하 범위 내에 있습니다. "
        )
    )

    val outsideRangeRule = RuleDto(
        ruleName = "Temperature Outside 10 to 15",
        type = "range",
        conditions = listOf(
            Conditions(
                function = "Temperature",
                operator = Operator.INSIDE,
                value1 = 10,
                value2 = 15
            )
        ),
        result = RuleResult(
            code = "TEMP_OUT_RANGE",
            messsage = "온도가 10도 미만이거나 15도 초과 입니다."
        )
    )
}
//</editor-fold desc="without Drools Rule Definition">