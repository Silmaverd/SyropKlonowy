package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.web.View;
import javafx.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DataView<K, V> implements View {
    private List<Pair<K, V>> data;

    public void addToList(K key, V value){ data.add(new Pair<>(key, value)); }

    public DataView(List<Pair<K, V>> data) {
        this.data = data;
    }

    public DataView() {
        this.data = new ArrayList<>();
    }
}
