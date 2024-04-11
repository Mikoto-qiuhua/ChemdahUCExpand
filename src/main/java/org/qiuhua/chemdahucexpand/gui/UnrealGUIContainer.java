package org.qiuhua.chemdahucexpand.gui;

import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.method.SchedulerRunnable;
import com.daxton.unrealcore.common.type.MouseActionType;
import com.daxton.unrealcore.common.type.MouseButtonType;
import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;
import com.daxton.unrealcore.display.content.module.control.ButtonModule;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.qiuhua.chemdahucexpand.Main;

import java.util.HashMap;
import java.util.Map;

public class UnrealGUIContainer extends UnrealCoreGUI {



    public Map<String, String> customValue = new HashMap<>();
    SchedulerRunnable schedulerRunnable;


    public UnrealGUIContainer(String guiName, FileConfiguration fileConfiguration) {
        super(guiName, fileConfiguration);
    }



    public void buttonClick(ButtonModule buttonModule, MouseButtonType button, MouseActionType action) {
        if (button == MouseButtonType.Left && action == MouseActionType.Off) {

        }

        super.buttonClick(buttonModule, button, action);
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
                    if (UnrealGUIContainer.this.getPlayer() == null) {
                        this.cancel();
                    } else {
                        Map<String, String> customValueMap = new HashMap();
                        UnrealGUIContainer.this.customValue.forEach((content, contentChange) -> {
                            if (UnrealGUIContainer.this.getPlayer() == null) {
                                this.cancel();
                            } else {
                                String value = PlaceholderAPI.setPlaceholders(UnrealGUIContainer.this.getPlayer(), "%" + content + "%");
                                customValueMap.put(contentChange, value);
                            }
                        });
                        UnrealCoreAPI.setCustomValueMap(UnrealGUIContainer.this.getPlayer(), customValueMap);
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
        super.close();
    }

}
