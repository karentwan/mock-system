package cn.karent.filter.plugin;

import cn.karent.common.Configurable;

/**
 * 可配置的插件适配器
 *
 * @author wanshengdao
 * @date 2024/6/18
 */
public abstract class ConfigurablePlugin<C> extends PluginAdapter implements Configurable<C> {

    /**
     * 配置信息
     */
    protected C config;

    @Override
    public void configure0(C c) {
        this.config = c;
    }
}
