
package jp.minecraftuser.ecousermanager.listener;

import jp.minecraftuser.ecoframework.PluginFrame;
import jp.minecraftuser.ecoframework.ListenerFrame;
import jp.minecraftuser.ecousermanager.config.EcoUserManagerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * プレイヤーイベント処理リスナークラス
 * @author ecolight
 */
public class PlayerListener extends ListenerFrame {
    private static EcoUserManagerConfig ecuConf = null;

    /**
     * コンストラクタ
     * @param plg_ プラグインインスタンス
     * @param name_ 名前
     */
    public PlayerListener(PluginFrame plg_, String name_) {
        super(plg_, name_);
        ecuConf = (EcoUserManagerConfig)conf;
    }

    /**
     * プレイヤーログインイベント処理
     * @param event イベント情報
     */
    @EventHandler
    public void PlayerLogin(PlayerLoginEvent event)
    {
        Player p = event.getPlayer();
        log.info("UUID check["+p.getName()+"]:"+p.getUniqueId().toString());
        ecuConf.getStore().checkPlayer(p);
    }

}
