package com.mankomania.game.gamecore.util;

import com.badlogic.gdx.math.Vector3;
import com.mankomania.game.core.fields.Position3;

/*********************************
 Created by Fabian Oraze on 09.05.20
 *********************************/

public class Vector3Helper {

    public Vector3Helper() {
    }

    public Vector3 getVector3(Position3 position) {

        Vector3 vector = new Vector3(position.x, position.y, position.z);
        return vector;

    }
}
