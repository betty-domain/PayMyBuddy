package com.paymybuddy.webapp.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.paymybuddy.webapp.dto.DtoJsonView;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Entity @IdClass(FriendshipId.class)
@Table(name="user_friends")
public class Friendship {

    @NotNull
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @NotNull
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_friend_id", nullable = false)
    private User ami;

    @Column(name="is_actif")
    @JsonView(DtoJsonView.Private.class)
    private boolean isActif;

}
