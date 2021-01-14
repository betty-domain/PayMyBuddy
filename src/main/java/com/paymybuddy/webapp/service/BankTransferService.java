package com.paymybuddy.webapp.service;

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

@Service
@Transactional
public class BankTransferService implements IBankTransferService {
    private static final Logger logger = LogManager.getLogger(BankTransferService.class);

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Autowired
    private BankTransferDtoMapper bankTransferDtoMapper;

    @Autowired
    private BankTransferListDtoMapper bankTransferListDtoMapper;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BankTransfer transferFromBank(final BankTransferDto bankTransferDto) throws FunctionalException {
        String errorKey = "bankTransfer.transferFromBank.error : ";
        if (bankTransferDto != null && bankTransferDto.isValid()) {
            logger.info("Recherche du compte bancaire associé à l'id du compte et de l'utilisateur fourni");
            Optional<BankAccount> existingBankAccount = bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankTransferDto.getBankAccountId(), bankTransferDto.getUserId());
            if (existingBankAccount.isPresent()) {
                //on ajoute le montant du transfert au compte de l'utilisateur
                existingBankAccount.get().getUser().setBalance(existingBankAccount.get().getUser().getBalance().add(bankTransferDto.getAmount()));

                BankTransfer bankTransferToSave = bankTransferDtoMapper.mapToBankTransfer(bankTransferDto, existingBankAccount.get());
                bankTransferToSave.setTransferOrder(BankTransferOrder.FROM_BANK);

                //on enregistre le transfert et l'utilisateur sera mis à jour en même temps
                logger.info("Sauvegarde du transfert d'argent et mise à jour du solde de l'utilisateur");
                BankTransfer savedBankTransfer = bankTransferRepository.save(bankTransferToSave);
                return savedBankTransfer;
            } else {
                throw new FunctionalException(errorKey + "Compte inexistant pour cet utilisateur");
            }
        } else {
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }

    @Override
    public BankTransfer transferToBank(final BankTransferDto bankTransferDto) throws FunctionalException {
        String errorKey = "bankTransfer.transferToBank.error : ";
        if (bankTransferDto != null && bankTransferDto.isValid()) {
            logger.info("Recherche du compte bancaire associé à l'id du compte et de l'utilisateur fourni");
            Optional<BankAccount> existingBankAccount = bankAccountRepository.findByIdAndIsActifTrueAndUser_id(bankTransferDto.getBankAccountId(), bankTransferDto.getUserId());
            if (existingBankAccount.isPresent()) {
                // on vérifie que le solde du compte est suffisant pour faire le transfert demandé
                User actualUser = existingBankAccount.get().getUser();
                BigDecimal newBalance = actualUser.getBalance().subtract(bankTransferDto.getAmount());
                if (newBalance.compareTo(BigDecimal.ZERO) > 0) {
                    //on met à jour le solde de l'utilisateur
                    actualUser.setBalance(newBalance);

                    BankTransfer bankTransferToSave = bankTransferDtoMapper.mapToBankTransfer(bankTransferDto, existingBankAccount.get());
                    bankTransferToSave.setTransferOrder(BankTransferOrder.TO_BANK);

                    //on enregistre le transfert et l'utilisateur sera mis à jour en même temps
                    logger.info("Sauvegarde du transfert d'argent et mise à jour du solde de l'utilisateur");
                    BankTransfer savedBankTransfer = bankTransferRepository.save(bankTransferToSave);
                    return savedBankTransfer;
                } else {
                    throw new FunctionalException("Solde Insuffisant");
                }
            } else {
                throw new FunctionalException(errorKey + "Compte inexistant pour cet utilisateur");
            }
        } else {
            throw new FunctionalException(errorKey + "Données incorrectes");
        }
    }

    @Override
    public BankTransferListDto getAllTransferForUser(final Integer userId) throws FunctionalException {

        //TODO : à compléter pour modifier l'objet retourné : User  liste des transferts et vérifier si l'utilisateur n'existe pas avant de retourner la liste
        String errorKey = "bankTransfer.get.error: ";
        if (userId != null) {
            Optional<User> existingUser = userRepository.findById(userId);
            if (existingUser.isPresent()) {

                //on récupère la liste des transferts bancaires de l'utilisateur
                List<BankTransfer> bankTransferList = existingUser.get().getBankTransferList();

                BankTransferListDto bankTransferListDto = bankTransferListDtoMapper.mapToBankTransferListDto(existingUser.get());

                //pour chaque compte bancaire, on trie les transferts bancaires par date
                existingUser.get().getBankAccountList().stream().forEach(bankAccount -> {
                    bankAccount.getBankTransferList().stream().sorted((object1, object2) -> object2.getDate().compareTo(object1.getDate()));
                });


                /*bankTransferListDto.getBankAccountDtoList().stream().forEach(bankAccountDtoIterator -> {
                    List<BankTransfer> bankTransferListForThisBankAccount = bankTransferList.stream()
                            .filter(bankTransferIterator -> bankTransferIterator.getBankAccount().getId().equals(bankAccountDtoIterator.getId()))
                            .sorted((object1, object2) -> object2.getDate().compareTo(object1.getDate())).collect(Collectors.toList());

                    bankAccountDtoIterator.setBankTransferDtoList(bankTransferDtoMapper.mapFromBankTransferList(bankTransferListForThisBankAccount));
                });*/

                return bankTransferListDto;
            } else {
                throw new FunctionalException(errorKey + "Utilisateur inexistant");
            }
        } else {
            throw new FunctionalException(errorKey + "Données invalides");
        }
    }
}
