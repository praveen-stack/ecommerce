package com.ecommerce.paymentservice.controllers;

import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.dtos.CreatePaymentDto;
import com.ecommerce.paymentservice.dtos.PaymentDto;
import com.ecommerce.paymentservice.mappers.PaymentMapper;
import com.ecommerce.paymentservice.models.Payment;
import com.ecommerce.paymentservice.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/payments")
public class PaymentController {

     @Autowired
     private PaymentService paymentService;

     @Autowired
     private PaymentMapper paymentMapper;

     @PostMapping
     public PaymentDto createPayment(Authentication authentication, @RequestBody @Valid CreatePaymentDto createPaymentDto) {
         AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
         Payment payment = paymentService.createPayment(authUser, createPaymentDto.getOrderId(), createPaymentDto.getAmount(), createPaymentDto.getPaymentMethod());
         return paymentMapper.toDto(payment);
     }

     @GetMapping("/{paymentId}")
     public PaymentDto  getPayment(Authentication authentication, @PathVariable Long paymentId) {
         AuthorizedUser authUser = (AuthorizedUser) authentication.getPrincipal();
         Payment payment = paymentService.getPaymentById(authUser, paymentId);
         return paymentMapper.toDto(payment);
     }
}
