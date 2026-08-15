package br.com.zenon.fraud.reports;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TransactionReport {
    public static void readFile(String filePath,  String language) throws IOException {

        Path path = Path.of(filePath);
        Stream<String> lines = Files.lines(path);
        long totalLines  = lines.count();
        System.out.println(getTranslateText("zeno.fraud.detector.msg.total_linhas", language) +": " + totalLines);
        lines = Files.lines(path);
        long totalFrauds = lines.filter(TransactionReport::isFraud).count();
        System.out.println(getTranslateText("zeno.fraud.detector.msg.total_fraudes", language) +": " + totalFrauds);
        lines = Files.lines(path);
        BigDecimal totalAmount = lines.map(TransactionReport::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(getTranslateText("zeno.fraud.detector.msg.valor_total", language) +": " + getFormattedCurrency(totalAmount, language));

    }

    static void main(String[] args) throws IOException {
        String language = (args!=null && args.length>0)? args[0] : "pt";

        readFile("./data/arquivo.csv", language);



    }

    private static String getFormattedCurrency(BigDecimal value, String language){
        if (value==null ) return null;
        Locale locale = null;
        if ("en".equalsIgnoreCase(language)){
            locale = Locale.of("en","US");
        } else if ("pt".equalsIgnoreCase(language)){
            locale =  Locale.of("pt","BR");
        } else{
            System.out.println("language não identificada, será usado o locale default");
            locale = Locale.getDefault();
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        return nf.format(value);

    }
    private static String getTranslateText(String prop, String language){
        if (prop==null || prop.isEmpty()) return null;

        Locale locale = null;
        if ("en".equalsIgnoreCase(language)){
            locale = Locale.of("en","US");
        } else if ("pt".equalsIgnoreCase(language)){
            locale =  Locale.of("pt","BR");
        } else{
            System.out.println("language não identificada, será usado o locale default");
            locale = Locale.getDefault();
        }
        ResourceBundle rsBundle = ResourceBundle.getBundle("report", locale);
        return rsBundle.getString(prop);
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
