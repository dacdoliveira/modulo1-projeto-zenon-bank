package br.com.zenon.fraud.repository;

import br.com.zenon.fraud.model.Customer;
import br.com.zenon.fraud.model.Transaction;
import br.com.zenon.fraud.model.TypeEnum;

import java.math.BigDecimal;
import java.sql.*;
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
}
