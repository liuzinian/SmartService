package com.liu.forever.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.*;
import xyz.erupt.toolkit.handler.SqlChoiceFetchHandler;

import javax.persistence.*;
import java.util.Date;

@Erupt(name = "AI聊天记录", primaryKeyCol = "aiId")
@Table(name = "ai_chat_record")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AiChatRecord {
    @Id
    @GeneratedValue(generator = "CustomID")
    @GenericGenerator(name = "CustomID", strategy = "com.liu.forever.util.CustomIDGenerator")
    @Column(name = "ai_id", updatable = false, nullable = false)
    @EruptField
    private String aiId;


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
                    title = "任务状态"
            ),
            edit = @Edit(
                    title = "任务状态",
                    type = EditType.CHOICE, search = @Search, notNull = true,
                    choiceType = @ChoiceType(vl = {
                            @VL(value = "准备中", label = "准备中"),
                            @VL(value = "进行中", label = "进行中"),
                            @VL(value = "异常", label = "异常"),
                            @VL(value = "已完成", label = "已完成"),
                            @VL(value = "取消", label = "取消"),
                            @VL(value = "超时", label = "超时")})
            )
    )
    private String taskState;


    @EruptField(
            views = @View(
                    title = "用户问题", show = true
            ),
            edit = @Edit(
                    title = "用户问题",
                    type = EditType.CODE_EDITOR, search = @Search, show = true, notNull = true,
                    codeEditType = @CodeEditorType(language = "txt")
            )
    )
    private @Lob String userQuestion;
    @EruptField(
            views = @View(
                    title = "AI回复", show = true
            ),
            edit = @Edit(
                    title = "AI回复",
                    type = EditType.CODE_EDITOR, search = @Search, show = true, notNull = true,
                    codeEditType = @CodeEditorType(language = "txt")
            )
    )
    private @Lob String aiReply;


    @EruptField(
            views = @View(
                    title = "请求时间"
            ),
            edit = @Edit(
                    title = "请求时间",
                    type = EditType.DATE, search = @Search, notNull = true,
                    dateType = @DateType(type = DateType.Type.DATE_TIME)
            )
    )
    private Date requestTime;

    @EruptField(
            views = @View(
                    title = "响应完成时间"
            ),
            edit = @Edit(
                    title = "响应完成时间",
                    type = EditType.DATE, search = @Search, notNull = true,
                    dateType = @DateType(type = DateType.Type.DATE_TIME)
            )
    )
    private Date responseCompletionTime;

    @EruptField(
            views = @View(
                    title = "原始请求", show = true
            ),
            edit = @Edit(
                    title = "原始请求",
                    type = EditType.CODE_EDITOR, search = @Search, show = true, notNull = true,
                    codeEditType = @CodeEditorType(language = "txt")
            )
    )
    private @Lob String originalMessage;


    @EruptField(
            views = @View(
                    title = "异常信息", show = true
            ),
            edit = @Edit(
                    title = "异常信息",
                    type = EditType.CODE_EDITOR, search = @Search, show = true, notNull = false,
                    codeEditType = @CodeEditorType(language = "txt")
            )
    )
    private @Lob String errorInfo;
}
