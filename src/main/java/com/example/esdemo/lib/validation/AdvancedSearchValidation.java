package com.example.esdemo.lib.validation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdvancedSearchValidation {

    @NotNull(message = "keyword not null")
//    @Length(min = 0,max = 25,message = "关键词不能大于25字")
    private List<String> keyword;

    @NotNull(message = "docType not null")
    @Length(min = 0,max = 10,message = "文件类别不能大于10字")
    private String docType;

    @NotNull(message = "reason not null")
    @Length(min = 0,max = 15,message = "案由不能大于15字")
    private String reason;

    @NotNull(message = "fromYear not null")
    private int fromYear;

    @NotNull(message = "toYear not null")
    private int toYear;

    @NotNull(message = "stage not null")
    @Length(min = 0,max = 10,message = "审理状态不能大于10字")
    private String stage;

    @NotNull(message = "court not null")
    @Length(min = 0,max = 15,message = "法院不能大于15字")
    private String court;

    @NotNull(message = "sortBy not null")
    private String sortBy;

    @NotNull(message = "order not null")
    private boolean order;

    @NotNull(message = "law not null")
    private String law;

    @NotNull(message = "location not null")
    private String location;

    @Override
    public String toString() {
        return "AdvancedSearchValidation{" +
                "keyword=" + keyword +
                ", docType='" + docType + '\'' +
                ", reason='" + reason + '\'' +
                ", fromYear=" + fromYear +
                ", toYear=" + toYear +
                ", stage='" + stage + '\'' +
                ", court='" + court + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", order=" + order +
                ", law='" + law + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLaw() {
        return law;
    }

    public void setLaw(String law) {
        this.law = law;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }
}
