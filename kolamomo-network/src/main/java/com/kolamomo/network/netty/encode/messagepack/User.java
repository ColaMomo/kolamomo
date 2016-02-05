package com.kolamomo.network.netty.encode.messagepack;

/**
 * Created by jay on 16-2-4.
 */

import org.msgpack.annotation.Message;

import java.io.Serializable;

@Message
public class User implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return id + "," + name;
    }
}
