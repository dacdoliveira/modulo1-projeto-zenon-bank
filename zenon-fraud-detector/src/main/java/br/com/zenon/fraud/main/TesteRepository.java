package br.com.zenon.fraud.main;

import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionListRepository;
import br.com.zenon.fraud.repository.TransactionMapRepository;
import br.com.zenon.fraud.repository.TransactionRepository;

import java.util.Optional;

public class TesteRepository {
    static void main() {
        System.out.println("--------- Teste usando List");
        TransactionRepository repository = new TransactionListRepository();
        String nomeCliente = "C1868032458";
        long timeIni = System.nanoTime();
        Optional<Transaction> optionalTransaction = repository.obterPorNomeCliente(nomeCliente);
        long timeFim = System.nanoTime();
        System.out.println(optionalTransaction.isPresent()?optionalTransaction.get():"Transação não encontrada para o cliente " + nomeCliente);
        System.out.println("Busca durou: " + (timeFim - timeIni) + " nanoSegundos");

        System.out.println("--------- Teste usando Map");
        repository = new TransactionMapRepository();

        timeIni = System.nanoTime();
        optionalTransaction = repository.obterPorNomeCliente(nomeCliente);
        timeFim = System.nanoTime();
        System.out.println(optionalTransaction.isPresent()?optionalTransaction.get():"Transação não encontrada para o cliente " + nomeCliente);
        System.out.println("Busca durou: " + (timeFim - timeIni) + " nanoSegundos");

    }
}
