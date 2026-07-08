package br.com.zenon.fraud.main;

import br.com.zenon.fraud.ingestordatas.FraudAnalyzer;
import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.math.BigDecimal;
import java.util.List;

public class TesteFraudsAnalyzer {

    static void main() {
        List<Transaction> transactionList = TransactionIngestor.extractTransaction("./data/arquivo.csv", 50000);
        FraudAnalyzer fraudAnalyzer = new FraudAnalyzer();

        System.out.println("a) apenas isFraud");

        List<Transaction> fraudTransactionList = fraudAnalyzer.filterFraudTransaction(transactionList);

       // fraudTransactionList.forEach(System.out::println);
        System.out.println("Total de Fraudes : " + fraudTransactionList.size());

        System.out.println("b) Imprimir as 3 fraydes de maior valor");
        fraudTransactionList = fraudAnalyzer.filterTopAmountTransaction(transactionList, 3);
        fraudTransactionList.forEach(a -> System.out.println(a.amount()));

        System.out.println("c) Apenas nomes de origem das fraudes, imprimir os 5 maiores suspeitos");
        List<String> fraudCustumerList = fraudAnalyzer.listNameOrig(transactionList, 5);
        fraudCustumerList.forEach(System.out::println);

        System.out.println("d) Prejuízo total frauds");
        BigDecimal valorPrejuizo = fraudAnalyzer.getTotalAmountFraud(transactionList);
        System.out.println(valorPrejuizo);

        System.out.println("e) Total frauds por tipo ");
        TypeEnum[] types = TypeEnum.values();

        for(TypeEnum type : types){
            List<Transaction> transactionsForType =  fraudAnalyzer.filterFraudTransactionForType(transactionList, type);
            System.out.println(type.name() + " : " + ((transactionsForType!=null)?transactionsForType.size():0));
        }

    }
}
