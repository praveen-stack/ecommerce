package com.ecommerce.usermanagementservice.repositories;

import com.ecommerce.usermanagementservice.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUserId(Long userId);

}
