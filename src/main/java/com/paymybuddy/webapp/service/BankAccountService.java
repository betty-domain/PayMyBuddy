package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDto;
import com.paymybuddy.webapp.dto.BankAccountDtoMapper;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BankAccountService implements IBankAccountService {

    private static final Logger logger = LogManager.getLogger(BankAccountService.class);

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountDtoMapper bankAccountDtoMapper;

    @Override
    public BankAccount addBankAccount(final BankAccountDto bankAccountDto) throws FunctionalException {
        String errorKey = "bankAccount.add.error :";

        if (isPossibleToSaveBankAccount(bankAccountDto, errorKey)) {
            return bankAccountRepository.save(bankAccountDtoMapper.mapToBankAccount(bankAccountDto));
        } else {
            logger.error("Impossible d'enregistrer le compte bancaire");
            return null;
        }
    }

    /**
     * Vérifie les conditions nécessaires à l'enregistrement d'un BankAccount
     *
     * @param bankAccountDto
     * @param errorKey
     * @return
     */
    private boolean isPossibleToSaveBankAccount(BankAccountDto bankAccountDto, String errorKey) {
        if (bankAccountDto != null && bankAccountDto.isValid()) {
            logger.info("Vérification que l'utilisateur existe");
            Optional<User> existingUser = userRepository.findById(bankAccountDto.getUserId());
            if (existingUser.isPresent()) {
                logger.info("Vérification que l'iban n'existe pas déjà pour l'utilisateur");
                Optional<BankAccount> existingBankAccount = bankAccountRepository.findByIbanAndUser_Id(bankAccountDto.getIban(), bankAccountDto.getUserId());
                if (existingBankAccount.isPresent()) {
                    throw new FunctionalException(errorKey + "Iban déjà existant pour cet utilisateur");
                } else {
                    return true;
                }
            } else {
                throw new FunctionalException(errorKey + "Utilisateur inconnu");
            }
        } else {
            throw new FunctionalException(errorKey + " Données incorrectes");
        }

    }

    @Override
    public boolean deleteBankAccount(final Integer bankAccountId) throws FunctionalException {
        String errorKey = "bankAccount.delete.error";
        if (bankAccountId == null) {
            throw new FunctionalException(errorKey+"Données incorrectes");
        }
        else
        {
            bankAccountRepository.deleteById(bankAccountId);
            return true;
        }
    }
}
