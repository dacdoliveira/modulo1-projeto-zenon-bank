package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;

import java.nio.file.Path;
import java.util.List;

public class DBMainBatchPath {
    static void main() {
        TransactionRepository repository = new TransactionSQLRepository();
        long timeIni = System.nanoTime();
       // repository.saveBatch(Path.of("./data/arquivo.csv"));
        repository.saveBatch(Path.of("./data/arquivo.csv"), 10000);
        long timeFim = System.nanoTime();

        System.out.println("Insert em lote durou: " + (timeFim - timeIni) + " nanoSegundos");
    }
}
