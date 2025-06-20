package kr.co.grib.drools.config

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class RequestInfoProvider {

    fun getCurrentRequest(): HttpServletRequest? {
        return (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
    }
    //ip
    fun getClientIp(): String?
    {
        val request = getCurrentRequest() ?: return null
        val headersToCheck = listOf(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
        )
        for (header in headersToCheck){
            val xfHeader = request.getHeader(header)
            if (!xfHeader.isNullOrBlank() && !xfHeader.equals("unknown", ignoreCase = true)) {
                return xfHeader.split(",")[0].trim()
            }
        }
        val ip = request.remoteAddr
        //로컬 테스트시
        return  if (ip == "::1" || ip == "0:0:0:0:0:0:0:1") "127.0.0.1" else ip
    }

    //method
    fun getApiMethod(): String ?= getCurrentRequest()?.method
    //uri
    fun getApiUri(): String ?= getCurrentRequest()?.requestURI

    //<editor-fold desc="token 에서 나온  username">
    fun getUserName(): String = getCurrentRequest()?.getAttribute("username").toString()
    //</editor-fold desc="token 에서 나온  username">
}