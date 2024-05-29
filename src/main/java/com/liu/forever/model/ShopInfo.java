package com.liu.forever.model;

import org.hibernate.annotations.GenericGenerator;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;

import javax.persistence.*;

@Erupt(name = "商品信息", primaryKeyCol = "shopId")
@Table(name = "shop_info")
@Entity
public class ShopInfo {
    @Id
    @GeneratedValue(generator = "CustomID")
    @GenericGenerator(name = "CustomID", strategy = "com.liu.forever.util.CustomIDGenerator")
    @Column(name = "shop_id", updatable = false, nullable = false)
    @EruptField
    private String shopId;

    @EruptField(
            views = @View(
                    title = "商品名"
            ),
            edit = @Edit(
                    title = "商品名",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String shopName;


    @EruptField(
            views = @View(
                    title = "商品功效"
            ),
            edit = @Edit(
                    title = "商品功效",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String shopEfficacy;

}
