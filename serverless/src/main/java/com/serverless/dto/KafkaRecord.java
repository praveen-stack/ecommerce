package com.serverless.dto;

import java.io.Serializable;
import java.util.Map;

public class KafkaRecord implements Serializable {

    private String key;
    private Map<String, Object> value;

    public KafkaRecord() {
    }

    public KafkaRecord(String key, Map<String, Object> value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }
}
