package org.qiuhua.chemdahucexpand.Theme;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import ink.ptms.chemdah.core.conversation.PlayerReply;
import ink.ptms.chemdah.core.conversation.Session;
import ink.ptms.chemdah.core.conversation.theme.Theme;
import ink.ptms.chemdah.taboolib.module.configuration.ConfigFile;
import ink.ptms.chemdah.taboolib.module.configuration.Configuration;
import ink.ptms.chemdah.taboolib.module.configuration.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.qiuhua.chemdahucexpand.config.UCGui;
import org.qiuhua.chemdahucexpand.gui.UnrealGUIContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ThemeUCGui extends Theme<ThemeUCGuiSettings> {


    @Override
    public ThemeUCGuiSettings createConfig() {

        return new ThemeUCGuiSettings (Configuration.Companion.loadFromOther(UCGui.getUcGui(), Type.YAML, true).getConfigurationSection("gui"));
    }

    /**
     *  这个貌似会在玩家进行对话的时候执行
     * @param session 会话连接
     * @param message 本次的对话内容
     * @param canReply 玩家是否允许回复
     * @return
     */
    @Override
    public CompletableFuture<Void> onDisplay(Session session, List<String> message, boolean canReply) {
        Player player = session.getPlayer();

        //获取对话标题
        session.getConversation().getOption().getTitle();

        //打开一个虚幻核心的gui
        UnrealGUIContainer unrealGUIContainer = new UnrealGUIContainer("CUCGui", UCGui.getUcGui());
        UnrealCoreAPI.inst(player).getGUIHelper().openCoreGUI(unrealGUIContainer);


        //获取玩家可回复列表
        ArrayList<PlayerReply> playerReplyList = session.getPlayerReplyForDisplay();
        //回复指定选项 这里就要看玩家点了哪个选项了
        //获取PlayerReply的uuid 然后使用指令进行回复   指令回复无需我们判断这个回复的条件
        UUID rid = session.getPlayerReplyForDisplay().get(0).getRid();
        player.performCommand("session reply" + rid);

        return null;
    }
}
