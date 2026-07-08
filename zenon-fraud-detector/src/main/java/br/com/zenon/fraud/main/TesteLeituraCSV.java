package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.TransacionIngestorException;
import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TesteLeituraCSV {

   static void main() {
       List<String> linesTransactionOutput = new ArrayList<>();
       List<String> linesTransactionOutputError = new ArrayList<>();
        List<String> transactionsLines = TransactionIngestor.extractTransactionLines("./data/paysim_with_bad_data.csv", 1000);
        int totalTransactions = 0;
       for (String line : transactionsLines){
           try{
               Optional<Transaction> transactionOp = TransactionIngestor.convertLineToTransaction(line);
               if (transactionOp.isPresent()){
                   linesTransactionOutput.add(transactionOp.get().toString());
                   totalTransactions++;
               } else {
                   linesTransactionOutputError.add("Error: " +line);
               }
           }catch (TransacionIngestorException e){
               linesTransactionOutputError.add("Error: " +line);
           }

       }

       linesTransactionOutputError.forEach(System.err::println);
       System.out.println(totalTransactions);
       linesTransactionOutput.forEach(System.out::println);

    }
}
