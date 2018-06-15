package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DataView<K, V> implements View {
    private List<DataViewValue<K, V>> data;

    public void addToList(K key, V value){ data.add(new DataViewValue<>(key, value)); }

    public DataView(List<DataViewValue<K, V>> data) {
        this.data = data;
    }

    public DataView() {
        this.data = new ArrayList<>();
    }
}
