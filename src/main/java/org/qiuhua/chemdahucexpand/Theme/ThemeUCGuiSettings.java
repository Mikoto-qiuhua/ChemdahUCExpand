package org.qiuhua.chemdahucexpand.Theme;

import ink.ptms.chemdah.core.conversation.theme.ThemeSettings;
import ink.ptms.chemdah.taboolib.library.configuration.ConfigurationSection;

public class ThemeUCGuiSettings extends ThemeSettings {
    public final String spacingX;
    public final String spacingY;

    public final int talkCloseDelay;

    public ThemeUCGuiSettings(ConfigurationSection root) {
        super(root);
        this.spacingX = root.getString("spacingX");
        this.spacingY = root.getString("spacingY");
        this.talkCloseDelay = root.getInt("talkCloseDelay");
    }

}
