package com.springboot.entity.newPlat.settingTerm;

import com.springboot.entity.newPlat.settingTerm.base.BaseSettings;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenb
 * @version 2020/8/6
 * description：配置的装载类
 */
@Data
@Component
@ConfigurationProperties(prefix = "newplat")
public class NewPlatSettings extends BaseSettings<TurnInnerSettingsTerm> {

}
