package com.liu.forever.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.*;

import javax.persistence.*;

/**
 * AI提示词
 */


@Erupt(name = "AI角色", primaryKeyCol = "gptId")
@Table(name = "taiyo_ai_role")
@Entity
@Data
public class TaiYoAiRoleDO {
    @Id
    @GeneratedValue(generator = "CustomID")
    @GenericGenerator(name = "CustomID", strategy = "com.liu.forever.util.CustomIDGenerator")
    @Column(name = "gpt_id", updatable = false, nullable = false)
    @EruptField
    private String gptId;


    @EruptField(
            views = @View(
                    title = "AI头像"
            ),
            edit = @Edit(
                    title = "AI头像",
                    type = EditType.ATTACHMENT, search = @Search, notNull = true,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )

    private String aiProfilePhoto;

    @EruptField(
            views = @View(
                    title = "角色名"
            ),
            edit = @Edit(
                    title = "角色名",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String roleName;

    @EruptField(
            views = @View(
                    title = "介绍功能用途"
            ),
            edit = @Edit(
                    title = "介绍功能用途",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String introduceInfo;

    @EruptField(
            views = @View(
                    title = "唯一标识KEY"
            ),
            edit = @Edit(
                    title = "唯一标识KEY",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )

    )
    private String uniqueKey;


    @EruptField(
            views = @View(
                    title = "提示词内容", show = true
            ),
            edit = @Edit(
                    title = "提示词内容",
                    type = EditType.CODE_EDITOR, search = @Search, show = true, notNull = false,
                    codeEditType = @CodeEditorType(language = "txt")
            )
    )
    private @Lob String prompt;


    @EruptField(
            views = @View(
                    title = "top_p"
            ),
            edit = @Edit(
                    title = "top_p",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    @Column(name = "top_p")
    private String topP;

    @EruptField(
            views = @View(
                    title = "temperature"
            ),
            edit = @Edit(
                    title = "temperature",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String temperature;

    @EruptField(
            views = @View(
                    title = "presencePenalty"
            ),
            edit = @Edit(
                    title = "presencePenalty",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String presencePenalty;
    @EruptField(
            views = @View(
                    title = "frequencyPenalty"
            ),
            edit = @Edit(
                    title = "frequencyPenalty",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String frequencyPenalty;

    @EruptField(
            views = @View(
                    title = "最大Token"
            ),
            edit = @Edit(
                    title = "最大Token",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String maxTokens;

    @EruptField(
            views = @View(
                    title = "包含模板"
            ),
            edit = @Edit(
                    title = "包含模板",
                    type = EditType.BOOLEAN, search = @Search, notNull = true,
                    boolType = @BoolType
            )
    )
    private Boolean isTemplateStr;

    @EruptField(
            views = @View(
                    title = "是否可用"
            ),
            edit = @Edit(
                    title = "是否可用",
                    type = EditType.BOOLEAN, search = @Search, notNull = true,
                    boolType = @BoolType
            )
    )
    private Boolean isUsable;


    @EruptField(
            views = @View(
                    title = "是不是角色"
            ),
            edit = @Edit(
                    title = "是不是角色",
                    type = EditType.BOOLEAN, search = @Search, notNull = true,
                    boolType = @BoolType
            )
    )
    private Boolean isRole;


}