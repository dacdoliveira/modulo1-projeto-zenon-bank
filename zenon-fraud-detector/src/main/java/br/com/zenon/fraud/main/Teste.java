package br.com.zenon.fraud.main;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Teste {

    static void main() {
        List<Transaction> transactions = new ArrayList<>();
        var clientOrg = new Customer("C1231006815", BigDecimal.valueOf(170136.0),  BigDecimal.valueOf(160296.36));
        var clientDest = new Customer("M1979787155", BigDecimal.valueOf(0.0),  BigDecimal.valueOf(0.0));

        var transaction1 = new Transaction(1, TypeEnum.PAYMENT, BigDecimal.valueOf(9839.64), clientOrg, clientDest, false, false);

        var clientOrg2 = new Customer("C1280323807", BigDecimal.valueOf(850002.52),  BigDecimal.valueOf(0.0));
        var clientDest2 = new Customer("C873221189", BigDecimal.valueOf(6510099.11),  BigDecimal.valueOf(7360101.63));

        var transaction2 = new Transaction(743, TypeEnum.CASH_OUT, BigDecimal.valueOf(850002.52), clientOrg2, clientDest2, true, false);

        transactions.add(transaction1);
        transactions.add(transaction2);

        int count =1;

        for (Transaction transaction: transactions){
            System.out.println("Transação " + count);
            System.out.println("step " + transaction.step());
            System.out.println("type " + transaction.type().name());
            System.out.println("amount " + transaction.amount());
            System.out.println("nameOrig " + transaction.clientOrig().name());
            System.out.println("oldbalanceOrig " + transaction.clientOrig().oldBalance());
            System.out.println("newbalanceOrig " + transaction.clientOrig().newBalance());
            System.out.println("nameDest " + transaction.clientDest().name());
            System.out.println("oldbalanceDest " + transaction.clientDest().oldBalance());
            System.out.println("newbalanceDest " + transaction.clientDest().newBalance());
            System.out.println("isFraud " + (transaction.isFraud()?1:0));
            System.out.println("isFlaggedFraud " + (transaction.isFlaggedFraud()?1:0));
            System.out.println();
            System.out.println();
            count ++;
        }





    }
}
