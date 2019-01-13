package com.example.pc.gallerytest;


public class MediaFileInfo {
    private String fileName;
    private String filePath;
    private Double fileSize;
    private boolean fileSelect;

    public boolean getFileSelect() {
        return fileSelect;
    }

    public void setFileSelect(boolean fileSelect) {
        this.fileSelect = fileSelect;
    }

    public Double getFileSize() { return fileSize;    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {        this.fileName = fileName;    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

