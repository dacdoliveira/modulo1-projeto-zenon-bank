package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.EfficientTransactionIngestor;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public class IngestionMain {
    static void main() {

        String pathName = "./data/arquivo.csv";
        TransactionRepository repository = new TransactionSQLRepository();

        Consumer<Transaction> saveTransaction = repository::save;
        Consumer<List<Transaction>> saveTransactionList = repository::saveTheBatch;

        EfficientTransactionIngestor efficientTransactionIngestor = new EfficientTransactionIngestor();

        long timeIni = System.nanoTime();
        try {
            //efficientTransactionIngestor.readAsStream(pathName, saveTransaction);
            efficientTransactionIngestor.readAsStreamBatch(pathName, saveTransactionList,10);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long timeFim = System.nanoTime();

        System.out.println("Insert durou: " + (timeFim - timeIni) + " nanoSegundos");
        int total = repository.getTotalTransactions().isPresent()?repository.getTotalTransactions().get():0;
        System.out.println("Total Transações: "+total);
        int totalFraud = repository.getTotalTransactionsFraud().isPresent()?repository.getTotalTransactionsFraud().get():0;
        System.out.println("Total Transações com fraudes: "+totalFraud);
        BigDecimal totalAmountFraud = repository.getTotalAmountFraud().isPresent()?repository.getTotalAmountFraud().get():null;
        System.out.println("Total valor das transações com fraudes: "+totalAmountFraud);
        BigDecimal totalAmount = repository.getTotalAmount().isPresent()?repository.getTotalAmount().get():null;
        System.out.println("Total valor das transações: "+totalAmount);
    }
}
