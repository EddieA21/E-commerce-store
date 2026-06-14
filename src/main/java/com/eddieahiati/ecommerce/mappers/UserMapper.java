package com.eddieahiati.ecommerce.mappers;

import com.eddieahiati.ecommerce.dtos.UpdateUserRequest;
import com.eddieahiati.ecommerce.dtos.UserDto;
import com.eddieahiati.ecommerce.dtos.UserRequest;
import com.eddieahiati.ecommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);
    UserRequest userRequest(User user);
    void update(UpdateUserRequest request, @MappingTarget User user);
}
