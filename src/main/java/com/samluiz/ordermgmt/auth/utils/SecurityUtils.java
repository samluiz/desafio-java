package com.samluiz.ordermgmt.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class SecurityUtils {

    public static void sendErrorResponse(HttpServletResponse response, HttpServletRequest request, Exception e) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> errorMap = new LinkedHashMap<>();
            String message = ExceptionUtils.getRootCauseMessage(e);
            String error = e.getClass().getSimpleName();

            errorMap.put("timestamp", LocalDateTime.now().format(ISO_LOCAL_DATE_TIME));

            if (error.contains("ServletException")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                errorMap.put("status", HttpStatus.BAD_REQUEST.value());
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                errorMap.put("status", HttpStatus.UNAUTHORIZED.value());
            }

            errorMap.put("error", error);

            if (ExceptionUtils.getRootCauseMessage(e).contains("ConstraintViolationException")) {
                String regex = "messageTemplate='([^']+)'";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(message);

                StringBuilder formattedMessage = new StringBuilder();
                while (matcher.find()) {
                    String messageTemplate = matcher.group(1);
                    formattedMessage.append(messageTemplate).append(", ");
                }

                if (formattedMessage.length() > 0) {
                    formattedMessage.delete(formattedMessage.length() - 2, formattedMessage.length());
                }

                errorMap.put("message", formattedMessage.toString());
            } else {
                errorMap.put("message", ExceptionUtils.getRootCauseMessage(e));
            }

            errorMap.put("path", request.getRequestURI());
            String errorResponse = mapper.writeValueAsString(errorMap);
            response.getOutputStream().write(errorResponse.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new UnknownError(ex.getMessage());
        }
    }
}
