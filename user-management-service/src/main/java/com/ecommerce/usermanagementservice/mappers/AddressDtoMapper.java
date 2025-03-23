package com.ecommerce.usermanagementservice.mappers;

import com.ecommerce.usermanagementservice.components.ObjectMapper;
import com.ecommerce.usermanagementservice.dtos.AddressDto;
import com.ecommerce.usermanagementservice.models.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressDtoMapper extends ObjectMapper<AddressDto, Address> {
    @Override
    public Class<AddressDto> getDtoClass() {
        return AddressDto.class;
    }

    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }
}
