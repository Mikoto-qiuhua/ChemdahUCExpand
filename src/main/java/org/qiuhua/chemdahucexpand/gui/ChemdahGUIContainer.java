package org.qiuhua.chemdahucexpand.gui;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.method.SchedulerRunnable;
import com.daxton.unrealcore.common.type.MouseActionType;
import com.daxton.unrealcore.common.type.MouseButtonType;
import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;
import com.daxton.unrealcore.display.content.module.control.ButtonModule;
import com.daxton.unrealcore.display.content.module.control.ContainerModule;
import com.daxton.unrealcore.display.content.module.display.TextModule;
import ink.ptms.chemdah.core.conversation.PlayerReply;
import ink.ptms.chemdah.core.conversation.Session;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.qiuhua.chemdahucexpand.Main;
import org.qiuhua.chemdahucexpand.config.UCGui;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ChemdahGUIContainer extends UnrealCoreGUI {

    public Map<String, String> customValue = new HashMap<>();
    SchedulerRunnable schedulerRunnable;

    public  Session session;

    //回复按钮
    private final ButtonModule playerReplyButton = (ButtonModule)this.getModule("ReplyButton").copy();


    public ChemdahGUIContainer(String guiName, FileConfiguration fileConfiguration) {
        super(guiName, fileConfiguration);
        this.removeModule("ReplyButton");
    }

    public void setTitle(String title) {
        TextModule titleModule = (TextModule)this.getModule(new String[]{"Title"});
        if (titleModule != null) {
            titleModule.setText(title);
        }
    }

    public void setMessage(List<String> message) {
        TextModule messageModule = (TextModule)this.getModule(new String[]{"Message"});
        if(messageModule != null) {
            messageModule.setText(message);
        }
    }

    public void setPlayerReplyList(Session session, Boolean canReply, String spacingX, String spacingY){
        this.session = session;
        Player player = session.getPlayer();

        CompletableFuture<List<PlayerReply>> replies = session.getConversation().getPlayerSide().checked(session);
        List<PlayerReply> playerReplyList = replies.join();
        int height = this.playerReplyButton.getHeight();
        int width = this.playerReplyButton.getWidth();
        int x = this.playerReplyButton.getX();
        int y = this.playerReplyButton.getY();
        for(int i = 0; i < playerReplyList.size(); ++i){
            PlayerReply reply = playerReplyList.get(i);
            ButtonModule buttonModule = this.playerReplyButton.copy();
            buttonModule.setModuleID("Reply_" + i);
            buttonModule.setText(reply.build(session));
            UUID rf = reply.getRid();
            player.sendMessage(rf);
            if(i != 0){
                if (y >= 0) {
                    buttonModule.setY("(" + height + spacingY + ")*" + i + "+" + y);
                } else {
                    buttonModule.setY("(" + height + spacingY + ")*" + i + y);
                }
                if (x >= 0) {
                    buttonModule.setX("(" + width + spacingX + ")*" + i + "+" + x);
                } else {
                    buttonModule.setX("(" + width + spacingX + ")*" + i + x);
                }
            }
            player.sendMessage(playerReplyList.get(i).getRid());
            int finalI = i;
            buttonModule.onButtonClick((buttonModule2, mouseButtonType, mouseActionType) -> {
                if (mouseButtonType == MouseButtonType.Left && mouseActionType == MouseActionType.Off) {
//                    player.performCommand("/session reply " + playerReplyList.get(finalI).getUniqueId());
                    player.sendMessage(rf);
//                    ChemdahGUIContainer chemdahGUIContainer = new ChemdahGUIContainer("UCGui", UCGui.getUCGui());
//                    UnrealCoreAPI.openGUI(player, chemdahGUIContainer);
                }
            });
            this.addModule(buttonModule);
        }


    }



    public void opening() {
        if (Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholderChange();
        }

    }

    public void placeholderChange() {
        try {
            this.schedulerRunnable = new SchedulerRunnable() {
                public void run() {
                    if (ChemdahGUIContainer.this.getPlayer() == null) {
                        this.cancel();
                    } else {
                        Map<String, String> customValueMap = new HashMap<>();
                        ChemdahGUIContainer.this.customValue.forEach((content, contentChange) -> {
                            if (ChemdahGUIContainer.this.getPlayer() == null) {
                                this.cancel();
                            } else {
                                String value = PlaceholderAPI.setPlaceholders(ChemdahGUIContainer.this.getPlayer(), "%" + content + "%");
                                customValueMap.put(contentChange, value);
                            }
                        });
                        UnrealCoreAPI.setCustomValueMap(ChemdahGUIContainer.this.getPlayer(), customValueMap);
                    }
                }
            };
            this.schedulerRunnable.runTimer(Main.getMainPlugin(), 0L, 5L);
        } catch (IllegalArgumentException var2) {
            Main.getMainPlugin().getLogger().warning("出现错误");
            this.schedulerRunnable.cancel();
        }

    }

    public void close() {
        if (this.schedulerRunnable != null) {
            this.schedulerRunnable.cancel();
        }
        this.customValue.clear();
        //这里是断开本次会话连接
        this.session.close(true);
        super.close();
    }

}
