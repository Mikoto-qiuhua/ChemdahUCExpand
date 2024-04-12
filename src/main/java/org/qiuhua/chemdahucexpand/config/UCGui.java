package org.qiuhua.chemdahucexpand.config;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.qiuhua.chemdahucexpand.Main;

import java.io.File;

public class UCGui {

    private static FileConfiguration ucGui;


    public static void load(){
        ucGui = Tool.loadFile(new File(Main.getMainPlugin().getDataFolder(),"ucGui.yml"));
    }



    public static FileConfiguration getUCGui() {
        return ucGui;
    }

}
