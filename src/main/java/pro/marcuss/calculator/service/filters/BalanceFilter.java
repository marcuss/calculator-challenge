package pro.marcuss.calculator.service.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pro.marcuss.calculator.security.AuthoritiesConstants;
import pro.marcuss.calculator.security.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BalanceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.printf("asdghjk");
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return; //Admin can do whatever
        }
        else {

        }
    }
}
