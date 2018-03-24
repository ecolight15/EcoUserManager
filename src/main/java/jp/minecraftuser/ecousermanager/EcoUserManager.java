package jp.minecraftuser.ecousermanager;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.CommandFrame;
import jp.minecraftuser.ecousermanager.command.EcuCommand;
import jp.minecraftuser.ecousermanager.command.EcuReloadCommand;
import jp.minecraftuser.ecousermanager.command.UnameCommand;
import jp.minecraftuser.ecousermanager.command.UuidCommand;
import jp.minecraftuser.ecousermanager.config.EcoUserManagerConfig;
import jp.minecraftuser.ecousermanager.db.EcoUserUUIDStore;
import jp.minecraftuser.ecousermanager.listener.PlayerListener;

public class EcoUserManager extends PluginFrame {
    private static EcoUserManagerConfig ecuConf = null;
    public void onEnable() {
        initialize();
        ecuConf = (EcoUserManagerConfig)getDefaultConfig();
    }

    public void onDisable()
    {
        if (ecuConf.getStore() != null) ecuConf.getStore().finalize();
        disable();
    }

    @Override
    public void initializeConfig() {
        EcoUserManagerConfig conf = new EcoUserManagerConfig(this);
        conf.registerBoolean("user-request");
        registerPluginConfig(conf);
    }

    @Override
    public void initializeCommand() {
        CommandFrame cmd = new EcuCommand(this, "ecu");
        cmd.addCommand(new EcuReloadCommand(this, "reload"));
        registerPluginCommand(cmd);
        registerPluginCommand(new UnameCommand(this, "uname"));
        registerPluginCommand(new UuidCommand(this, "uuid"));
    }

    @Override
    public void initializeListener() {
        registerPluginListener(new PlayerListener(this, "player"));
    }

    public EcoUserUUIDStore getStore() {
        return ecuConf.getStore();
    }

}