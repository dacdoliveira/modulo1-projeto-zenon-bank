package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.util.List;

public class TesteOutOfMemory {
    static void main() {
        List<Transaction> transactionList = TransactionIngestor.extractTransaction("./data/arquivo.csv");
        System.out.println(transactionList.size());
    }
}
