package com.blinenterprise.SyropKlonowy.view;

import com.blinenterprise.SyropKlonowy.web.View;
import lombok.Getter;

import java.util.Map;

@Getter
public class MarketingDataView implements View {
    private Map<String, Object> data;

    public void addToMap(String key, Object value){ data.put(key, value); }

    public MarketingDataView(Map<String, Object> data) {
        this.data = data;
    }
}
