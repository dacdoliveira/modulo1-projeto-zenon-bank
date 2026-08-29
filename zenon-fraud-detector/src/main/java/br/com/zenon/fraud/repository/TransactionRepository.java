package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    public Optional<Transaction> obterPorNomeCliente(String nomeCliente);
    public void save(Transaction transaction);
    public void saveBatch(List<Transaction> transactions);
    public void saveBatchByPath(Path csvFile);
    public void saveBatch(Path csvFile, long limit);
    public void saveTheBatch(List<Transaction> transactions);
    public Optional<Integer> getTotalTransactions();
    public Optional<Integer> getTotalTransactionsFraud();
    public Optional<BigDecimal> getTotalAmountFraud();
    public Optional<BigDecimal> getTotalAmount();
}
