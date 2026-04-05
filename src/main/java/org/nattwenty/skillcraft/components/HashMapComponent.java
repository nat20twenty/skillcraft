package org.nattwenty.skillcraft.components;

import org.ladysnake.cca.api.v3.component.Component;

import java.util.HashMap;

public interface HashMapComponent extends Component {
    HashMap<String, Integer> getMap();
    void set(String key, Integer value);
    void remove(String key);
    void clear();
    int getKey(String key);
}
