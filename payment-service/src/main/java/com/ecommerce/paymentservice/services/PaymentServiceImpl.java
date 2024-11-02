package com.ecommerce.paymentservice.services;

import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.exceptions.NotFoundException;
import com.ecommerce.paymentservice.models.Payment;
import com.ecommerce.paymentservice.paymentgateways.PaymentGatewayFactory;
import com.ecommerce.paymentservice.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentGatewayFactory paymentGatewayFactory;

    private Payment getActivePayment(Long orderId) {
        return paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.PENDING);
    }

    @Override
    @Transactional
    public Payment createPayment(AuthorizedUser user, Long orderId, Double amount) {
        var payment = getActivePayment(orderId);
        if (payment != null) {
            return payment;
        }
        var paymentGateway = paymentGatewayFactory.getPaymentGateway();
        PaymentLink link = paymentGateway.createPaymentLink(orderId, user.getId(), amount);
        payment = new Payment();
        payment.setGatewayPaymentId(link.getId());
        payment.setPaymentLink(link.getLink());
        payment.setOrderId(orderId);
        payment.setUserId(user.getId());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setGateway(paymentGateway.getGateway());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(AuthorizedUser user, Long paymentId) {
        var payment = paymentRepository.findByIdAndUserId(paymentId, user.getId());
        if (payment == null) {
            throw new NotFoundException("Payment not found");
        }
        return payment;
    }
}
