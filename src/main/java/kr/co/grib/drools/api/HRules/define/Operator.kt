package kr.co.grib.drools.api.HRules.define


//<editor-fold desc="without Drools operations">
enum class Operator (val op: String) {
    MATCH("match"),
    GREATER_THAN("greater than"),
    GREATER_THAN_OR_EQUAL("greater than or equal to"),
    LESS_THAN("less than"),
    LESS_THAN_OR_EQUAL("less than or equal"),
    EQUAL_TO("equal to"),
    INSIDE("inside"),
    OUTSIDE("outside");

    companion object {
        fun from(op: String): Operator? = values().find { it.op == op }
    }
}
//</editor-fold desc="without Drools operations">