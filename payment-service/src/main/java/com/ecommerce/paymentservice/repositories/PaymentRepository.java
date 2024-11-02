package com.ecommerce.paymentservice.repositories;

import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderIdAndStatus(Long orderId, PaymentStatus status);

    Payment findByIdAndUserId(Long paymentId, Long userId);
}
