package br.com.zenon.fraud.ingestordatas;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    static void main() {

    }
    public List<Transaction> filterFraudTransaction(List<Transaction> transactionList){
        List<Transaction> fraudList = new ArrayList<>();
       if (transactionList!=null && !transactionList.isEmpty()){
        fraudList = transactionList.stream().filter(Transaction::isFraud).collect(Collectors.toList());
       }
        return fraudList;
    }

    public List<Transaction> filterTopAmountTransaction(List<Transaction> transactionList, long limit){
        List<Transaction> mostFraudList = new ArrayList<>();

        if (transactionList!=null && !transactionList.isEmpty()){
            mostFraudList = transactionList.stream().filter(Transaction::isFraud)
                    .sorted(Comparator.comparing(Transaction::amount).reversed()).limit(limit).toList();
        }
        return mostFraudList;
    }

    public List<String> listNameOrig(List<Transaction> transactionList, long limit){
        List<String> nameOrigList = new ArrayList<>();

        if (transactionList!=null && !transactionList.isEmpty()){
            nameOrigList = transactionList.stream().filter(Transaction::isFraud)
                    .sorted(Comparator.comparing(Transaction::amount).reversed()).map(Transaction::clientOrig).map(Customer::name).distinct().limit(limit).toList();
        }
        return nameOrigList;
    }

    public BigDecimal getTotalAmountFraud(List<Transaction> transactionList){
        BigDecimal total = null;
        List<Transaction> fraudList = new ArrayList<>();
        if (transactionList!=null && !transactionList.isEmpty()){
            total = transactionList.stream().filter(Transaction::isFraud)
                    .map(Transaction::amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return total;
    }

    public List<Transaction> filterFraudTransactionForType(List<Transaction> transactionList, TypeEnum type){
        List<Transaction> fraudList = new ArrayList<>();
        if (transactionList!=null && !transactionList.isEmpty()){
            fraudList = transactionList.stream().
                    filter(a -> (a.isFraud() && a.type().equals(type)))
                    .collect(Collectors.toList());
        }
        return fraudList;
    }
}
