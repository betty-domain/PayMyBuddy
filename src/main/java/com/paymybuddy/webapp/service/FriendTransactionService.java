package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class FriendTransactionService implements IFriendTransactionService{
    @Override
    public TransactionDto transferToFriend(final IncomingTransactionDto incomingTransactionDto) {
        return null;
    }
}
