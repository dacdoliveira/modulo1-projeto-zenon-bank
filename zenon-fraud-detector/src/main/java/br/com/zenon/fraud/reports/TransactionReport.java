package br.com.zenon.fraud.reports;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TransactionReport {
    public static void readFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        Stream<String> lines = Files.lines(path);
        long totalLines  = lines.count();
        System.out.println("Total de linhas: " + totalLines);
        lines = Files.lines(path);
        long totalFrauds = lines.filter(TransactionReport::isFraud).count();
        System.out.println("Total de fraudes: " + totalFrauds);
        lines = Files.lines(path);
        BigDecimal totalAmount = lines.map(TransactionReport::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Valor total transacionado: " + totalAmount);

    }

    static void main() throws IOException {
        readFile("./data/arquivo.csv");
    }

    public static boolean isFraud (String line){
        if( line.startsWith("step")) return false;// Verifica se é cabeçalho

        Optional<Transaction> transactionOp = TransactionIngestor.convertLineToTransaction(line);
        return transactionOp.isPresent() && transactionOp.get().isFraud();
    }

    public static BigDecimal amount (String line){
        if( line.startsWith("step")) return BigDecimal.valueOf(0);// Verifica se é cabeçalho

        Optional<Transaction> transactionOp = TransactionIngestor.convertLineToTransaction(line);
        return transactionOp.isPresent()? transactionOp.get().amount():BigDecimal.valueOf(0);
    }
}
