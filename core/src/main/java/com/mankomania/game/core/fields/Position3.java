package com.mankomania.game.core.fields;

public class Position3 {
    public float x;
    public float y;
    public float z;

    public Position3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Position3 add(Position3 pos){
        return new Position3(this.x+pos.x,this.y+pos.y,this.z+pos.z);
    }
}
