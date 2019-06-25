package com.example.esdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.elasticsearch.index.Index;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Document(indexName = "doc",shards = 1,replicas = 0)
public class Doc {

    @Id
    private long id;

    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String caseName;

    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String court;

    @Field(type = FieldType.Keyword)
    private List<String> caseType;

    @Field(type = FieldType.Keyword)
    private String caseNumber;

    @Field(type = FieldType.Keyword)
    private String docType;

    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String reason;

    @Field(index=true,analyzer="ik_smart",store=true,searchAnalyzer="ik_smart",type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private List<String> litigant;


    @Field(type = FieldType.Date, format = DateFormat.custom,pattern ="yyyy-MM-dd")
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd",timezone ="GMT+8")
    private Date time;

    @Field(type = FieldType.Keyword)
    private String url;

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
                ", time=" + time +
                ", url='" + url + '\'' +
                '}';
    }

    public String getcaseNumber() {
        return caseNumber;
    }

    public void setcaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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
}
