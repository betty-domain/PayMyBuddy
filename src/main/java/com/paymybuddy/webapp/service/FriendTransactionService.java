package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.IncomingTransactionDto;
import com.paymybuddy.webapp.dto.TransactionDto;
import com.paymybuddy.webapp.dto.TransactionDtoMapper;
import com.paymybuddy.webapp.model.Fee;
import com.paymybuddy.webapp.model.Friendship;
import com.paymybuddy.webapp.model.FunctionalException;
import com.paymybuddy.webapp.model.PayMyBuddyConstants;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.repository.FeeRepository;
import com.paymybuddy.webapp.repository.FriendTransactionRepository;
import com.paymybuddy.webapp.repository.UserRepository;
import com.paymybuddy.webapp.utils.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class FriendTransactionService implements IFriendTransactionService {

    private static final Logger logger = LogManager.getLogger(FriendTransactionService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendTransactionRepository transactionRepository;

    @Autowired
    private FeeRepository feeRepository;

    @Autowired
    DateUtils dateUtils;

    @Autowired
    TransactionDtoMapper transactionDtoMapper;

    @Override
    public TransactionDto transferToFriend(final IncomingTransactionDto incomingTransactionDto) throws FunctionalException {
        String errorKey = "transaction.save.error: ";

        //on crée la transaction à partir des informations fournies, si possible
        Transaction transactionToSave = prepareTransactionToSave(incomingTransactionDto, errorKey);

        // on calcule la taxe
        Fee feeToSave = createFee(transactionToSave);

        //on calcule le montant TTC de la transaction
        BigDecimal totalTransactionAmount = feeToSave.getAmount().add(transactionToSave.getAmount());

        // on vérifie que le solde de l'utilisateur est suffisant pour réaliser la transaction
        if (feeToSave.getUser().getBalance().compareTo(totalTransactionAmount) >= 0) {
            //solde suffisant --> on peut sauvegarder la transaction, la taxe et mettre à jour les soldes des payers et bénéficiaires

            //on débite le payeur
            feeToSave.getUser().setBalance(feeToSave.getUser().getBalance().subtract(totalTransactionAmount));

            //on crédite le bénéficiaire
            feeToSave.getTransaction().getBeneficiary().setBalance(feeToSave.getTransaction().getBeneficiary().getBalance().add(feeToSave.getTransaction().getAmount()));

            //on enregistre la taxe, la transaction, le bénéficiaire et le payeur
            try {
                transactionRepository.save(transactionToSave);
                Fee createdFee = feeRepository.save(feeToSave);

                //TODO : à revoir avec Alexandre : est ce mieux de faire un map from l'entité retournée en retour du save et cella passée e paramètre, notamment pour la testatibilité de l'application ? --> obliger de tester les montantss, soldes ... en IT et non en TU
                return transactionDtoMapper.mapFromFee(createdFee);
            }
            catch (Exception exception)
            {
                logger.error(errorKey + "Erreur durant l'enregsitrement de la taxe, transaction et soldes utilisateurs : " + exception.getStackTrace());
                throw new FunctionalException(errorKey + "Erreur lors de l'enregistrement");
            }

        } else {
            logger.info(errorKey + " Solde de l'utilisateur insuffisant pour réaliser la transaction");
            throw new FunctionalException(errorKey + "Solde insuffisant pour réaliser la transaction avec les frais");
        }
    }

    /**
     * Vérifie les données fourni et l'existence des utilisateurs pour créer un objet transaction
     *
     * @param incomingTransactionDto données reçues pour créer une transaction
     * @param errorKey               préfixe pour les exceptions éventuelles
     * @return transaction préparée pour la sauvegarde avec les données saisies ou récupérées (payer et bénéficiaire)
     * @throws FunctionalException
     */
    private Transaction prepareTransactionToSave(IncomingTransactionDto incomingTransactionDto, String errorKey) throws FunctionalException {
        if (incomingTransactionDto == null || !incomingTransactionDto.isValid()) {
            logger.info(errorKey + "données incorrectes : nulles ou invalides");
            throw new FunctionalException("Données incorrectes");
        } else {
            Optional<User> existingPayer = userRepository.findById(incomingTransactionDto.getPayerId());
            if (existingPayer.isPresent()) {
                //le paueyr est connu du système
                Optional<User> existingBeneficiary = userRepository.findById(incomingTransactionDto.getBeneficiaryId());
                if (existingBeneficiary.isPresent()) {
                    //le bénéficiaire est connu du système
                    Optional<Friendship> existingFriendship = existingPayer.get().getFriendshipList().stream().filter(friendship ->
                            friendship.getAmi().getId().equals(incomingTransactionDto.getBeneficiaryId()) &&
                                    friendship.getUser().getId().equals(incomingTransactionDto.getPayerId())).findFirst();

                    if (existingFriendship.isPresent()) {
                        //les deux personnes sont des amis , on initialise un objet Transaction avec les données fournies en entrée
                        Transaction transaction = new Transaction();
                        transaction.setBeneficiary(existingBeneficiary.get());
                        transaction.setPayer(existingPayer.get());
                        transaction.setAmount(incomingTransactionDto.getAmount());
                        transaction.setDescription(incomingTransactionDto.getDescription());
                        transaction.setDate(dateUtils.getNowLocalDate());
                        return transaction;
                    } else {
                        logger.info(errorKey + "le payeur et le bénéificiare ne sont pas des amis, impossible de faire une transaction entre eux ");
                        throw new FunctionalException("Cette personne n'est pas un ami du payeur");
                    }
                } else {
                    logger.info(errorKey + "utilisateur Beneficiary inexistant ");
                    throw new FunctionalException("Bénéficiaire inexistant");
                }
            } else {
                logger.info(errorKey + "utilisateur Payer inexistant ");
                throw new FunctionalException("Payeur inexistant");
            }
        }
    }

    /**
     * création de la taxe sur la transaction fourni
     *
     * @param transactionToFee transaction à taxer
     * @return taxe générée
     */
    private Fee createFee(Transaction transactionToFee) {

        Fee fee = new Fee();
        fee.setPercentage100(PayMyBuddyConstants.FEE_PERCENTAGE100);
        fee.setDate(transactionToFee.getDate());
        fee.setUser(transactionToFee.getPayer());
        fee.setTransaction(transactionToFee);
        fee.setAmount(transactionToFee.getAmount().multiply(PayMyBuddyConstants.FEE_PERCENTAGE100.divide(new BigDecimal(100))));

        return fee;
    }
}
