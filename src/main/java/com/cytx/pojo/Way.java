package com.cytx.pojo;

public class Way {
    private Integer wayId;

    private String wayName;

    private String wayContent;

    public Integer getWayId() {
        return wayId;
    }

    public void setWayId(Integer wayId) {
        this.wayId = wayId;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName == null ? null : wayName.trim();
    }

    public String getWayContent() {
        return wayContent;
    }

    public void setWayContent(String wayContent) {
        this.wayContent = wayContent == null ? null : wayContent.trim();
    }
}