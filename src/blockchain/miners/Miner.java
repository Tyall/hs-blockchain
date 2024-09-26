package blockchain.miners;

import blockchain.Block;
import blockchain.Blockchain;
import blockchain.users.User;

import java.time.Duration;
import java.time.Instant;

public class Miner extends User {

    private final Blockchain blockchain;

    public Miner(int id, String name) {
        super(id, name);
        this.blockchain = Blockchain.getInstance();
    }

    public void mine(MiningMode mode, int param) {
        switch (mode) {
            case SINGLE -> mineSingleBlock();
            case THRESHOLD -> mineUntilThresholdReached(param);
            case SECONDS -> mineForGivenAmountOfTime(Duration.ofSeconds(param));
            case MINUTES -> mineForGivenAmountOfTime(Duration.ofMinutes(param));
            case HOURS -> mineForGivenAmountOfTime(Duration.ofHours(param));
        }
    }

    private void mineSingleBlock() {
        generateBlock();
    }

    private void mineUntilThresholdReached(int threshold) {
        while (blockchain.getGeneratedBlocks().size() < threshold) {
            generateBlock();
        }
    }

    private void mineForGivenAmountOfTime(Duration duration) {
        Instant start = Instant.now();
        while (Duration.between(start, Instant.now()).toMillis() <= duration.toMillis()) {
            generateBlock();
        }
    }

    private void generateBlock() {
        blockchain.submitBlock(new Block(this));
    }
}
