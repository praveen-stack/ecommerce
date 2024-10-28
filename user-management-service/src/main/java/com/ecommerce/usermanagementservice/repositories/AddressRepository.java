package com.ecommerce.usermanagementservice.repositories;

import com.ecommerce.usermanagementservice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    void deleteByIdAndUserId(Long addressId, Long userId);
}
