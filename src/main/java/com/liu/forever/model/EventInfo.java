package com.liu.forever.model;

import org.hibernate.annotations.GenericGenerator;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.ChoiceType;
import xyz.erupt.annotation.sub_field.sub_edit.InputType;
import xyz.erupt.annotation.sub_field.sub_edit.Search;
import xyz.erupt.annotation.sub_field.sub_edit.VL;
import xyz.erupt.toolkit.handler.SqlChoiceFetchHandler;

import javax.persistence.*;

@Erupt(name = "事件信息", primaryKeyCol = "eId")
@Table(name = "event_info")
@Entity
public class EventInfo {

    @Id
    @GeneratedValue(generator = "CustomID")
    @GenericGenerator(name = "CustomID", strategy = "com.liu.forever.util.CustomIDGenerator")
    @Column(name = "e_id", updatable = false, nullable = false)
    @EruptField
    private String eId;

    @EruptField(
            views = @View(title = "用户名"),
            edit = @Edit(
                    search = @Search,
                    title = "用户名",
                    type = EditType.CHOICE,
                    notNull = true,
                    choiceType = @ChoiceType(
                            fetchHandler = SqlChoiceFetchHandler.class,
                            //参数一必填，表示sql语句
                            //参数二可不填，表示缓存时间，默认为3000毫秒，1.6.10及以上版本支持
                            fetchHandlerParams = {"SELECT user_id  as id, nickname as name  FROM  user_info "}
                    ))
    )

    private String userId;

    @EruptField(
            views = @View(
                    title = "事件信息"
            ),
            edit = @Edit(
                    title = "事件信息",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String eventInfo;


    @EruptField(
            views = @View(
                    title = "事件状态"
            ),
            edit = @Edit(
                    title = "事件状态",
                    type = EditType.CHOICE, search = @Search, notNull = true,
                    choiceType = @ChoiceType(vl = {
                            @VL(value = "未处理", label = "未处理"),
                            @VL(value = "已处理", label = "已处理"),
                    })
            )
    )
    private String taskState;


}
