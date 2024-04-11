package org.qiuhua.chemdahucexpand;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.qiuhua.chemdahucexpand.command.ChemdahUCExpandCommand;
import org.qiuhua.chemdahucexpand.config.Tool;

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
        new ChemdahUCExpandCommand().register();
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