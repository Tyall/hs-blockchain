package blockchain;

import blockchain.transactions.Transaction;
import blockchain.users.User;
import blockchain.utils.SignatureUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static blockchain.config.SimulationConfig.DIFFICULTY_TIME_THRESHOLD;

public class Blockchain {

    private static final Blockchain INSTANCE = new Blockchain();

    List<Block> generatedBlocks;
    List<Transaction> transactions;
    List<Transaction> awaitingTransactions;
    List<User> registeredUsers;
    AtomicInteger hashDifficulty;
    AtomicInteger transactionControlNumber;

    private Blockchain() {
        this.generatedBlocks = Collections.synchronizedList(new ArrayList<>());
        this.transactions = Collections.synchronizedList(new ArrayList<>());
        this.awaitingTransactions = Collections.synchronizedList(new ArrayList<>());
        this.registeredUsers = Collections.synchronizedList(new ArrayList<>());
        this.hashDifficulty = new AtomicInteger(0);
        this.transactionControlNumber = new AtomicInteger(1);
    }

    public static Blockchain getInstance() {
        return INSTANCE;
    }

    public synchronized void submitBlock(Block block) {
        if (block.isHashValid() && isPrevHashValid(block) && areTransactionsNotRegisteredYet(block.getTransactions())) {
            generatedBlocks.add(block);
            removeConfirmedTransactionsFromAwaiting(block.getTransactions());
            transactions.addAll(block.getTransactions());
            verifyDifficulty(block);
        }
    }

    private boolean areTransactionsNotRegisteredYet(List<Transaction> incomingTransactions) {
        return Collections.disjoint(transactions, incomingTransactions);
    }

    public synchronized void addTransaction(Transaction transaction) {
        if (transaction != null && transaction.isValid(transactionControlNumber) && isTransactionSecure(transaction)) {
            transaction.getSender().addTransaction(transaction);
            transaction.getReceiver().addTransaction(transaction);
            awaitingTransactions.add(transaction);
            transactionControlNumber.incrementAndGet();
        }
    }

    private synchronized boolean isTransactionSecure(Transaction transaction) {
        return SignatureUtils.verifySignature(transaction.getTransactionHash().getBytes(), transaction.getSignature(), transaction.getSender().getPublicKey());
    }

    private synchronized void verifyDifficulty(Block block) {
        if (block.getGenerationTime() > DIFFICULTY_TIME_THRESHOLD && hashDifficulty.get() > 0) {
            hashDifficulty.decrementAndGet();
        } else if (block.getGenerationTime() <  DIFFICULTY_TIME_THRESHOLD) {
            hashDifficulty.incrementAndGet();
        }
    }

    public synchronized int getHashDifficulty() {
        return hashDifficulty.get();
    }

    private synchronized boolean isPrevHashValid(Block block) {
        return generatedBlocks.isEmpty() || Objects.equals(generatedBlocks.get(generatedBlocks.size() - 1).getHash(), block.getPreviousBlockHash());
    }

    public boolean isValid() {
        if (generatedBlocks.isEmpty()){
            return true;
        } else if (generatedBlocks.size() == 1) {
            return generatedBlocks.get(0).isHashValid();
        }

        for (int i = 1; i < generatedBlocks.size(); i++) {
            if (!generatedBlocks.get(i).isHashValid() ||
                !Objects.equals(generatedBlocks.get(i - 1).getHash(), generatedBlocks.get(i).getPreviousBlockHash())) {
                return false;
            }
        }

        return true;
    }

    public synchronized List<Block> getGeneratedBlocks() {
        return generatedBlocks;
    }

    public synchronized List<User> getRegisteredUsers() {
        return registeredUsers;
    }

    public void registerNewUser(User user) {
        if (!registeredUsers.contains(user)) {
            registeredUsers.add(user);
        } else {
            System.out.printf("User with id %d and name %s already exists!%n", user.getId(), user.getName());
        }

    }

    public synchronized List<Transaction> getAwaitingTransactionsCopy() {
        return List.copyOf(awaitingTransactions);
    }

    private synchronized void removeConfirmedTransactionsFromAwaiting(List<Transaction> confirmedTransactions) {
        awaitingTransactions.removeAll(confirmedTransactions);
    }

    public void printBlocksInfoInRange(int startingId, int endingId) {
        generatedBlocks.stream()
                .skip(startingId)
                .limit(endingId)
                .forEach(System.out::println);
    }
}
