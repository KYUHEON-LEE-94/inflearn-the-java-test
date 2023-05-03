package me.khlee.inflearnthejavatest.domain;

import java.time.LocalDateTime;

public class Study {

    private StudyStatus status =StudyStatus.DRAFT;

    private int limit;

    private String name;

    private Member owner;

    private LocalDateTime openDateTime;


    public Study() {
    }

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }
    
    public  void open(){
        this.status = StudyStatus.OPEN;
        this.openDateTime = LocalDateTime.now();
    }

    public Study(int limit) {
        if(limit<0){
            throw  new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this. limit = limit;
    }

    public StudyStatus getStatus(){
        return this.status;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setStatus(StudyStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member getOwner() {
        return owner;
    }

    public LocalDateTime getOpenDateTime() {
        return openDateTime;
    }

    public void setOpenDateTime(LocalDateTime openDateTime) {
        this.openDateTime = openDateTime;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limit=" + limit +
                ", name=" + name +
                '}';
    }
}
