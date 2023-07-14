package com.jodonghyeon.controller;

import com.jodonghyeon.domain.customer.CustomerDto;
import com.jodonghyeon.domain.model.Customer;
import com.jodonghyeon.domain.repository.CustomerRepository;
import com.jodonghyeon.exception.CustomException;
import com.jodonghyeon.exception.ErrorCode;
import com.jodonghyeon.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.example.config.JwtAuthenticationProvider;
import org.example.domain.common.UserVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final JwtAuthenticationProvider provider;
    private final CustomerService customerService;
    @GetMapping("/getInfo")
    public ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        UserVo vo = provider.getUserVo(token);

        Customer c = customerService.findByIdAndEmail(vo.getId(), vo.getEmail()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );

        return ResponseEntity.ok(CustomerDto.from(c));
    }
}
