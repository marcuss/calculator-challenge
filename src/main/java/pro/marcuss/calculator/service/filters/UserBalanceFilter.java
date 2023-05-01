package pro.marcuss.calculator.service.filters;

import org.springframework.web.filter.OncePerRequestFilter;
import pro.marcuss.calculator.security.AuthoritiesConstants;
import pro.marcuss.calculator.security.SecurityUtils;
import pro.marcuss.calculator.service.UserBalanceService;
import pro.marcuss.calculator.service.dto.UserBalanceDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


public class UserBalanceFilter extends OncePerRequestFilter {

    public final UserBalanceService userBalanceService;

    public UserBalanceFilter(UserBalanceService userBalanceService) {
        this.userBalanceService = userBalanceService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            filterChain.doFilter(request, response);//Admin can do whatever.
        } else {
            Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
            if (userLogin.isEmpty()) {
                returnPaymentRequiredError(response);
            }
            Optional<UserBalanceDTO> balanceDTO = userBalanceService.findUserBalanceByUserLogin(userLogin.get());
            if (balanceDTO.isPresent() && balanceDTO.get().getBalance() > 0) {
                filterChain.doFilter(request, response);//If user still has balance proceed.
            } else {
                returnPaymentRequiredError(response);
            }
        }
    }

    private void returnPaymentRequiredError(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_PAYMENT_REQUIRED, "Payment Required");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SecurityUtils.apiUnAuthorizeEndpoints
            .stream().filter(e -> e.equals(path))
            .findFirst().isPresent() || "/health".equals(path);
    }
}
