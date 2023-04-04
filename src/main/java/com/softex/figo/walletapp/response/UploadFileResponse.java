package com.softex.figo.walletapp.response;

public record UploadFileResponse(
    String fileName,
    String fileDownloadUri,
    String fileType,
    long size
) implements MResponse {
}
