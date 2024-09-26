package blockchain.users;

import blockchain.Blockchain;
import blockchain.transactions.Transaction;
import blockchain.utils.SignatureUtils;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static blockchain.config.SimulationConfig.BLOCK_MINED_REWARD;
import static blockchain.config.SimulationConfig.STARTING_BALANCE;

public class User {

    private final int id;
    private final String name;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final List<Transaction> transactions;

    public User(int id, String name) {
        this.id = id;
        this.name = name;

        KeyPair keyPair = SignatureUtils.getKeyPair();
        this.privateKey = keyPair == null ? null : keyPair.getPrivate();
        this.publicKey = keyPair == null ? null : keyPair.getPublic();

        this.transactions = Collections.synchronizedList(new ArrayList<>());
    }

    public boolean isInvalid() {
        return this.name.isEmpty() ||
                this.privateKey == null ||
                this.publicKey == null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized long getBalance() {
        long sent = transactions.stream()
                .filter(t -> t.getSender().equals(this))
                .filter(t -> t.getAmount() > 0)
                .mapToLong(Transaction::getAmount)
                .sum();

        long received = transactions.stream()
                .filter(t -> t.getReceiver().equals(this))
                .filter(t -> t.getAmount() > 0)
                .mapToLong(Transaction::getAmount)
                .sum();

        long blocksMined = Blockchain.getInstance().getGeneratedBlocks().stream()
                .filter(b -> b.getMiner().equals(this))
                .count();


        return (STARTING_BALANCE + (blocksMined * BLOCK_MINED_REWARD) + received - sent);
    }

    public synchronized void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

}
