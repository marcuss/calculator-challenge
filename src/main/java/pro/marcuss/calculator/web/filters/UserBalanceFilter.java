package pro.marcuss.calculator.web.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import pro.marcuss.calculator.security.AuthoritiesConstants;
import pro.marcuss.calculator.security.SecurityUtils;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;
import pro.marcuss.calculator.web.rest.errors.ErrorConstants;
import pro.marcuss.calculator.web.rest.errors.UnfundedRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class UserBalanceFilter extends OncePerRequestFilter {

    public final UserBalanceService userBalanceService;

    private final ObjectMapper mapper;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public UserBalanceFilter(UserBalanceService userBalanceService) {
        this.userBalanceService = userBalanceService;
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            filterChain.doFilter(request, response);//Admin can do whatever.
        } else {
            Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();

            if (userLogin.isEmpty()) {
                returnErrorResponse(response, userLogin.get(), request);
            }
            Optional<UserBalanceDTO> balanceDTO = userBalanceService.findUserBalanceByUserLogin(userLogin.get());
            if (balanceDTO.isPresent() && balanceDTO.get().getBalance() > 0) {
                filterChain.doFilter(request, response);//If user still has balance proceed.
            } else {
                returnErrorResponse(response, userLogin.get(), request);
            }
        }
    }

    private void returnErrorResponse(HttpServletResponse response, String login, HttpServletRequest request) throws IOException {
        String title = "Operation can not be execute, insufficient funds ";
        UnfundedRequestAlertException ex = new UnfundedRequestAlertException(title, "record", "insufficient-funds");
        HttpHeaders headers = HeaderUtil.createFailureAlert(applicationName, true, ex.getEntityName(), ex.getErrorKey(), ex.getMessage());
        headers.entrySet().forEach(entry -> response.setHeader(entry.getKey(), entry.getValue().stream().collect(Collectors.joining(","))));

        Map<String, String> body = new HashMap<>();
        body.put("type", ErrorConstants.PROBLEM_BASE_URL);
        body.put("title", title);
        body.put("status", HttpStatus.PAYMENT_REQUIRED.toString());
        body.put("detail", "User: " + login + " has insufficient funds for executing operations. " + request.getReader().readLine());
        body.put("path", request.getRequestURL().toString());
        body.put("message", "error.http.402");
        response.setStatus(HttpStatus.PAYMENT_REQUIRED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), body);
    }

    private void returnPaymentRequiredError(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "Payment Required");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "/api/v1/records".equals(path) && "GET".equals(request.getMethod());
    }
}
