package com.paymybuddy.webapp.dto;

import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.utils.EncryptUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class UserDtoMapper {

    @Autowired
    private EncryptUtils encryptUtils;

    @Mappings({
            @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword"),
            @Mapping(target="balance", ignore = true)
    })
    public abstract User mapToUser(UserDto userDto);

    @Mappings({
            @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
    })
    public abstract void updateUserFromUserDto(UserDto userDto, @MappingTarget User user);

    @Named("encryptPassword")
    public String getEncryptPassword(String password) {

        return encryptUtils.encodePassword(password);
    }

    public abstract UserDto mapFromUser(User user);

    public abstract List<UserDto> mapFromUserList(List<User> user);

}
