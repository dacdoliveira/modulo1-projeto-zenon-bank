package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;
import java.nio.file.Path;
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

    @Override
    public void saveBatch(List<Transaction> transactions) {
        if (transactions!=null && !transactions.isEmpty()) {
            transactions.forEach(this::save);
        }
    }

    @Override
    public void saveBatchByPath(Path csvFile) {
        if (csvFile!=null){
            List<Transaction> transactionList = TransactionIngestor.extractTransaction(csvFile.toAbsolutePath().toString(), 10000);
            this.saveBatch(transactionList);
        }
    }

    @Override
    public void saveBatch(Path csvFile, long limit) {
        if (csvFile!=null){
            List<Transaction> transactionList = TransactionIngestor.extractTransaction(csvFile.toAbsolutePath().toString(), limit);
            this.saveBatch(transactionList);
        }
    }

    @Override
    public void saveTheBatch(List<Transaction> transactions) {

    }

    @Override
    public Optional<Integer> getTotalTransactions() {
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getTotalTransactionsFraud() {
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getTotalAmountFraud() {
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getTotalAmount() {
        return Optional.empty();
    }
}
