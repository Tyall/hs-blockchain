package blockchain;

import blockchain.miners.MinerManager;
import blockchain.transactions.TransactionSender;

import static blockchain.config.SimulationConfig.*;
import static blockchain.utils.SimulationUtils.waitForSimulationToProgress;

public class Main {

    public static void main(String[] args) {
        final Blockchain blockchain = Blockchain.getInstance();

        MinerManager minerManager = new MinerManager();
        minerManager.createAndRunDefault(NUMBER_OF_MINERS);

        TransactionSender sender = new TransactionSender();
        sender.simulate(TRANSACTION_DELAY_MS, TRANSACTION_INTERVAL_MS);

        while (blockchain.getGeneratedBlocks().size() < NUMBER_OF_BLOCKS_TO_GENERATE) {
            waitForSimulationToProgress(100);
        }

        blockchain.printBlocksInfoInRange(0, NUMBER_OF_BLOCKS_TO_GENERATE);
    }

}
