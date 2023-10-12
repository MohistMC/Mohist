package com.mohistmc.plugins;

import com.mohistmc.util.I18n;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 19:53:09
 */

public enum MessageI18N {

    WORLDMANAGE_PREFIX(I18n.as("worldmanage.prefix")),
    WORLDMANAGE_GUI_LORE_0(I18n.as("worldmanage.gui.lore0")),
    WORLDMANAGE_GUI_LORE_1(I18n.as("worldmanage.gui.lore1")),
    WORLDMANAGE_GUI_LORE_2(I18n.as("worldmanage.gui.lore2")),
    WORLDMANAGE_GUI_LORE_3(I18n.as("worldmanage.gui.lore3")),
    WORLDMANAGE_GUI_LORE_4(I18n.as("worldmanage.gui.lore4")),
    WORLDMANAGE_GUI_TITLE_0(I18n.as("worldmanage.gui.title0")),
    WORLDMANAGE_GUI_TITLE_1(I18n.as("worldmanage.gui.title1")),
    WORLDMANAGE_GUI_CLOSE(I18n.as("worldmanage.gui.close"));

    public final String key;

    MessageI18N(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
