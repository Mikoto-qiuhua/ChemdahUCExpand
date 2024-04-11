package org.qiuhua.chemdahucexpand.config;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.qiuhua.chemdahucexpand.Main;

import java.io.File;

public class UCGui {


    private static FileConfiguration ucGui;

    public static void load(){
        ucGui = Tool.loadFile(new File(Main.getMainPlugin().getDataFolder(),"UCGui.yml"));
    }

    public static FileConfiguration getUcGui(){

        return ucGui;
    }


}
