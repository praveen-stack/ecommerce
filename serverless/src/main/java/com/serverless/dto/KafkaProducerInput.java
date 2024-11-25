package com.serverless.dto;

import java.io.Serializable;
import java.util.List;

public class KafkaProducerInput implements Serializable {
    private List<KafkaRecord> records;

    public List<KafkaRecord> getRecords() {
        return records;
    }

    public void setRecords(List<KafkaRecord> records) {
        this.records = records;
    }
}
