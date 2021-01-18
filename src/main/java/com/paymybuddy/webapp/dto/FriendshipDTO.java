package com.paymybuddy.webapp.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;

@Data
@JsonView(DtoJsonView.Public.class)
public class FriendshipDTO {

    private UserDto user;

    private List<UserDto> friends;

}
