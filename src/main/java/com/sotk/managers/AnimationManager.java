package com.sotk.managers;

import java.util.ArrayList;

public class AnimationManager {
    ArrayList<Animation> anims = new ArrayList<>();
    ArrayList<Animation> crucial = new ArrayList<>();//these animations take priority
    Animation curAnimation;

    public AnimationManager() {

    }

    public void addAnimation(Animation a) {
        anims.add(a);
    }
}
