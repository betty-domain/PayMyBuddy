package com.paymybuddy.webapp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendshipId implements Serializable {
    private User user;
    private User ami;

}
