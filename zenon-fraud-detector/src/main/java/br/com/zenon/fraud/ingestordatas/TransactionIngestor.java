package br.com.zenon.fraud.ingestordatas;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionIngestor {

    private static final int LIMIT = 1000;
    private static final int TOTAL_COLUMN = 11;

    public static List<Transaction> extractTransaction(String filePath) {
        Path path = Path.of(filePath);

        List<Transaction> transactionList = new ArrayList<>();
        if (Files.exists(path)) {
                List<String> linesFinal = extractLines(path);
                transactionList = linesFinal.stream().map(a -> convertLineToTransaction(a).orElse(null)).toList();
                return transactionList;
                //  foreach()

        } else {
            throw new RuntimeException("Arquivo não encontrado");
        }

    }

    public static List<String> extractLines(Path path) {
        try {
        List<String> lines = Files.readAllLines(path);
        int limit = Math.min(lines.size(), LIMIT);
        return lines.subList(1, limit);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> extractTransactionLines(String filePath) {
        Path path = Path.of(filePath);

        if (Files.exists(path)) {

            try {
                List<String> lines = Files.readAllLines(path);
                int limit = Math.min(lines.size(), LIMIT);

                return lines.subList(1, limit);
                //  foreach()
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("Arquivo não encontrado");
        }

    }

    public static Optional<Transaction> convertLineToTransaction(String line) {
        if (line != null && !line.isEmpty()) {
            String[] chunks = line.split(",");

            if (chunks.length != TOTAL_COLUMN) {
                throw new TransacionIngestorException("Quantidade de colunas deve ser igual a " + TOTAL_COLUMN);
            }
            int step = getStep(chunks[0]);
            TypeEnum type = getTypeEnum(chunks[1]);
            BigDecimal amount = getAmount(chunks[2]);
            Customer clientOrig = getCustomer(chunks[3], chunks[4], chunks[5]);
            Customer clientDest = getCustomer(chunks[6], chunks[7], chunks[8]);
            boolean isFraud = "1".equals(chunks[9]);
            boolean isFlaggedFraud = "1".equals(chunks[10]);

            return Optional.of(new Transaction(step, type, amount, clientOrig, clientDest, isFraud, isFlaggedFraud));

        }
        return Optional.ofNullable(null);
    }


    private static TypeEnum getTypeEnum(String typeText) {
        if (typeText!=null && typeText.isEmpty()){
            throw new TransacionIngestorException("Valor do tipo não pode ser nulo");
        }
        TypeEnum type = null;
        try {
            type = TypeEnum.valueOf(typeText);
        } catch (IllegalArgumentException e) {
            throw new TransacionIngestorException("Valor do type obtido: " + type + " é inválido. Valor esperado deve ser um dos seguintes: "
                    + Arrays.stream(TypeEnum.values()).toList());
        }
        return type;
    }

    private static int getStep(String stepText) {
        if (stepText!=null && stepText.isEmpty()){
            throw new TransacionIngestorException("Valor do step não pode ser nulo");
        }
        int step = 0;
        try {
            step = Integer.parseInt(stepText);
            if (step <= 0) {
                throw new TransacionIngestorException("Valor de step inválido. Valor esperado é maior que zero. Valor obtido: " + step);
            }
        } catch (NumberFormatException e) {
            throw new TransacionIngestorException("Valor de step inválido. Valor deve ser numérico. ", e);
        }
        return step;
    }

    private static BigDecimal getAmount(String amountText) {
        if (amountText!=null && amountText.isEmpty()){
            throw new TransacionIngestorException("Valor do amount não pode ser nulo");
        }
        BigDecimal amount = null;
        try {
            amount = new BigDecimal(amountText);
            if (amount.compareTo(BigDecimal.ZERO)<0) {
                throw new TransacionIngestorException("Valor de amount inválido. Valor esperado é maior ou igual azero. Valor obtido: " + amount);
            }
        } catch (NumberFormatException e) {
            throw new TransacionIngestorException("Valor de amount inválido. Valor deve ser numérico. ", e);
        }
        return amount;
    }

    private static Customer getCustomer(String name, String oldBalanceText,
                                        String newBalanceText) {
        if (name!=null && name.isEmpty()){
            throw new TransacionIngestorException("Valor do 'name' do cliente não pode ser nulo");
        }
        if (oldBalanceText!=null && oldBalanceText.isEmpty()){
            throw new TransacionIngestorException("Valor do 'oldBalance' do cliente não pode ser nulo");
        }
        if (newBalanceText!=null && newBalanceText.isEmpty()){
            throw new TransacionIngestorException("Valor do 'newBalance' do cliente não pode ser nulo");
        }
        BigDecimal oldBalance = null;
        try {
            oldBalance = new BigDecimal(oldBalanceText);
            if (oldBalance.compareTo(BigDecimal.ZERO)<0) {
                throw new TransacionIngestorException("Valor de oldBalance inválido. Valor esperado é maior ou igual azero. Valor obtido: " + oldBalance);
            }
        } catch (NumberFormatException e) {
            throw new TransacionIngestorException("Valor de oldBalance inválido. Valor deve ser numérico. ", e);
        }

        BigDecimal newBalance = null;
        try {
            newBalance = new BigDecimal(newBalanceText);
            if (newBalance.compareTo(BigDecimal.ZERO)<0) {
                throw new TransacionIngestorException("Valor de newBalance inválido. Valor esperado é maior ou igual azero. Valor obtido: " + newBalance);
            }
        } catch (NumberFormatException e) {
            throw new TransacionIngestorException("Valor de newBalance inválido. Valor deve ser numérico. ", e);
        }

        return new Customer(name, oldBalance, newBalance);
    }
}
