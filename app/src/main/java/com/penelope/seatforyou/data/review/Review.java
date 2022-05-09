package com.penelope.seatforyou.data.review;

import java.util.Objects;

public class Review {

    private String id;
    private String shopId;
    private String uid;
    private String summary;
    private String detail;
    private int rating;
    private long created;

    public Review() {
    }

    public Review(String shopId, String uid, String summary, String detail, int rating) {
        this.shopId = shopId;
        this.uid = uid;
        this.summary = summary;
        this.detail = detail;
        this.rating = rating;
        this.created = System.currentTimeMillis();
        this.id = shopId + "_" + uid + "_" + created;
    }

    public String getId() {
        return id;
    }

    public String getShopId() {
        return shopId;
    }

    public String getUid() {
        return uid;
    }

    public String getSummary() {
        return summary;
    }

    public String getDetail() {
        return detail;
    }

    public int getRating() {
        return rating;
    }

    public long getCreated() {
        return created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return rating == review.rating && created == review.created && Objects.equals(id, review.id) && Objects.equals(shopId, review.shopId) && Objects.equals(uid, review.uid) && Objects.equals(summary, review.summary) && Objects.equals(detail, review.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shopId, uid, summary, detail, rating, created);
    }
}
