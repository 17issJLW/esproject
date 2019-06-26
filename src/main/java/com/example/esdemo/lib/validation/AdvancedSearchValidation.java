package com.example.esdemo.lib.validation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdvancedSearchValidation {


    @Length(min = 0,max = 25,message = "关键词不能大于25字")
    private String keyword;

    @Length(min = 0,max = 10,message = "文件类别不能大于10字")
    private String docType;

    @Length(min = 0,max = 15,message = "案由不能大于15字")
    private String reason;


    private int fromYear;

    private int toYear;

    @Length(min = 0,max = 10,message = "审理状态不能大于10字")
    private String stage;

    @Length(min = 0,max = 15,message = "法院不能大于15字")
    private String court;

    private String sortBy;

    private boolean order;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
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

    @Override
    public String toString() {
        return "AdvancedSearchValidation{" +
                "keyword='" + keyword + '\'' +
                ", docType='" + docType + '\'' +
                ", reason='" + reason + '\'' +
                ", fromYear=" + fromYear +
                ", toYear=" + toYear +
                ", stage='" + stage + '\'' +
                ", court='" + court + '\'' +
                ", sortBy='" + sortBy + '\'' +
                ", order=" + order +
                '}';
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
