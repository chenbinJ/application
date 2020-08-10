package com.springboot.entity.newPlat.query.req;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.req.PaphReqEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/***
 *   登记资料 查询接口  对接南京新平台 5.1  请求参数  产权查询 适用
 */


@NoArgsConstructor
@Data
public class DjzlReq {

    private String qlrmc;// 权利人名称
    private String qlrzjh; // 权利人证件号
    private String zl; // 坐落

    public DjzlReq initByPaph(PaphReqEntity paph){
        this.qlrmc = paph.getQlrmc();
        this.qlrzjh = paph.getQlrzjh();
        if(StringUtils.isNotBlank(paph.getBdczl())){
            this.zl = paph.getBdczl().replaceAll("\\$","%");
            if(StringUtils.isBlank(this.zl.replaceAll("%","")))
                throw new ZtgeoBizException("非法输入，不可以全部输入“$”符号");
        }
        return this;
    }
}