package blockchain.miners;

import blockchain.Blockchain;
import blockchain.users.UserProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static blockchain.config.SimulationConfig.NUMBER_OF_BLOCKS_TO_GENERATE;
import static blockchain.config.SimulationConfig.NUMBER_OF_MINERS;
import static blockchain.utils.SimulationUtils.waitForSimulationToProgress;

public class MinerManager {

    private final ExecutorService executor;

    public MinerManager() {
        this.executor = Executors.newFixedThreadPool(NUMBER_OF_MINERS);
    }

    public void createAndRunDefault(int numberOfMiners) {
        createAndRun(numberOfMiners, MiningMode.THRESHOLD, NUMBER_OF_BLOCKS_TO_GENERATE);
    }

    public void createAndRun(int numberOfMiners, MiningMode mode, int param) {

        UserProvider<Miner> minerProvider = new UserProvider<>(Miner.class);

        for (int i = 0; i < numberOfMiners; i++) {
            executor.submit(() -> {
                Miner miner = minerProvider.getUser();

                waitForSimulationToProgress(ThreadLocalRandom.current().nextInt(50));

                miner.mine(mode, param);
            });
        }

        while (Blockchain.getInstance().getGeneratedBlocks().size() < NUMBER_OF_BLOCKS_TO_GENERATE) {
            waitForSimulationToProgress(100);
        }

        executor.shutdownNow();
    }

}
