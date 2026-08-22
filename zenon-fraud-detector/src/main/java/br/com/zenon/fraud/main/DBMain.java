package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.repository.TransactionRepository;
import br.com.zenon.fraud.repository.TransactionSQLRepository;

import java.util.List;
import java.util.Optional;

public class DBMain {
    static void main() {

        TransactionRepository repository = new TransactionSQLRepository();

        List<Transaction> transactionList = TransactionIngestor.extractTransaction("./data/arquivo.csv", 10000);
        int total = transactionList.size();
        int count = 1;
        long timeIni = System.nanoTime();
        for (Transaction transaction: transactionList){
            if(count > 1331) {
                repository.save(transaction);
                System.out.println("Salvou " + count + "de " + total);
            }
            count++;
        }
        long timeFim = System.nanoTime();

      //  System.out.println("Insert em lote durou: " + (timeFim - timeIni) + " nanoSegundos");

        String nomeCliente = "C1231006815";
        System.out.println("buscando por: " + nomeCliente);
        timeIni = System.nanoTime();
        Optional<Transaction> transactionOp = repository.obterPorNomeCliente(nomeCliente);
        transactionOp.ifPresent(System.out::println);
        timeFim = System.nanoTime();
        System.out.println("Busca durou : " + (timeFim - timeIni) + " nanoSegundos");

        nomeCliente = "C1234";
        System.out.println("buscando por: " + nomeCliente);
        timeIni = System.nanoTime();
        Optional<Transaction> transactionOp2 = repository.obterPorNomeCliente(nomeCliente);
        transactionOp2.ifPresent(System.out::println);
        timeFim = System.nanoTime();
        System.out.println("Busca durou : " + (timeFim - timeIni) + " nanoSegundos");


    }
}
