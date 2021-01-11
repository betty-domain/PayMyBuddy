package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.EncryptUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class UserDtoMapper {

    @Autowired
    private EncryptUtils encryptUtils;

    @Mapping(target = "password", source="password", qualifiedByName = "encryptPassword")
    public abstract User mapToUser(UserDto userDto);

    @Named("encryptPassword")
    public String getEncryptPassword(String password) {

        return encryptUtils.encodePassword(password);
    }

}
