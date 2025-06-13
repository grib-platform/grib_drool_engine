package kr.co.grib.drools.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.grib.drools.utils.getLogger
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class IpLoggingFilter : OncePerRequestFilter()  {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val clientIp = getClientIp(request)
        logger.info("client.ip.$clientIp")
        filterChain.doFilter(request,response)
    }

    private fun getClientIp(
        request: HttpServletRequest
    ): String {
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

        logger.info("check.request.remoteAddr.${request.remoteAddr}")
        return request.remoteAddr

    }

}