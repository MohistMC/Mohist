package com.mohistmc.plugins;

import com.mohistmc.MohistMC;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Mgazul by MohistMC
 * @date 2023/6/14 19:53:09
 */

public enum MessageI18N {

    WORLDMANAGE_PREFIX(MohistMC.i18n.get("worldmanage.prefix")),
    WORLDMANAGE_GUI_LORE_0(MohistMC.i18n.get("worldmanage.gui.lore0")),
    WORLDMANAGE_GUI_LORE_1(MohistMC.i18n.get("worldmanage.gui.lore1")),
    WORLDMANAGE_GUI_LORE_2(MohistMC.i18n.get("worldmanage.gui.lore2")),
    WORLDMANAGE_GUI_LORE_3(MohistMC.i18n.get("worldmanage.gui.lore3")),
    WORLDMANAGE_GUI_LORE_4(MohistMC.i18n.get("worldmanage.gui.lore4")),
    WORLDMANAGE_GUI_TITLE_0(MohistMC.i18n.get("worldmanage.gui.title0")),
    WORLDMANAGE_GUI_TITLE_1(MohistMC.i18n.get("worldmanage.gui.title1")),
    WORLDMANAGE_GUI_CLOSE(MohistMC.i18n.get("worldmanage.gui.close"));

    public final String key;

    MessageI18N(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
