package com.blinenterprise.SyropKlonowy.view;

import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DataView<K, V> implements View {
    private List<Pair<K, V>> data;

    public void addToList(K key, V value){ data.add(new ImmutablePair<>(key, value)); }

    public DataView(List<Pair<K, V>> data) {
        this.data = data;
    }

    public DataView() {
        this.data = new ArrayList<>();
    }
}
