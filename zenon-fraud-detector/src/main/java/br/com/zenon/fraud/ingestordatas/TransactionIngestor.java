package br.com.zenon.fraud.ingestordatas;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionIngestor {

    private static final int LIMIT = 1000;

    public static List<Transaction> extractTransaction(String filePath) {
        Path path = Path.of(filePath);

        List<Transaction> transactionList = new ArrayList<>();
        if (Files.exists(path)) {

            try {
                List<String> lines = Files.readAllLines(path);
                int limit = Math.min(lines.size(), LIMIT);
                List<String> linesFinal = lines.subList(1,limit);
                transactionList = linesFinal.stream().map(TransactionIngestor::convertLineToTransaction).toList();
                return transactionList;
              //  foreach()
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Arquivo não encontrado");
        }

    }
    private static Transaction convertLineToTransaction(String line){
        if (line!=null && !line.isEmpty()) {
            String[] chunks = line.split(",");

            int step = Integer.parseInt(chunks[0]);
            TypeEnum type = TypeEnum.valueOf(chunks[1]);
            BigDecimal amount = new BigDecimal(chunks[2]);
            Customer clientOrig = new Customer(chunks[3],new BigDecimal(chunks[4]), new BigDecimal(chunks[5]));
            Customer clientDest = new Customer(chunks[6],new BigDecimal(chunks[7]), new BigDecimal(chunks[8]));
            boolean isFraud ="1".equals(chunks[9]);
            boolean isFlaggedFraud = "1".equals(chunks[10]);

            return new Transaction(step, type, amount, clientOrig, clientDest, isFraud, isFlaggedFraud);

        }
        return null;
    }
}
