package com.penelope.seatforyou.data.editor;

import com.google.gson.JsonObject;
import com.penelope.seatforyou.data.shop.Shop;
import com.penelope.seatforyou.data.user.User;

import java.lang.reflect.Array;
import java.security.cert.PKIXRevocationChecker;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 하나의 편집 프로젝트(가게 전체 배치정보) 정보를 저장하는 클래스
 * 길이기준 최대 - px당 1미터, 최소 - px당 0.5mm
 * 기본길이단위는 mm
 */
public class InteriorProject {

    // 프로젝트 식별 정보
    private User user;
    private Shop shop;
    private String uid;

    // 가게의 각 층의 정보를 저장
    private ArrayList<InteriorLevel> levels = new ArrayList<>();
    private InteriorLevel currentLevel;

    // 기존 프로젝트를 불러올 때 호출됨
    public InteriorProject(User user, Shop shop, ArrayList<InteriorLevel> levels) {
        this.user = user;
        this.shop = shop;
        this.levels = levels;
        if(!levels.isEmpty())
            currentLevel = levels.get(0);
        else
            throw new NoSuchElementException();
    }

    // 새로운 프로젝트를 생성할 때 호출됨
    public InteriorProject(User user, Shop shop) {
        this.user = user;
        this.shop = shop;
        levels.add(new InteriorLevel((levels.size()+1) + "층"));
        currentLevel = levels.get(0);
    }

    // 새로운 프로젝트를 생성할 때 호출됨2
    public InteriorProject(String uid) {
        this.uid = uid;
        levels.add(new InteriorLevel((levels.size()+1) + "층"));
        currentLevel = levels.get(0);
    }

    public ArrayList<InteriorLevel> getLevels(){
        return levels;
    }

    public InteriorLevel getCurrentLevel(){
        return currentLevel;
    }

    public void addLevel(InteriorLevel level){
        levels.add(level);
    }

    public void deleteLevel(int i){
        levels.remove(i);
    }

    public void setCurrentLevel(int levelId){
        Optional<InteriorLevel> findLevel =
                levels.stream().filter(s -> s.getLevelId() == levelId).findFirst();

        findLevel.ifPresent(level -> currentLevel = level);
    }

    // 현재 층의 실내도를 그리는 함수
    public void drawAll(){
        currentLevel.drawAll();
    }

    // 프로젝트를 json 객체로 변환한 뒤 db에 저장하는 함수
    public JsonObject saveProject(){

        return new JsonObject();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

}
