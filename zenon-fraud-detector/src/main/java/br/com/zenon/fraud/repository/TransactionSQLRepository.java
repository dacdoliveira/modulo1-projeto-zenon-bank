package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.ingestordatas.TransactionIngestor;
import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository{
    @Override
    public Optional<Transaction> obterPorNomeCliente(String nomeCliente) {
        String sql = "SELECT id, step, type_enum , amount, cliente_orig_name, cliente_orig_new_balance, cliente_orig_old_balance, " +
                " cliente_dest_name, cliente_dest_new_balance, cliente_dest_old_balance, is_fraud, is_flagged_fraud " +
                " FROM transaction_fraud where cliente_orig_name = ? ";
        Transaction transaction = null;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zenonfraud", "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql);){
            ps. setString(1, nomeCliente);
            ResultSet rs = ps.executeQuery();
           if(rs.next()){
               int step = rs.getInt(2);
               TypeEnum type = TypeEnum.valueOf(rs.getString(3));
               BigDecimal amount = rs.getBigDecimal(4);
               String clientOrigName = rs.getString(5);
               BigDecimal clientOrigNewBalance = rs.getBigDecimal(6);
               BigDecimal clientOrigOldBalance = rs.getBigDecimal(7);
               Customer clientOrig = new Customer(clientOrigName, clientOrigOldBalance, clientOrigNewBalance);
               String clientDestName = rs.getString(8);
               BigDecimal clientDestNewBalance = rs.getBigDecimal(9);
               BigDecimal clientDestOldBalance = rs.getBigDecimal(10);
               Customer clientDest = new Customer(clientDestName, clientDestOldBalance, clientDestNewBalance);
               boolean isFraud = rs.getBoolean(11);
               boolean isFlaggedFraud = rs.getBoolean(12);
               transaction = new Transaction(step, type, amount, clientOrig, clientDest, isFraud, isFlaggedFraud);
           }


        }catch (SQLException e){
            System.out.println("Erro ao conectar com banco de dados");
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(transaction);
    }
    @Override
    public Optional<Integer> getTotalTransactions() {
        String sql = "select count(*) as TOTAL_TRANSACAO FROM transaction_fraud";
        Integer total = null;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zenonfraud", "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                total = rs.getInt(1);
            }


        }catch (SQLException e){
            System.out.println("Erro ao conectar com banco de dados");
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(total);
    }
    @Override
    public Optional<Integer> getTotalTransactionsFraud() {
        String sql = "select count(*) as TOTAL_FRAUD FROM transaction_fraud where is_fraud = true";
        Integer total = null;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zenonfraud", "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                total = rs.getInt(1);
            }


        }catch (SQLException e){
            System.out.println("Erro ao conectar com banco de dados");
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(total);
    }
    @Override
    public Optional<BigDecimal> getTotalAmountFraud() {
        String sql = "select sum(amount) as TOTAL_AMOUNT_FRAUD FROM transaction_fraud where is_fraud = true";
        BigDecimal total = null;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zenonfraud", "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                total = rs.getBigDecimal(1);
            }


        }catch (SQLException e){
            System.out.println("Erro ao conectar com banco de dados");
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(total);
    }
    @Override
    public Optional<BigDecimal> getTotalAmount() {
        String sql = "select sum(amount) as TOTAL_AMOUNT_FRAUD FROM transaction_fraud";
        BigDecimal total = null;
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zenonfraud", "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                total = rs.getBigDecimal(1);
            }


        }catch (SQLException e){
            System.out.println("Erro ao conectar com banco de dados");
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(total);
    }

    @Override
    public void save(Transaction transaction) {

        String sql = "INSERT INTO transaction_fraud (step, type_enum, amount, cliente_orig_name, cliente_orig_old_balance, cliente_orig_new_balance,\n" +
                "cliente_dest_name, cliente_dest_old_balance, cliente_dest_new_balance, is_fraud, is_flagged_fraud)\n" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/zenonfraud", "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql);){
            ps.setInt(1, transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());
            ps.setString(4, transaction.clientOrig().name());
            ps.setBigDecimal(5,transaction.clientOrig().oldBalance() );
            ps.setBigDecimal(6,transaction.clientOrig().newBalance() );
            ps.setString(7, transaction.clientDest().name());
            ps.setBigDecimal(8,transaction.clientDest().oldBalance());
            ps.setBigDecimal(9,transaction.clientDest().newBalance());
            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggedFraud());

            ps.execute();

        }catch (SQLException e){
            System.out.println("Erro ao conectar com banco de dados");
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveBatch(List<Transaction> transactions) {
        String url = "jdbc:mysql://localhost:3306/zenonfraud"
                + "?rewriteBatchedStatements=true";
//rewriteBatchedStatements -> Essa opção permite que o driver otimize várias inserções
        String sql = """
        INSERT INTO transaction_fraud (
            step,
            type_enum,
            amount,
            cliente_orig_name,
            cliente_orig_old_balance,
            cliente_orig_new_balance,
            cliente_dest_name,
            cliente_dest_old_balance,
            cliente_dest_new_balance,
            is_fraud,
            is_flagged_fraud
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try(
            Connection con = DriverManager.getConnection(url, "root", "senha123");
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            con.setAutoCommit(false);
            try {
                int sizeBatch = 1_000;
                int totalInBatch = 0;

                for (Transaction transaction : transactions) {
                    fillStatement(ps, transaction);
                    ps.addBatch();
                    totalInBatch++;

                    if (totalInBatch == sizeBatch) {
                        ps.executeBatch();
                        ps.clearBatch();
                        totalInBatch = 0;
                    }
                }
                // Executa os registros restantes
                if (totalInBatch > 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e);
            }


        }catch (SQLException e){
            throw new RuntimeException(
                    "Erro ao inserir transações em lote", e
            );
        }

    }

    @Override
    public void saveTheBatch(List<Transaction> transactions) {
        String url = "jdbc:mysql://localhost:3306/zenonfraud"
                + "?rewriteBatchedStatements=true";
//rewriteBatchedStatements -> Essa opção permite que o driver otimize várias inserções
        String sql = """
        INSERT INTO transaction_fraud (
            step,
            type_enum,
            amount,
            cliente_orig_name,
            cliente_orig_old_balance,
            cliente_orig_new_balance,
            cliente_dest_name,
            cliente_dest_old_balance,
            cliente_dest_new_balance,
            is_fraud,
            is_flagged_fraud
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try(
                Connection con = DriverManager.getConnection(url, "root", "senha123");
                PreparedStatement ps = con.prepareStatement(sql)
        ){
            con.setAutoCommit(false);
            try {

                for (Transaction transaction : transactions) {
                    fillStatement(ps, transaction);
                    ps.addBatch();
                }
                    ps.executeBatch();
                    ps.clearBatch();

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e);
            }


        }catch (SQLException e){
            throw new RuntimeException(
                    "Erro ao inserir transações em lote", e
            );
        }

    }

    @Override
    public void saveBatchByPath(Path csvFile) {
        String url = "jdbc:mysql://localhost:3306/zenonfraud"
                + "?rewriteBatchedStatements=true";
//rewriteBatchedStatements -> Essa opção permite que o driver otimize várias inserções
        String sql = """
        INSERT INTO transaction_fraud (
            step,
            type_enum,
            amount,
            cliente_orig_name,
            cliente_orig_old_balance,
            cliente_orig_new_balance,
            cliente_dest_name,
            cliente_dest_old_balance,
            cliente_dest_new_balance,
            is_fraud,
            is_flagged_fraud
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try(
                BufferedReader reader = Files.newBufferedReader(csvFile);
                Connection con = DriverManager.getConnection(url, "root", "senha123");
                PreparedStatement ps = con.prepareStatement(sql)
        ){
            con.setAutoCommit(false);
            try {
                String line = null;
                int sizeBatch = 1_000;
                int totalInBatch = 0;
                reader.readLine(); // header
                while ((line = reader.readLine()) != null) {
                    Transaction transaction = TransactionIngestor.convertLineToTransaction(line).isPresent()
                            ?TransactionIngestor.convertLineToTransaction(line).get():null;

                    if (transaction==null)
                        continue;

                    fillStatement(ps, transaction);
                    ps.addBatch();
                    totalInBatch++;

                    if (totalInBatch == sizeBatch) {
                        ps.executeBatch();
                        ps.clearBatch();
                        totalInBatch = 0;
                    }
                }
                // Executa os registros restantes
                if (totalInBatch > 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e);
            }


        }catch (SQLException e){
            throw new RuntimeException(
                    "Erro ao inserir transações em lote", e
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void saveBatch(Path csvFile, long limit) {
        long count= 0;
        String url = "jdbc:mysql://localhost:3306/zenonfraud"
                + "?rewriteBatchedStatements=true";
//rewriteBatchedStatements -> Essa opção permite que o driver otimize várias inserções
        String sql = """
        INSERT INTO transaction_fraud (
            step,
            type_enum,
            amount,
            cliente_orig_name,
            cliente_orig_old_balance,
            cliente_orig_new_balance,
            cliente_dest_name,
            cliente_dest_old_balance,
            cliente_dest_new_balance,
            is_fraud,
            is_flagged_fraud
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try(
                BufferedReader reader = Files.newBufferedReader(csvFile);
                Connection con = DriverManager.getConnection(url, "root", "senha123");
                PreparedStatement ps = con.prepareStatement(sql)
        ){
            con.setAutoCommit(false);
            try {
                String line = null;
                int sizeBatch = 1_000;
                int totalInBatch = 0;
                reader.readLine(); // header
                while ((count<limit) && ((line = reader.readLine()) != null)) {
                    Transaction transaction = TransactionIngestor.convertLineToTransaction(line).isPresent()
                            ?TransactionIngestor.convertLineToTransaction(line).get():null;

                    if (transaction==null)
                        continue;

                    fillStatement(ps, transaction);
                    ps.addBatch();
                    totalInBatch++;

                    if (totalInBatch == sizeBatch) {
                        ps.executeBatch();
                        ps.clearBatch();
                        totalInBatch = 0;
                    }
                    count++;

                }
                // Executa os registros restantes
                if (totalInBatch > 0) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw new RuntimeException(e);
            }


        }catch (SQLException e){
            throw new RuntimeException(
                    "Erro ao inserir transações em lote", e
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillStatement(PreparedStatement ps, Transaction transaction) throws SQLException {
        Customer origem = transaction.clientOrig();
        Customer destino = transaction.clientDest();

        ps.setInt(1, transaction.step());
        ps.setString(2, transaction.type().name());
        ps.setBigDecimal(3, transaction.amount());

        ps.setString(4, origem.name());
        ps.setBigDecimal(5, origem.oldBalance());
        ps.setBigDecimal(6, origem.newBalance());

        ps.setString(7, destino.name());
        ps.setBigDecimal(8, destino.oldBalance());
        ps.setBigDecimal(9, destino.newBalance());

        ps.setBoolean(10, transaction.isFraud());
        ps.setBoolean(11, transaction.isFlaggedFraud());
    }

}
