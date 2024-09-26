package blockchain.transactions;

import blockchain.Blockchain;
import blockchain.users.User;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionProvider {

    private final Blockchain blockchain;
    private final AtomicInteger transactionId;

    public TransactionProvider() {
        this.transactionId = new AtomicInteger(0);
        this.blockchain = Blockchain.getInstance();
    }

    public TransactionProvider(Blockchain blockchain) {
        this.transactionId = new AtomicInteger(0);
        this.blockchain = blockchain;
    }

    public Transaction getNewTransaction() {
        return generate();
    }

    private Transaction generate() {
        if (blockchain.getRegisteredUsers().size() < 2) {
            return transactionError("Blockchain has too few users to generate transaction");
        }

        User sender = getUserWithExistingBalance();
        User receiver = getAnotherRandomUser(sender);

        if (sender == null || receiver == null) {
            return transactionError("Unable to find suitable sender or receiver among blockchain users");
        }

        long amount = ThreadLocalRandom.current().nextLong(1, sender.getBalance());
        return new Transaction(transactionId.getAndIncrement(), sender, receiver, amount);
    }

    private User getUserWithExistingBalance() {
        return blockchain.getRegisteredUsers().stream()
                .filter(u -> u.getBalance() > 0)
                .skip(ThreadLocalRandom.current().nextInt(blockchain.getRegisteredUsers().size()))
                .findFirst()
                .orElse(null);
    }

    private User getAnotherRandomUser(User user) {
        return blockchain.getRegisteredUsers().stream()
                .filter(u -> !u.equals(user))
                .skip(ThreadLocalRandom.current().nextInt(blockchain.getRegisteredUsers().size()))
                .findFirst()
                .orElse(null);
    }

    private Transaction transactionError(String message) {
        //System.out.println(message);
        return null;
    }

}
