package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.web.View;
import lombok.Getter;

import java.util.Map;

@Getter
public class MarketingDataView implements View {
    private Map<String, Object> map;

    public void addToMap(String key, Object value){ map.put(key, value); }

    public MarketingDataView(Map<String, Object> map) {
        this.map = map;
    }
}
