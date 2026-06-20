package br.com.zenon.fraud.ingestordatas;

public class TransacionIngestorException extends RuntimeException{

    public TransacionIngestorException(String message) {
        super(message);
    }
    public TransacionIngestorException(String message, Throwable cause) {
        super(message, cause);
    }
}
