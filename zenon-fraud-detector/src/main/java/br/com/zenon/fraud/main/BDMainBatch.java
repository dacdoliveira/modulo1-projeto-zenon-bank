package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;

import java.util.List;

public class BDMainBatch {
    static void main() {
        TransactionRepository repository = new TransactionSQLRepository();

        List<Transaction> transactionList = TransactionIngestor.extractTransaction("./data/arquivo.csv", 10000);
        int total = transactionList.size();
        int count = 1;
        System.out.println("total: " + total);
        long timeIni = System.nanoTime();
        repository.saveBatch(transactionList);
        long timeFim = System.nanoTime();

        System.out.println("Insert em lote durou: " + (timeFim - timeIni) + " nanoSegundos");
    }
}
