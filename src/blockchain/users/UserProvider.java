package blockchain.users;

import blockchain.Blockchain;
import blockchain.miners.Miner;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static blockchain.config.SimulationConfig.USER_GEN_PREFIXES;

public class UserProvider<T extends User> {

    private final Class<T> type;

    private final SecureRandom secureRandom = new SecureRandom();

    private final Blockchain blockchain;

    private final AtomicInteger transactionId;

    public UserProvider(Class<T> type) {
        this.type = type;
        this.blockchain = Blockchain.getInstance();
        this.transactionId = new AtomicInteger(0);
    }

    public T getUser() {
        T user = generateByType(type);
        blockchain.registerNewUser(user);
        return user;
    }

    @SuppressWarnings("unchecked")
    private T generateByType(Class<T> type) {

        int userId = transactionId.getAndIncrement();
        String username = generateUsername();

        if (type.isAssignableFrom(Miner.class)) {
            return (T) new Miner(userId, username);
        }

        return (T) new User(userId, username);
    }

    private String generateUsername() {
        return USER_GEN_PREFIXES.get(secureRandom.nextInt(USER_GEN_PREFIXES.size())) + secureRandom.nextInt(99);
    }

}
