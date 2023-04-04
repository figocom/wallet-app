package com.softex.figo.walletapp.response;

import lombok.Builder;

@Builder
public record WebResponse<T extends MResponse>(T data) {

}
