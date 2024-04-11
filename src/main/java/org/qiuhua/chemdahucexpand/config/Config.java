package org.qiuhua.chemdahucexpand.config;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.qiuhua.chemdahucexpand.Main;

import java.io.File;

public class Config {

    private static FileConfiguration config;

    public static String getStr(String val){
        return config.getString(val);
    }

    public static int getInt(String val){
        return config.getInt(val);
    }


    public static void load(){
        config = Tool.loadFile(new File(Main.getMainPlugin().getDataFolder(),"Config.yml"));
    }







}
