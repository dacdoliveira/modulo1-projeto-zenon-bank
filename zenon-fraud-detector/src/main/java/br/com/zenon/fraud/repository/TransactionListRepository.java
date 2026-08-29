package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {
  private static List<Transaction> transactionList = new ArrayList<>();

    public TransactionListRepository() {
        transactionList =  TransactionIngestor.extractTransaction("./data/arquivo.csv", 100000);
    }

    public Optional<Transaction> obterPorNomeCliente(String nomeCliente){

      Optional<Transaction> resultOp = transactionList.stream().filter(a->a.clientOrig().name().equals(nomeCliente)).findFirst();

    return resultOp;
  }

  @Override
  public void save(Transaction transaction) {
    transactionList.add(transaction);
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
