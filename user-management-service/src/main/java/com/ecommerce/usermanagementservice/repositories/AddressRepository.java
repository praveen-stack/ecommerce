package com.ecommerce.usermanagementservice.repositories;

import com.ecommerce.usermanagementservice.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    void deleteByIdAndUserId(Long addressId, Long userId);

    List<Address> findByUserId(Long userId);
}
