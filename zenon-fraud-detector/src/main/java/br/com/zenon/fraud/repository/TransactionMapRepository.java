package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository{

    private static Map<String, Transaction> transactionMap = new HashMap<>();

    public TransactionMapRepository() {
     List<Transaction>  transactionList =  TransactionIngestor.extractTransaction("./data/arquivo.csv", 100000);
     transactionMap = transactionList.stream().collect(Collectors.toMap(a -> a.clientOrig().name(), Function.identity()));
    }

    @Override
    public Optional<Transaction> obterPorNomeCliente(String nomeCliente) {
        return Optional.of(transactionMap.get(nomeCliente));
    }

    @Override
    public void save(Transaction transaction) {
        transactionMap.put(transaction.clientOrig().name(),transaction);
    }
}
