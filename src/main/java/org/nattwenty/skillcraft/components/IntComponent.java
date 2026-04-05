package org.nattwenty.skillcraft.components;

import org.ladysnake.cca.api.v3.component.Component;

public interface IntComponent extends Component {
    int getValue();
    void modValue(int mod);
}
