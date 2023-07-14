package com.jodonghyeon.application;

import com.jodonghyeon.domain.SignInForm;
import com.jodonghyeon.domain.model.Customer;
import com.jodonghyeon.exception.CustomException;
import com.jodonghyeon.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtAuthenticationProvider;
import org.example.domain.common.UserType;
import org.springframework.stereotype.Service;

import static com.jodonghyeon.exception.ErrorCode.LOGIN_CHECK_FAIL;

@Service
@RequiredArgsConstructor
public class SignInApplication {

    private final CustomerService customerService;
    private final JwtAuthenticationProvider provider;
    public String customerLoginToken(SignInForm form) {
        // 1. 로그인 가능 여부
        Customer c = customerService.findValidCustomer(form.getEmail(), form.getPassword())
                .orElseThrow(() -> new CustomException(LOGIN_CHECK_FAIL));
        // 2. 토큰을 발행하고

        // 3. 토큰을 response한다.
        return provider.createToken(c.getEmail(), c.getId(), UserType.CUSTOMER);
    }
}
