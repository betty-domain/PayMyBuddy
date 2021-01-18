package com.paymybuddy.webapp.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class FriendshipId implements Serializable {
    private Integer user;
    private Integer ami;

}
