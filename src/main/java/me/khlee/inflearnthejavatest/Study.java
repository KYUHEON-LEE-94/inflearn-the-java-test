package me.khlee.inflearnthejavatest;

public class Study {

    private  StudyStatus status =StudyStatus.DRAFT;

    private int limit;

    private String name;


    public Study() {
    }

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
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

    @Override
    public String toString() {
        return "Study{" +
                "status=" + status +
                ", limit=" + limit +
                ", name=" + name +
                '}';
    }
}