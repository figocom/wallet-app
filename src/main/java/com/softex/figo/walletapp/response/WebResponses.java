package com.softex.figo.walletapp.response;

import java.util.List;

public record WebResponses<T extends MResponse>(List<T> datalist) {

    public List<T> getDataList() {
        return datalist;
    }
}
