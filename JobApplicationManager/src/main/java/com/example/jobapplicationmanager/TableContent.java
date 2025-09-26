package com.example.jobapplicationmanager;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.util.function.Consumer;

public class TableContent {

    private String companyName;
    private String  jobTitle;
    private LocalDate applicationDate;
    private LocalDate technicalTestDate;
    private String interview;
    private String result;
    private String accountInfo;
    private String link;

    public TableContent() {

        this.applicationDate=LocalDate.now();
        this.interview="No";
        this.result="Pending";
    }

    public TableContent(String companyName, String jobTitle, LocalDate applicationDate, LocalDate technicalTestDate, String interview, String result, String accountInfo, String link) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.applicationDate = applicationDate;
        this.technicalTestDate = technicalTestDate;
        this.interview = interview;
        this.result = result;
        this.accountInfo = accountInfo;
        this.link = link;
    }

    public String getCompanyName() {
        return companyName;
    }

    public  void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getTechnicalTestDate() {
        return technicalTestDate;
    }

    public void setTechnicalTestDate(LocalDate technicalTestDate) {
        this.technicalTestDate = technicalTestDate;
    }

    public String getInterview() {
        return interview;
    }

    public void setInterview(String interview) {
        this.interview = interview;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


}
