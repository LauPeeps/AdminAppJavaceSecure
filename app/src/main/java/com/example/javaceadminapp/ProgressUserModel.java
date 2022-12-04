package com.example.javaceadminapp;

public class ProgressUserModel {

    String module_id, module_name;
    Long progress;

    public ProgressUserModel(String module_id, String module_name, Long progress) {
        this.module_id = module_id;
        this.module_name = module_name;
        this.progress = progress;
    }

    public String getModule_id() {
        return module_id;
    }

    public void setModule_id(String module_id) {
        this.module_id = module_id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }
}
