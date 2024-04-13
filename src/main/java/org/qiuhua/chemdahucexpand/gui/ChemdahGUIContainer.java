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
import javafx.print.Printer;
import javafx.scene.layout.Priority;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.qiuhua.chemdahucexpand.Main;
import org.qiuhua.chemdahucexpand.config.Config;
import org.qiuhua.chemdahucexpand.config.UCGui;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ChemdahGUIContainer extends UnrealCoreGUI {


    //本次对话的连接
    private Session session;
    //是否是能够回复  不能回复就是最后一句话
    private Boolean canReply;
    //本次对话主要消息
    private List<String> message;
    //回复选项的X间隔
    private final String spacingX = Config.getStr("Option.spacingX");
    //回复选项的Y间隔
    private final String spacingY = Config.getStr("Option.spacingY");
    //如果是最后一句对话 延迟多少tick关闭对话
    private final int talkCloseDelay = Config.getInt("Option.talkCloseDelay");
    //点击回复按钮的音效
    private final Sound sound = Sound.valueOf(Config.getStr("Option.sound.name"));
    private final Float volume = (float) Config.getInt("Option.sound.v");
    private final Float pitch = (float) Config.getInt("Option.sound.p");
    //回复按钮
    private final ButtonModule playerReplyButton = (ButtonModule)this.getModule("ReplyButton").copy();
    //文本是否打印完成
    private Boolean stopAnimation;


    public ChemdahGUIContainer(String guiName, FileConfiguration fileConfiguration) {
        super(guiName, fileConfiguration);
        this.removeModule("ReplyButton");
    }

    //设置参数
    public void setOption(Session session, List<String> message, Boolean canReply){
        this.session = session;
        this.message = message;
        this.canReply = canReply;
        //设置本次对话的标题
        TextModule titleModule = (TextModule)this.getModule(new String[]{"Title"});
        if (titleModule != null) {
            titleModule.setText(session.getConversation().getOption().getTitle());
        }
    }


    //设置回复项
    public void setPlayerReplyList(){
        int height = this.playerReplyButton.getHeight();
        int width = this.playerReplyButton.getWidth();
        int x = this.playerReplyButton.getX();
        int y = this.playerReplyButton.getY();
        Player player = session.getPlayer();
        CompletableFuture<List<PlayerReply>> replies = session.getConversation().getPlayerSide().checked(session);
        List<PlayerReply> playerReplyList = replies.join();
        for(int i = 0; i < playerReplyList.size(); ++i){
            PlayerReply reply = playerReplyList.get(i);
            ButtonModule buttonModule = this.playerReplyButton.copy();
            buttonModule.setModuleID("Reply_" + i);
            buttonModule.setText(reply.build(session));
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
            buttonModule.onButtonClick((buttonModule2, mouseButtonType, mouseActionType) -> {
                if (mouseButtonType == MouseButtonType.Left && mouseActionType == MouseActionType.Off) {
                    player.playSound(player.getLocation(),sound,volume,pitch);
                    player.chat("/session reply " + reply.getRid());
                }
            });
            this.addModule(buttonModule);
        }
    }


    //设置文本组件
    public void setMessage(){
        //获取消息组件并且设置文本
        TextModule messageModule = (TextModule)this.getModule(new String[]{"Message"});
        if(messageModule != null) {
            messageModule.setText(message);
        }
    }


    //界面打开时自动触发
    public void opening() {
        //设置文本
        this.setMessage();
        //如果文本对话完成就设置回复选项
        this.setPlayerReplyList();
        //刷新界面  只要有组件变动 就要刷新界面
        this.upDate();
        //如果是最后一句回复 那就延迟关闭界面
        if(!canReply){
            Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getMainPlugin(), () -> {
                Bukkit.getScheduler().runTask(Main.getMainPlugin(), this::close);
            }, talkCloseDelay);
        }

    }




    public void close() {
        //这里是断开本次会话连接
        this.session.close(true);
        super.close();
    }


}
