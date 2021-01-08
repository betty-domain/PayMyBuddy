package com.paymybuddy.webapp.model;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity @IdClass(FriendshipId.class)
@Table(name="user_friends")
public class Friendship {

    @NotNull
    @Id
    @ManyToOne()
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @NotNull
    @Id
    @ManyToOne()
    @JoinColumn(name="user_friend_id", nullable = false)
    private User ami;

    @Column(name="is_actif")
    private boolean isActif;

}
