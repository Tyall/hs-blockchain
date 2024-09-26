package blockchain.transactions;

import java.util.concurrent.atomic.AtomicInteger;

import static blockchain.utils.ValidationUtils.returnValidationFalseWithMessage;

public class TransactionValidator {

    public static synchronized boolean isValid(Transaction transaction, AtomicInteger controlNumber) {

        if (transaction.getId() < 0) {
            return returnValidationFalseWithMessage("Transaction invalid. Wrong message id");
        }

        if (transaction.getSender().isInvalid()) {
            return returnValidationFalseWithMessage("Transaction invalid. Sender isn't valid");
        }

        if (!isAmountValid(transaction)) {
            return returnValidationFalseWithMessage("Transaction invalid. Amount isn't valid");
        }

        if (transaction.getReceiver().isInvalid()) {
            return returnValidationFalseWithMessage("Transaction invalid. Receiver isn't valid");
        }

        if (controlNumber.get() <= transaction.getId()) {
            return returnValidationFalseWithMessage("Transaction invalid. Blockchain's continuity of transaction was interrupted!");
        }

        return true;
    }

    private static synchronized boolean isAmountValid(Transaction transaction) {

        if (transaction.getAmount() < 0) {
            return returnValidationFalseWithMessage("Transaction amount invalid. Amount is negative");
        }

        if (transaction.getSender().getBalance() < transaction.getAmount()) {
            return returnValidationFalseWithMessage("Transaction amount invalid. Sender doesn't have enough VC");
        }

        return true;
    }

    private TransactionValidator() {
        throw new IllegalArgumentException("Cannot instantiate utility class");
    }

}
