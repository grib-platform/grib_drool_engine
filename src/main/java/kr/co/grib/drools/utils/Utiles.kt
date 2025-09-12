package kr.co.grib.drools.utils

import com.fasterxml.jackson.databind.ObjectMapper
import kr.co.grib.drools.api.HRules.dto.FnData
import kr.co.grib.drools.api.HRules.dto.RuleDto
import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.rules.templateManager.dto.RuleTemplateDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import kr.co.grib.drools.api.CRules.dto.ActionDto
import kr.co.grib.drools.api.CRules.dto.CFnData
import kr.co.grib.drools.api.CRules.dto.ConditionsDto
import kr.co.grib.drools.api.CRules.dto.RedisRuleDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties

//logger  관련
inline fun <reified T> T.getLogger(): Logger = LoggerFactory.getLogger(T::class.java)

object Utiles {

    //redis 직렬화를 위해
    private val objectMapper = ObjectMapper()

    //<editor-fold desc="variable to Map">
    fun getVariableToMap(data: Any): Map<String,Any?>{
        return data::class.memberProperties.associate {
            prop -> prop.name to prop.getter.call(data)
        }
    }
    //</editor-fold desc="variable to Map">

    //<editor-fold desc="Drools 템플릿 유효성 검사">
    fun isValidDroolsTemplate(template: String): Boolean {
        return template.isNotEmpty() && template.isNotBlank()
    }
    //</editor-fold desc="Drools 템플릿 유효성 검사">

    //<editor-fold desc="Thymeleaf placeholder : single">
    fun convertToTemplateVariable(
        request: RuleRequestDto
    ): Map<String,Any?> {
        val result = mutableMapOf<String,Any?>()
        //groupId 고정으로 넣기
        //result["groupId"] = request.ruleGroup

        request.facts.forEachIndexed{
            index, fact ->
            val idx = index + 1
            result["deviceId$idx"] = fact.deviceId
            result["functionName$idx"] = fact.functionName
            result["functionValue$idx"] = fact.functionValue
        }
        return result
    }
    //</editor-fold desc="Thymeleaf placeholder : single">

    //<editor-fold desc="Thymeleaf placeholder : every rules">
    fun convertToTemplateEveryVariable(
        request: RuleTemplateDto,
        header: Boolean
    ): Map<String,Any?> {
        val result = mutableMapOf<String,Any?>()
        //ruleGroup 고정으로 넣기
        result["ruleGroup"] = request.ruleGroup
        result["stringRules"] = request.stringRules
        result["defaultRules"] = request.defaultRules
        result["rangeRules"] = request.rangeRules
        result["includeHeader"] = header
        return result
    }
    //</editor-fold desc="Thymeleaf placeholder : every rules">


    //<editor-fold desc="Drl: 기존 Text 룰 유지 해당 rule 블록만 덮어쓰고 나머지 그대로 유지">
    fun mergeDrlRuleOverwrite(
        existStr: String,
        newStr: String
    ): String {
        val result = StringBuilder()
        val ruleRegex = Regex("""(?s)(rule\s*"([^"]+?)".*?end)""") // rule~end 전체 블록 추출

        val seperateText = ruleRegex.find(existStr)
        var importPart = if (seperateText != null) seperateText.range.first else existStr.length

        //import 되는 부분 substr
        val header = existStr.substring(0,importPart).trimEnd()
        //String to map
        val originRuleMap = ruleRegex.findAll(existStr).associate {
            it.groupValues[2] to it.groupValues[1]
        }.toMutableMap()
        val newRuleMap = ruleRegex.findAll(newStr).associate {
            it.groupValues[2] to it.groupValues[1]
        }.toMutableMap()
        // 텍스트 병합
        for((name, newBlock) in newRuleMap) {
            originRuleMap[name] = newBlock
        }
        //Header와 추가된 문자열 추가
        result.appendLine(header)
            .appendLine() // 빈줄
            .appendLine(originRuleMap.values.joinToString("\n\n") { it.trim() })

        return result.toString().trim()
    }
    //</editor-fold desc="Drl: 기존 Text 룰 유지 해당 rule 블록만 덮어쓰고 나머지 그대로 유지">

    //<editor-fold desc="Drl: 기존 룰 이름이 존재 하면 새룰 을 기존 룰에 덮어쓰고, 아니면 새룰을 추가 하기">
    fun mergeDrlTextAvoidingDuplicates(
        existingRulesText: String,
        newRulesText: String
    ): String {
        val originRuleMap = buildRuleToMap(existingRulesText).toMutableMap()
        val newRuleMap = buildRuleToMap(newRulesText)

        for((name, newBlock) in newRuleMap){
            //덮어쓰기 및 추가
            originRuleMap[name] = newBlock
        }
        return originRuleMap.values.joinToString("\n\n") {it.trim()}
    }
    //</editor-fold desc="Drl: 기존 Text에 새로운 rule로 덮어쓰기">

    //<editor-fold desc="룰 블록을 정확히 rule ~~ end 까지 자르는 방식">
    fun buildRuleToMap(
        ruleText: String
    ): Map<String,String> {
        val rulePattern = Regex("""(?s)(rule\s*"([^"]+?)".*?end)""")  // DOTALL: \n 포함, rule~end 블록 전체 추출
        val result = mutableMapOf<String, String>()

        for (match in rulePattern.findAll(ruleText)) {
            val fullBlock = match.groupValues[1].trim()
            val ruleName = match.groupValues[2].trim()
            result[ruleName] = fullBlock
        }

        return result
    }
    //</editor-fold desc="룰 블록을 정확히 rule ~~ end 까지 자르는 방식">

    //<editor-fold desc="find every Rules">
    fun findEveryRules(
        ruleText: String
    ): Set<String> {
        val regex = Regex("""rule\s*"([^"]+)"""")
        return regex.findAll(ruleText).map { it.groupValues[0]}.toSet()
    }
    //</editor-fold desc="find every Rules">

    //<editor-fold desc="find every Rules By names">
    fun extractRulesByNames(
        ruleText: String,
        ruleNames: List<String>
    ): String {
        val builder = StringBuilder()

        for (ruleName in ruleNames) {
            val ruleRegex = Regex("""(?s)(rule\s*"${Regex.escape(ruleName)}".*?end)""")
            val match = ruleRegex.find(ruleText)
            if (match != null) {
                builder.appendLine(match.value.trim())
                builder.appendLine() // 줄바꿈 하나 추가 (가독성용)
            }
        }

        return builder.toString().trim()
    }

    //</editor-fold desc="find every Rules By names">

    //<editor-fold desc="delete Rule">
    fun removeRuleByRuleName(
        drlText: String,
        ruleName: List<String>
    ):String {
        var str = drlText
        for (name in ruleName) {
            val ruleBlockRegex = Regex("""(?s)(rule\s*"${Regex.escape(name)}".*?end)""")
            str = str.replace(ruleBlockRegex, "").trim()
        }
        return str
    }
    //</editor-fold desc="delete Rule">

    //<editor-fold desc="RuleName 중 실제로 존해 하는 rule 갯수 반환">
    fun countExistingRules(
        drlText: String,
        ruleName: List<String>
    ): Int {
        return ruleName.count { ruleName ->
            val ruleRegex = Regex("""(?s)rule\s*"${Regex.escape(ruleName)}".*?end""")
            ruleRegex.containsMatchIn(drlText)
        }
    }
    //</editor-fold desc="RuleName 중 실제로 존해 하는 rule 갯수 반환">

    //<editor-fold desc="rule 평가">
    fun setEvaluationRules(
        rule: RuleDto,
        data: List<FnData>
    ): Boolean {
        if (rule.conditions.isEmpty()){
            return false
        }

        for (condition in rule.conditions) {
            val actualFnData  = data.find { it.functionName == condition.function } ?: return false
            val actualValue = actualFnData.functionValue

            val matching = when(rule.type) {
                "string" -> actualValue.toString() == condition.value.toString()
                "default" -> {
                    val functionVale = (actualValue as Number).toDouble()
                    val compareValue = (condition.value as? Number)?.toDouble()
                        ?: condition.value.toString().toDoubleOrNull() ?: return false
                    getCompareValue(functionVale, compareValue, condition.operator.op)
                }
                "range" -> {
                    val functionValue =  (actualValue as Number).toDouble()
                    val minValue = (condition.value1 as? Number)?.toDouble()
                        ?: condition.value1.toString().toDoubleOrNull() ?: return false
                    val maxValue = (condition.value2 as? Number)?.toDouble()
                        ?: condition.value2.toString().toDoubleOrNull() ?: return false
                    when(condition.operator.op) {
                        //min 부터 max 까지의 범위에 포함 되는지 (여부를 검사하는 표현
                        "inside" -> functionValue in minValue .. maxValue
                        "outside" -> functionValue < minValue || functionValue > maxValue
                        else ->  false
                    }
                }
                else -> false
            }

            if (!matching) return false
        }
        return true
    }
    //</editor-fold desc="rule 평가">

    //<editor-fold desc="rule 평가">
    fun getCompareValue(
        functionValue: Double, //a
        compareValue: Double, //b
        operation: String
    ): Boolean {
        return when(operation) {
            "greater than" -> functionValue > compareValue
            "greater than or equal to" -> functionValue >= compareValue
            "less than" -> functionValue < compareValue
            "less than or eqaul to" -> functionValue <= compareValue
            "equal to" -> functionValue == compareValue
            else ->  false
        }
    }
    //</editor-fold desc="rule 평가">

    //<editor-fold desc="json string to Map">
    fun getJsonToMap(jsonStr: String): Map<String,Any> =
        objectMapper.readValue(jsonStr, object : TypeReference<Map<String, Any>>() {})
    //</editor-fold desc="json string to Map">

    //<editor-fold desc="object to json">
    fun getObjectToJson(obj: Any): String {
        val node: JsonNode = objectMapper.valueToTree(obj)
        return node.toString()
    }
    //<editor-fold desc="object to json">

    //<editor-fold desc="json string to List<map>">
    fun getJsonToList(jsonStr: String): List<Map<String,Any>> =
        objectMapper.readValue(jsonStr, object : TypeReference<List<Map<String,Any>>>() {})
    //</editor-fold desc="json String to List<map>">

    //<editor-fold desc="json string to List<map>">
    fun getJsonToListDto(jsonStr: String): List<RedisRuleDto> =
        objectMapper.readValue(jsonStr, object : TypeReference<List<RedisRuleDto>>() {})
    //</editor-fold desc="json String to List<map>">

    //<editor-fold desc="json string to List<객체>">
    fun <T> getJsonToDto(jsonStr: String, dtoClass: Class<T>): T =
        objectMapper.readValue(jsonStr, dtoClass)
    //<editor-fold desc="json string to List<객체>">

    //<editor-fold desc="redis String Rule to Object ">
    fun getStringParseRule(
        ruleList: List<RedisRuleDto>
    ): List<Triple<ConditionsDto, ActionDto, Boolean>> {
        return ruleList.map { raw ->
            val condition = getJsonToDto(raw.conditions, ConditionsDto::class.java)
            val action = getJsonToDto(raw.actions, ActionDto::class.java)
            Triple(condition, action, raw.active)
        }
    }
    //<editor-fold desc="redis String Rule to Object ">

    //<editor-fold desc="센서 값에 대한 매칭 되는 모든 룰을 반환">
    fun getCRuleMatchedAction(
        sensorInfo: CFnData,
        rules:  List<Triple<ConditionsDto, ActionDto,Boolean>>
    ): List<ActionDto> {
        val rtn  = mutableListOf<ActionDto>()
        for ((cond, act,active) in rules) {
            //active 유무
            if (!active) continue
            if (sensorInfo.functionName !=  cond.functionName) continue
            //sensor value
            val value = sensorInfo.functionValue.toDouble()
            val math = when(cond.operator) {
                "MATCH" -> value == (cond.functionValue ?: Double.NaN)
                "GT" ->  value >  (cond.functionValue ?: Double.MIN_VALUE)
                "GTE" -> value >= (cond.functionValue ?: Double.MIN_VALUE)
                "LT" -> value < (cond.functionValue ?: Double.MAX_VALUE)
                "LTE" -> value <= (cond.functionValue ?: Double.MAX_VALUE)
                "INSIDE" ->  value >= (cond.minValue ?: Double.MIN_VALUE) &&
                             value <= (cond.maxValue ?: Double.MAX_VALUE)
                "OUTSIDE" -> value < (cond.minValue ?: Double.MIN_VALUE) ||
                             value > (cond.maxValue ?: Double.MAX_VALUE)
                else -> false
            }
            if (math) rtn.add(act)
        }
        return rtn
    }
    //</editor-fold desc="센서 값에 대한 매칭 되는 모든 룰을 반환">

    //<editor-fold desc="condition이 등록된 애랑 같은지 판단">
    fun getChkConditions(
        reqContions:  Any,
        condtions: String
    ): Boolean {
        val check = getObjectToJson(reqContions)
        val result = objectMapper.readTree(check) == objectMapper.readTree(condtions)

        return result
    }
    //</editor-fold desc="condition이 등록된 애랑 같은지 판단">

    //<editor-fold desc="Json String에서 특정 String뽑기">
    fun getStringJsonToString(
        str :String,
        getStr: String
    ): String {
        val mapper = ObjectMapper()
        val node = mapper.readTree(str)   // 바로 파싱 가능
        return node.get(getStr)?.asText() ?: ""
    }
    //</editor-fold desc="Json String에서 특정 String뽑기">

    //<editor-fold desc="JsonFunctionVale">
    fun getStringJsonToFunctionValue(
        str :String
    ): String {
        val mapper = ObjectMapper()
        val node = mapper.readTree(str)

        return if (node.has("functionValue")) {
            node.get("functionValue").asText()
        } else {
            val min = node.get("minValue")?.asText()
            val max = node.get("maxValue")?.asText()
            if (min != null && max != null) "$min~$max" else ""
        }
    }
    //</editor-fold desc="JsonFunctionVale">


    //<editor-fold desc="active Vs deactive">
    fun getAtiveDeactive(
        str: String
    ): String {
        if (str.equals("active")) return "true" else return "false"
    }
    //<editor-fold desc="active Vs deactive">



}