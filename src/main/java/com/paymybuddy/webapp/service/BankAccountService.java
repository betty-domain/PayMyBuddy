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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public BankAccountDto addBankAccount(final BankAccountDto bankAccountDto) throws FunctionalException {
        String errorKey = "bankAccount.add.error :";

        if (bankAccountDto != null && bankAccountDto.isValid()) {
            logger.info("Vérification que l'utilisateur existe");
            Optional<User> existingUser = userRepository.findById(bankAccountDto.getUserId());
            if (existingUser.isPresent()) {
                logger.info("Vérification que l'iban n'existe pas déjà pour l'utilisateur");
                Optional<BankAccount> existingBankAccount = bankAccountRepository.findByIbanAndUser_Id(bankAccountDto.getIban(), bankAccountDto.getUserId());
                if (existingBankAccount.isPresent() && existingBankAccount.get().isActif()) {
                    logger.info(errorKey + "Iban déjà existant pour cet utilisateur");
                    throw new FunctionalException(errorKey + "Iban déjà existant pour cet utilisateur");
                } else {
                    BankAccount savedBankAccount = null;
                    if (existingBankAccount.isPresent() && !existingBankAccount.get().isActif()) {//si le compte avait été désactivé, on va le réactiver
                        savedBankAccount = existingBankAccount.get();
                        savedBankAccount.setActif(true);
                        bankAccountDtoMapper.updateBankAccountFromBankAccountDto(bankAccountDto,savedBankAccount);
                        bankAccountRepository.save(savedBankAccount);
                    } else {
                        savedBankAccount = bankAccountRepository.save(bankAccountDtoMapper.mapToBankAccount(bankAccountDto, existingUser.get()));
                    }
                    return bankAccountDtoMapper.mapFromBankAccount(savedBankAccount);
                }
            } else {
                logger.info(errorKey + "Utilisateur inconnu");
                throw new FunctionalException(errorKey + "Utilisateur inconnu");
            }
        } else {
            logger.info(errorKey + "Données incorrectes");
            throw new FunctionalException(errorKey + " Données incorrectes");
        }
    }

    @Override
    public boolean desactivateBankAccount(final Integer bankAccountId) throws FunctionalException {
        String errorKey = "bankAccount.delete.error :";
        if (bankAccountId == null) {
            throw new FunctionalException(errorKey + "Données incorrectes");
        } else {
            Optional<BankAccount> existingBankAccount = bankAccountRepository.findById(bankAccountId);
            if (existingBankAccount.isPresent()) {
                existingBankAccount.get().setActif(false);
                BankAccount updatingBankAccount = bankAccountRepository.save(existingBankAccount.get());
                return true;
            } else {
                logger.info(errorKey + "Compte bancaire inexistant");
                throw new FunctionalException(errorKey + "Compte bancaire inexistant");
            }

        }
    }

    @Override
    public List<BankAccountDto> getBankAccountListForUser(final Integer userId)  throws FunctionalException {
        String errorKey = "bankAccount.get.error: ";
        if (userId!=null) {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent())
            {
                List<BankAccount> activeBankAccountList = existingUser.get().getBankAccountList().stream().filter(
                        bankAccount -> bankAccount.isActif()
                ).collect(Collectors.toList());

                return bankAccountDtoMapper.mapListFromBankAccountList(activeBankAccountList);
            }
            else
            {
                logger.info(errorKey + "Utilisateur inexistant");
                throw new FunctionalException(errorKey + "Utilisateur inexistant");
            }
        }
        else
        {
            logger.info(errorKey + "Données invalides");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }
}
