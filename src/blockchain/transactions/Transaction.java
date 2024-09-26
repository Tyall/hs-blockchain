package blockchain.transactions;

import blockchain.utils.SignatureUtils;
import blockchain.users.User;
import blockchain.utils.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class Transaction {

    private final int id;
    private User sender;
    private final User receiver;
    private final long amount;
    private final byte[] signature;

    public Transaction(int id, User receiver, long amount) {
        this.id = id;
        this.receiver = receiver;
        this.amount = amount;
        this.signature = SignatureUtils.sign(getTransactionHash(), sender.getPrivateKey());
    }

    public Transaction(int id, User sender, User receiver, long amount) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.signature = SignatureUtils.sign(getTransactionHash(), sender.getPrivateKey());
    }

    public int getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public long getAmount() {
        return amount;
    }

    public byte[] getSignature() {
        return signature;
    }

    public String getTransactionHash() {
        return StringUtils.applySha256(id + sender.getName() + sender.getBalance() + amount + receiver.getName() + receiver.getBalance());
    }

    public boolean isValid(AtomicInteger transactionControlNumber) {
        return TransactionValidator.isValid(this, transactionControlNumber);
    }

    @Override
    public String toString() {
        return "%s sent %d VC to %s".formatted(sender.getName(), amount, receiver.getName());
    }

}
