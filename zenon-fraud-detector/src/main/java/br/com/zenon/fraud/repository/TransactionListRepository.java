package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

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
}
