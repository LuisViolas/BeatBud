package com.example.beatbud_1;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSucess(List<Festival> festivalList);
    void onFirebaseLoadFailed(String message);
}
