package blockchain.transactions;

import blockchain.Blockchain;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static blockchain.config.SimulationConfig.NUMBER_OF_BLOCKS_TO_GENERATE;
import static blockchain.utils.SimulationUtils.waitForSimulationToProgress;

public class TransactionSender {

    private final Blockchain blockchain;
    private final TransactionProvider transactionProvider;

    public TransactionSender() {
        this.blockchain = Blockchain.getInstance();
        this.transactionProvider = new TransactionProvider(blockchain);
    }

    public void simulate(int delay, int interval) {
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(
                this::getAndSubmitTransaction,
                delay,
                interval,
                TimeUnit.MILLISECONDS);


        while (blockchain.getGeneratedBlocks().size() < NUMBER_OF_BLOCKS_TO_GENERATE) {
            waitForSimulationToProgress(100);
        }

        scheduledExecutor.shutdownNow();

    }

    private void getAndSubmitTransaction() {
        blockchain.addTransaction(transactionProvider.getNewTransaction());
    }

}
