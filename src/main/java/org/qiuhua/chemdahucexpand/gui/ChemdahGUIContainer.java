package org.qiuhua.chemdahucexpand.gui;

import com.daxton.unrealcore.common.type.MouseActionType;
import com.daxton.unrealcore.common.type.MouseButtonType;
import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;
import com.daxton.unrealcore.display.content.module.control.ButtonModule;
import com.daxton.unrealcore.display.content.module.display.TextModule;
import ink.ptms.chemdah.core.conversation.PlayerReply;
import ink.ptms.chemdah.core.conversation.Session;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.qiuhua.chemdahucexpand.Main;
import org.qiuhua.chemdahucexpand.config.Config;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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
                    boolean isTalk = false;
                    boolean isClose = false;
                    for(String action : reply.getAction()){
                        if(action.contains("talk")){
                            isTalk = true;
                        }
                        if(action.contains("close")){
                            isClose = true;
                        }
                    }
                    //如果有close没有talk并且没有回复选项 那就立即关闭界面
                    if(isClose && !isTalk){
                        this.close();
                    }
                }
            });
            this.addModule(buttonModule);
            this.upDate();
        }
    }



    public void setMessage(List<String> display){
        //获取消息组件并且设置文本
        TextModule messageModule = (TextModule)this.getModule(new String[]{"Message"});
        if(messageModule != null) {
            messageModule.setText(display);
        }
    }


    //当前文本播放的索引
    private Integer megIndex = 0;
    //动态消息列表
    private final List<String> newMeg = new ArrayList<>();
    //消息播放速度
    private final Integer messagePlaySpeed = Config.getInt("Option.messagePlaySpeed");


    //界面打开时自动触发
    public void opening() {
        List<List<String>> a = new ArrayList<>();
        for (String s : this.message) {
            a.add(splitText(s));
        }
        typeWriter(a, (unused) -> {
            //如果玩家没有的回复 那就延迟关闭界面
            if(!canReply){
                Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getMainPlugin(), () -> {
                    Bukkit.getScheduler().runTask(Main.getMainPlugin(), this::close);
                }, talkCloseDelay);
                return;
            }
            //如果文本对话完成就设置回复选项
            this.setPlayerReplyList();
        });




    }

    private BukkitTask task;

    /**
     *
     * @param a 传递的递归数组
     * @param end 结束时执行的代码
     */
    public void typeWriter (List<List<String>> a, Consumer<Void> end) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getMainPlugin(), (task) -> {
            if(a.isEmpty()){
                task.cancel();
                end.accept(null);
                return;
            }
            this.task = task;
            //每次都取出递归数组中的第一个元素进行索引  也就是第一行文本
            List<String> splitText = a.get(0);
            //如果这个元素是空的那代表里面的文本已经播放完毕 把他给删除 并且进行换行操作
            if(splitText.isEmpty()){
                //移除这个元素 避免下次重复拿取
                a.remove(0);
                //换行操作
                ++ megIndex;
                if(a.isEmpty()){
                    task.cancel();
                    end.accept(null);
                    return;
                }
                //这里要重新拿取新的
                splitText = a.get(0);
            }
            //拿到这行文本的第一个字符 如果这是最后一个字符就不加下划线
            String meg = splitText.get(0);
            if(splitText.size() != 1){
                meg = meg + "_";
            }
            //先看newMeg内有没有这个行数
            //如果他的大小是当前输出的消息行数大小 则代表没有该元素
            if(newMeg.size() == megIndex){
                newMeg.add(meg);
            }else{
                //拼接字符串前先删掉多余的下划线
                String meg0 = newMeg.get(megIndex).replace("_", "");
                //否则就拼接字符串
                newMeg.set(megIndex, meg0 + meg);
            }
            splitText.remove(0);
            setMessage(newMeg);

            //更新界面
            this.upDate();
        }, 0, messagePlaySpeed);
    }

    private static List<String> splitText(String text) {
        List<String> outputList = new ArrayList<>();

        if (text.length() == 1) {
            outputList.add(text);
        } else {
            String firstChar = text.substring(0, 1);
            outputList.add(firstChar);

            String remainingChars = text.substring(1);
            List<String> recursiveList = splitText(remainingChars);
            outputList.addAll(recursiveList);
        }

        return outputList;
    }





    public void close() {
        //这里是断开本次会话连接
        this.session.close(true);
        if(task != null){
            this.task.cancel();
        }
        super.close();
    }


}
