package blockchain;

import blockchain.miners.Miner;
import blockchain.transactions.Transaction;
import blockchain.utils.StringUtils;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static blockchain.config.SimulationConfig.BLOCK_MINED_REWARD;
import static blockchain.config.SimulationConfig.DIFFICULTY_TIME_THRESHOLD;

public class Block {

    private static final SecureRandom secureRandom = new SecureRandom();

    private final Blockchain blockchain;
    private final Long id;
    private final Miner miner;
    private final Long timestamp;
    private final String previousBlockHash;
    private final String hash;
    private final int magicNumber;
    private final Long generationTime;
    private final int difficulty;
    private final List<Transaction> transactions;

    public Block(Miner miner) {
        long generationStartTime = System.nanoTime();

        this.blockchain = Blockchain.getInstance();
        this.miner = miner;
        this.timestamp = new Date().getTime();

        BlockchainDynamicValuesWrapper dynamicValuesWrapper = new BlockchainDynamicValuesWrapper();
        do {
            dynamicValuesWrapper.updateValues();
        } while (!isProved(dynamicValuesWrapper));

        this.id = dynamicValuesWrapper.getId();
        this.previousBlockHash = dynamicValuesWrapper.getPrevBlockHash();
        this.difficulty = dynamicValuesWrapper.getDifficulty();
        this.transactions = dynamicValuesWrapper.getTransactions();
        this.magicNumber = dynamicValuesWrapper.getMagicNumber();

        this.hash = calculateHash();

        this.generationTime = TimeUnit.SECONDS.convert(System.nanoTime() - generationStartTime, TimeUnit.NANOSECONDS);
    }

    private boolean isProved(BlockchainDynamicValuesWrapper values) {
        String calculatedHash = calculateHash(values);
        return calculatedHash.substring(0, values.getDifficulty()).matches("^0*$");
    }

    private String calculateHash(BlockchainDynamicValuesWrapper valuesWrapper) {
        return StringUtils.applySha256(
                valuesWrapper.getMagicNumber() +
                timestamp +
                valuesWrapper.getPrevBlockHash() +
                valuesWrapper.getDifficulty() +
                miner.getId() +
                valuesWrapper.getId() +
                getTransactionHashesCombinedAsString(valuesWrapper.getTransactions()));
    }

    private String calculateHash() {
        return StringUtils.applySha256(
                magicNumber +
                        timestamp +
                        previousBlockHash +
                        difficulty +
                        miner.getId() +
                        id +
                        getTransactionHashesCombinedAsString(transactions));
    }

    public boolean isHashValid() {
        return Objects.equals(hash, calculateHash());
    }

    private String getTransactionHashesCombinedAsString(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getTransactionHash)
                .collect(Collectors.joining());
    }

    public Miner getMiner() {
        return miner;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public Long getGenerationTime() {
        return generationTime;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return """
                Block:
                Created by: miner%d
                miner%d gets %s VC
                Id: %d
                Timestamp: %d
                Magic number: %d
                Hash of the previous block:
                %s
                Hash of the block:
                %s
                Block data: %s
                Block was generating for %d seconds
                N %s
                """
                .formatted(
                        this.miner.getId(),
                        this.miner.getId(),
                        BLOCK_MINED_REWARD,
                        this.id,
                        this.timestamp,
                        this.magicNumber,
                        this.previousBlockHash,
                        this.hash,
                        getBlockData(),
                        this.generationTime,
                        getDifficultyChangeMessage()
                );
    }

    private String getBlockData() {
        if (transactions.isEmpty()) {
            return "no transactions";
        } else {
            return "\n" + transactions.stream().map(Transaction::toString).collect(Collectors.joining("\n"));
        }
    }

    private String getDifficultyChangeMessage() {
        StringBuilder msg = new StringBuilder();
        if (generationTime > DIFFICULTY_TIME_THRESHOLD && difficulty > 0) {
            msg.append("was decreased to ").append(difficulty - 1);
        } else if (generationTime <  DIFFICULTY_TIME_THRESHOLD) {
            msg.append("was increased to ").append(difficulty + 1);
        } else {
            msg.append("stays the same");
        }
        return msg.toString();
    }

    private class BlockchainDynamicValuesWrapper {

        long id;
        String prevBlockHash;
        int difficulty;
        List<Transaction> transactions;
        int magicNumber;

        public BlockchainDynamicValuesWrapper() {
            updateValues();
        }

        public void updateValues() {
            this.id = blockchain.getGeneratedBlocks().size() + 1L;
            this.prevBlockHash = blockchain.getGeneratedBlocks().isEmpty() ? "0" :
                    blockchain.getGeneratedBlocks().get(blockchain.getGeneratedBlocks().size() - 1).getHash();
            this.difficulty = blockchain.getHashDifficulty();
            this.transactions = blockchain.getAwaitingTransactionsCopy();
            this.magicNumber = secureRandom.nextInt(Integer.MAX_VALUE);
        }

        public long getId() {
            return id;
        }

        public String getPrevBlockHash() {
            return prevBlockHash;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public int getMagicNumber() {
            return magicNumber;
        }
    }

}
