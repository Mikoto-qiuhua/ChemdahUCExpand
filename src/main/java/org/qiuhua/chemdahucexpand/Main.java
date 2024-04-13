package org.qiuhua.chemdahucexpand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.qiuhua.chemdahucexpand.Theme.ThemeUCGui;
import org.qiuhua.chemdahucexpand.command.ChemdahUCExpandCommand;
import org.qiuhua.chemdahucexpand.config.Tool;
import org.qiuhua.chemdahucexpand.listener.UnrealListener;

public class Main extends JavaPlugin {
    private static Main mainPlugin;
    public static Main getMainPlugin(){
        return mainPlugin;
    }

    //启动时运行
    @Override
    public void onEnable(){
        mainPlugin = this;
        Tool.saveAllConfig();
        Tool.load();
        new ThemeUCGui().register("UCGui");
        new ChemdahUCExpandCommand().register();
        Bukkit.getPluginManager().registerEvents(new UnrealListener(), this);
    }


    //关闭时运行
    @Override
    public void onDisable(){

    }

    //执行重载命令时运行
    @Override
    public void reloadConfig(){
        Tool.load();
    }


}