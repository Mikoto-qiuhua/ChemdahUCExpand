package org.qiuhua.chemdahucexpand.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.qiuhua.chemdahucexpand.Main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChemdahUCExpandCommand implements CommandExecutor, TabExecutor {


    public void register() {
        Bukkit.getPluginCommand("ChemdahUCExpand").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0].equalsIgnoreCase("reload") && sender.hasPermission("chemdahucexpand.reload")) {
            Main.getMainPlugin().reloadConfig();
            sender.sendMessage("[ChemdahUCExpand] 重载完成");
        }
        return true;
    }






    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if(sender.hasPermission("chemdahucexpand.reload")) {
            if (args.length == 1) {
                result.add("reload");
            }
        }
        return result;
    }
}

