package org.qiuhua.chemdahucexpand.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.qiuhua.chemdahucexpand.Main;
import org.qiuhua.chemdahucexpand.Theme.ThemeUCGui;

import java.io.File;

public class Tool {

    public static void saveAllConfig(){
        //创建一个插件文件夹路径为基础的 并追加下一层。所以此时的文件应该是Config.yml
        //exists 代表是否存在
        if (!(new File(Main.getMainPlugin().getDataFolder(),"Config.yml").exists()))
            Main.getMainPlugin().saveResource("Config.yml", false);
        if (!(new File(Main.getMainPlugin().getDataFolder(),"UCGui.yml").exists()))
            Main.getMainPlugin().saveResource("UCGui.yml", false);
    }

    public static YamlConfiguration loadFile(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }


    //加载文件
    public static void load(){
        Config.load();
        UCGui.load();
    }



}
