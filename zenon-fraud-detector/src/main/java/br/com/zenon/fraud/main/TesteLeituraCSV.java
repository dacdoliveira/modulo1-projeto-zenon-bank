package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TesteLeituraCSV {

   static void main() {
        List<Transaction> transactions = TransactionIngestor.extractTransaction("./data/arquivo.csv");
        transactions.forEach(System.out::println);

    }
}
