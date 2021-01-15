package com.paymybuddy.webapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class FriendshipDTO {

    private UserDto user;

    private List<UserDto> friends;

}
