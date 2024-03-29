package com.example.esdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.elasticsearch.index.Index;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(indexName = "doc",shards = 1,replicas = 0)
public class Doc{

    @Id
    private long id;

    @Field(index=true,analyzer="ik_max_word",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String caseName;


//    @Field(index=true,type = FieldType.Keyword)
    @Field(index=true,type = FieldType.Keyword)
    private String court;

    @Field(type = FieldType.Keyword)
    private List<String> caseType;

    @Field(type = FieldType.Keyword)
    private String caseNumber;

    @Field(type = FieldType.Keyword)
    private String docType;

    @Field(type = FieldType.Keyword)
    private String reason;


    @Field(index=true,analyzer="ik_max_word",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private List<String> litigant;

    @Field(type = FieldType.Integer)
    private int clickCount;


    @Field(type = FieldType.Date, format = DateFormat.custom,pattern ="yyyy-MM-dd")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd",timezone ="GMT+8")
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    private Date time;

    @Field(type = FieldType.Keyword)
    private String url;

    @Field(index=true,type = FieldType.Keyword)
    private List<String> lawList;

    @Field(index = true,type = FieldType.Keyword)
    private List<String> lawNameList;

    @Field(index = true,type = FieldType.Keyword)
    private String location;

    @Field(type = FieldType.Integer)
    private int contentSize;

    @Field(type = FieldType.Integer)
    private int weight;

    @Field(type = FieldType.Keyword)
    private List<String> stage;

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", caseName='" + caseName + '\'' +
                ", court='" + court + '\'' +
                ", caseType=" + caseType +
                ", caseNumber='" + caseNumber + '\'' +
                ", docType='" + docType + '\'' +
                ", reason='" + reason + '\'' +
                ", content='" + content + '\'' +
                ", litigant=" + litigant +
                ", clickCount=" + clickCount +
                ", time=" + time +
                ", url='" + url + '\'' +
                ", lawList=" + lawList +
                ", lawNameList=" + lawNameList +
                ", location='" + location + '\'' +
                ", contentSize=" + contentSize +
                ", weight=" + weight +
                ", stage=" + stage +
                '}';
    }

    public List<String> getLawNameList() {
        return lawNameList;
    }

    public void setLawNameList(List<String> lawNameList) {
        this.lawNameList = lawNameList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getContentSize() {
        return contentSize;
    }

    public void setContentSize(int contentSize) {
        this.contentSize = contentSize;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public List<String> getCaseType() {
        return caseType;
    }

    public void setCaseType(List<String> caseType) {
        this.caseType = caseType;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getLitigant() {
        return litigant;
    }

    public void setLitigant(List<String> litigant) {
        this.litigant = litigant;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getLawList() {
        return lawList;
    }

    public void setLawList(List<String> lawList) {
        this.lawList = lawList;
    }

    public List<String> getStage() {
        return stage;
    }

    public void setStage(List<String> stage) {
        this.stage = stage;
    }
}
