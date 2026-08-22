package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    public Optional<Transaction> obterPorNomeCliente(String nomeCliente);
    public void save(Transaction transaction);
}
