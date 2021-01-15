package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.BankAccountDtoMapper;
import com.paymybuddy.webapp.dto.BankTransferDto;
import com.paymybuddy.webapp.dto.BankTransferDtoMapper;
import com.paymybuddy.webapp.dto.BankTransferListDto;
import com.paymybuddy.webapp.dto.BankTransferListDtoMapper;
import com.paymybuddy.webapp.model.BankAccount;
import com.paymybuddy.webapp.model.BankTransfer;
import com.paymybuddy.webapp.model.BankTransferOrder;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.BankAccountRepository;
import com.paymybuddy.webapp.repository.BankTransferRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankTransferService implements IBankTransferService {
    private static final Logger logger = LogManager.getLogger(BankTransferService.class);

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Autowired
    private BankTransferDtoMapper bankTransferDtoMapper;

    @Autowired
    private BankAccountDtoMapper bankAccountDtoMapper;

    @Autowired
    private BankTransferListDtoMapper bankTransferListDtoMapper;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BankTransferDto transferFromBank(final BankTransferDto bankTransferDto) throws FunctionalException {
        String errorKey = "bankTransfer.transferFromBank.error : ";
        if (bankTransferDto != null && bankTransferDto.isValid()) {
            logger.info("Recherche du compte bancaire actif associé à l'id du compte et de l'utilisateur fourni");
            Optional<BankAccount> existingBankAccount = bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankTransferDto.getBankAccountId(), bankTransferDto.getUserId());
            if (existingBankAccount.isPresent()) {
                //on ajoute le montant du transfert au compte de l'utilisateur
                existingBankAccount.get().getUser().setBalance(existingBankAccount.get().getUser().getBalance().add(bankTransferDto.getAmount()));

                BankTransfer bankTransferToSave = bankTransferDtoMapper.mapToBankTransfer(bankTransferDto, existingBankAccount.get());
                bankTransferToSave.setTransferOrder(BankTransferOrder.FROM_BANK);

                //on enregistre le transfert et l'utilisateur sera mis à jour en même temps
                logger.info("Sauvegarde du transfert d'argent et mise à jour du solde de l'utilisateur");
                try {
                    BankTransfer savedBankTransfer = bankTransferRepository.save(bankTransferToSave);
                    return bankTransferDtoMapper.mapFromBankTransfer(savedBankTransfer);
                } catch (Exception exception) {
                    logger.error(errorKey + "Erreur lors de l'enregistrement : " + exception.getStackTrace());
                    throw new FunctionalException(errorKey + "Erreur lors de l'enregistrement");
                }
            } else {
                logger.info(errorKey + "Compte inexistant pour cet utilisateur");
                throw new FunctionalException(errorKey + "Compte inexistant pour cet utilisateur");
            }
        } else {
            logger.info(errorKey + "Données incorrectes");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }

    @Override
    public BankTransferDto transferToBank(final BankTransferDto bankTransferDto) throws FunctionalException {
        String errorKey = "bankTransfer.transferToBank.error : ";
        if (bankTransferDto != null && bankTransferDto.isValid()) {
            logger.info("Recherche du compte bancaire associé à l'id du compte et de l'utilisateur fourni");
            Optional<BankAccount> existingBankAccount = bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankTransferDto.getBankAccountId(), bankTransferDto.getUserId());
            if (existingBankAccount.isPresent()) {
                // on vérifie que le solde du compte est suffisant pour faire le transfert demandé
                logger.info("Vérification du solde de l'utilisateur avant Transfer vers un compte bancaire");
                User actualUser = existingBankAccount.get().getUser();
                BigDecimal newBalance = actualUser.getBalance().subtract(bankTransferDto.getAmount());
                if (newBalance.compareTo(BigDecimal.ZERO) > 0) {
                    //on met à jour le solde de l'utilisateur
                    actualUser.setBalance(newBalance);

                    BankTransfer bankTransferToSave = bankTransferDtoMapper.mapToBankTransfer(bankTransferDto, existingBankAccount.get());
                    bankTransferToSave.setTransferOrder(BankTransferOrder.TO_BANK);

                    //on enregistre le transfert et l'utilisateur sera mis à jour en même temps
                    logger.info("Sauvegarde du transfert d'argent et mise à jour du solde de l'utilisateur");
                    try {
                        BankTransfer savedBankTransfer = bankTransferRepository.save(bankTransferToSave);
                        return bankTransferDtoMapper.mapFromBankTransfer(savedBankTransfer);
                    } catch (Exception exception) {
                        logger.error(errorKey + "Erreur lors de l'enregistrement : " + exception.getStackTrace());
                        throw new FunctionalException(errorKey + "Erreur lors de l'enregistrement");
                    }

                } else {
                    logger.info(errorKey + "Solde utilisateur insuffisant");
                    throw new FunctionalException("Solde Insuffisant");
                }
            } else {
                logger.info(errorKey + "Compte inexisant pour cet utilisateur");
                throw new FunctionalException(errorKey + "Compte inexistant pour cet utilisateur");
            }
        } else {
            logger.info(errorKey + "Données incorrectes");
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }

    @Override
    public BankTransferListDto getAllTransferForUser(final Integer userId) throws FunctionalException {
        String errorKey = "bankTransfer.get.error: ";
        if (userId != null) {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {

                BankTransferListDto bankTransferListDto = bankTransferListDtoMapper.mapToBankTransferListDto(existingUser.get());

                //on récupère la liste des comptes bancaires pour lesquels il y a eu des transferts d'argent
                List<BankAccount> bankAccountList = existingUser.get().getBankAccountList().stream().filter(
                        bankAccount -> bankAccount.getBankTransferList() != null && bankAccount.getBankTransferList().size() > 0
                ).collect(Collectors.toList());

                //pour chaque compte bancaire, on trie les transferts bancaires par date
                bankAccountList.stream().forEach(bankAccount -> {
                    bankAccount.getBankTransferList().stream().sorted((object1, object2) -> object2.getDate().compareTo(object1.getDate()));
                });

                bankTransferListDto.setBankAccountDtoList(bankAccountDtoMapper.mapListFromBankAccountList(bankAccountList));

                return bankTransferListDto;
            } else {
                logger.info(errorKey + "Utilisateur inexistant");
                throw new FunctionalException(errorKey + "Utilisateur inexistant");
            }
        } else {
            logger.info(errorKey + "Données invalides");
            throw new FunctionalException(errorKey + "Données invalides");
        }
    }
}
