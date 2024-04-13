package org.qiuhua.chemdahucexpand.Theme;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.display.been.module.ModuleData;
import ink.ptms.chemdah.core.conversation.PlayerReply;
import ink.ptms.chemdah.core.conversation.Session;
import ink.ptms.chemdah.core.conversation.theme.Theme;
import ink.ptms.chemdah.taboolib.module.configuration.Configuration;
import ink.ptms.chemdah.taboolib.module.configuration.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.qiuhua.chemdahucexpand.Main;
import org.qiuhua.chemdahucexpand.config.Config;
import org.qiuhua.chemdahucexpand.config.UCGui;
import org.qiuhua.chemdahucexpand.gui.ChemdahGUIContainer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ThemeUCGui extends Theme<ThemeUCGuiSettings> {


    @Override
    public ThemeUCGuiSettings createConfig() {

        return new ThemeUCGuiSettings (Configuration.Companion.loadFromOther(Config.getConfig(), Type.YAML, true).getConfigurationSection("Option"));
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
        //打开一个虚幻核心的gui
        ChemdahGUIContainer chemdahGUIContainer = new ChemdahGUIContainer(UUID.randomUUID().toString(), UCGui.getUCGui());
        chemdahGUIContainer.setMessage(message);
        chemdahGUIContainer.setTitle(session.getConversation().getOption().getTitle());
        chemdahGUIContainer.setPlayerReplyList(session, canReply);
        UnrealCoreAPI.openGUI(player, chemdahGUIContainer);
        return CompletableFuture.completedFuture(null);
    }
    /**
     * 是否支持告别
     * 即结束对话时使用 talk 语句创建没有回复的对话信息
     * 在原版 chat 对话模式中支持告别，而 chest 不支持（会被转换为 Holographic 信息）
     */
    @Override
    public boolean allowFarewell() {
        return true;
    }

    /**
     * 会话结束时，目前未被使用
     */
    public CompletableFuture<Void> onClose(Session session, boolean canReply) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        // TODO: 实现会话结束时的逻辑
        future.complete(null);
        return future;
    }

}
