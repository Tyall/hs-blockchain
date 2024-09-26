package blockchain.config;

import java.util.List;

public class SimulationConfig {

    public static final int DIFFICULTY_TIME_THRESHOLD = 0;
    public static final int NUMBER_OF_BLOCKS_TO_GENERATE = 15;

    public static final int NUMBER_OF_MINERS = 15;
    public static final int STARTING_BALANCE = 100;
    public static final int BLOCK_MINED_REWARD = 100;

    public static final List<String> USER_GEN_PREFIXES = List.of("Username", "David", "Alex", "Maria", "Anna",
            "Marco", "Antonio", "Daniel", "Andrea", "Laura", "Ali", "Jose", "Miner", "User", "Anonymous",
            "Guest", "Incognito", "Carlos", "FastFood", "CarShop", "Developer", "Hacker", "CryptoLover", "Chainer");

    public static final int TRANSACTION_DELAY_MS = 15;
    public static final int TRANSACTION_INTERVAL_MS = 150;

    private SimulationConfig() {
        throw new IllegalArgumentException("Cannot instantiate utility-config class");
    }
}
