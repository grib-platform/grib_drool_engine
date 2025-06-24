package kr.co.grib.drools.utils

import kr.co.grib.drools.api.rules.dto.RuleRequestDto
import kr.co.grib.drools.api.templateManager.dto.RuleTemplateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties

//logger  관련
inline fun <reified T> T.getLogger(): Logger = LoggerFactory.getLogger(T::class.java)

object Utiles {

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
        //groupId 고정으로 넣기
        result["groupId"] = request.ruleGroup
        result["singleRules"] = request.singleRules
        result["andRules"] = request.andRules
        result["orRules"] = request.orRules
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
}