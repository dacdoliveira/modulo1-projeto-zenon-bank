use zenonfraud

create table transaction_fraud (
                                   id bigint PRIMARY KEY AUTO_INCREMENT,
                                   step int NOT NULL,
                                   type_enum ENUM('CASH_IN', 'CASH_OUT', 'DEBIT', 'PAYMENT', 'TRANSFER') NOT NULL,
                                   amount DECIMAL(19,2) NOT NULL,
                                   cliente_orig_name VARCHAR(20) NOT NULL,
                                   cliente_orig_old_balance DECIMAL(19,2) NOT NULL,
                                   cliente_orig_new_balance DECIMAL(19,2) NOT NULL,
                                   cliente_dest_name VARCHAR(20) NOT NULL,
                                   cliente_dest_old_balance  DECIMAL(19,2) NOT NULL,
                                   cliente_dest_new_balance  DECIMAL(19,2) NOT NULL,
                                   is_fraud BOOLEAN NOT NULL,
                                   is_flagged_fraud BOOLEAN NOT NULL
);
