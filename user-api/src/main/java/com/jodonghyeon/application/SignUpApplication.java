package com.jodonghyeon.application;

import com.jodonghyeon.client.MailgunClient;
import com.jodonghyeon.client.mailgurn.SendMailForm;
import com.jodonghyeon.domain.SignUpForm;
import com.jodonghyeon.domain.model.Customer;
import com.jodonghyeon.exception.CustomException;
import com.jodonghyeon.exception.ErrorCode;
import com.jodonghyeon.service.SignUpCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignUpApplication {
    private final MailgunClient mailgunClient;
    private final SignUpCustomerService signUpCustomerService;

    public void customerVerify(String email, String code) {
        signUpCustomerService.verifyEmail(email, code);
    }

    public String customerSingUp(SignUpForm form) {

        if (signUpCustomerService.isEmailExist(form.getEmail())) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        } else {
            Customer c = signUpCustomerService.signUp(form);
            LocalDateTime now = LocalDateTime.now();

            String code = getRandomCode();
            SendMailForm sendMailForm = SendMailForm.builder()
                    .from("jawoo1003@gmail.com")
                    .to(form.getEmail())
                    .subject("Verification Email!")
                    .text(getVerificationEmailBody(c.getEmail(), c.getName(), code))
                    .build();
            log.info("Send email result : " + mailgunClient.sendEmail(sendMailForm));
            signUpCustomerService.changeCustomerValidateEmail(c.getId(), code);
            return "회원 가입에 성공하였습니다.";
        }

    }

    private String getRandomCode() {
        return RandomStringUtils.random(10, true, true);
    }

    private String getVerificationEmailBody(String email, String name, String code) {
        StringBuilder builder = new StringBuilder();
        return builder.append("Hello ").append(name).append("! Please Click Link for verification\n\n")
//                .append("http://localhost:8080/customer/signup/verify?email=")
                .append("http://localhost:8080/customer/verify/customer?email=")
                .append(email)
                .append("&code=")
                .append(code).toString();
    }
}
