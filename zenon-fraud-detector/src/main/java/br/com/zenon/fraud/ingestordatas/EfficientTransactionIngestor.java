package br.com.zenon.fraud.ingestordatas;

import br.com.zenon.fraud.model.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class EfficientTransactionIngestor {

    private final static long LIMIT = 10_000;

    public void readAsStream(String pathName, Consumer<Transaction> transactionConsumer) throws IOException {

        try(BufferedReader reader = Files.newBufferedReader(Path.of(pathName))){
            reader.readLine(); //Pula nome de colunas
            String line;

            long count = 0;
            while (((line = reader.readLine()) != null) && count < LIMIT) {
                Optional<Transaction> transactionOp = TransactionIngestor.convertLineToTransaction(line);
                transactionOp.ifPresent(transactionConsumer);

                count++;

            }
        }
    }


    public void readAsStreamBatch(String pathName, Consumer<List<Transaction>> transactionConsumer, int totalThreads) throws IOException {


        try(BufferedReader reader = Files.newBufferedReader(Path.of(pathName));
            ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
        ){
            reader.readLine(); //Pula nome de colunas
            String line;
            List<Transaction> transactions = new ArrayList<>();

            long totalInBatch = 0;
            long batchLimit = 10_000;
            while (((line = reader.readLine()) != null)) {


                if(totalInBatch< batchLimit){
                    Optional<Transaction> transactionOp = TransactionIngestor.convertLineToTransaction(line);
                    transactionOp.ifPresent(transactions::add);
                    totalInBatch++;
                } else{
                    List<Transaction> batch = new ArrayList<>(transactions);
                    executorService.execute(() -> {
                        transactionConsumer.accept(batch);
                    });
                    totalInBatch = 0;
                    transactions = new ArrayList<>();
                }

            }

        }
    }
}
