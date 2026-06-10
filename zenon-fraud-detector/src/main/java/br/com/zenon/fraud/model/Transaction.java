package br.com.zenon.fraud.model;

import java.math.BigDecimal;

public record Transaction(int step, TypeEnum type, BigDecimal amount, Customer clientOrig, Customer clientDest,
                          boolean isFraud, boolean isFlaggedFraud) {

}
