package com.liu.forever.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import xyz.erupt.annotation.Erupt;
import xyz.erupt.annotation.EruptField;
import xyz.erupt.annotation.sub_erupt.RowOperation;
import xyz.erupt.annotation.sub_field.Edit;
import xyz.erupt.annotation.sub_field.EditType;
import xyz.erupt.annotation.sub_field.View;
import xyz.erupt.annotation.sub_field.sub_edit.*;

import javax.persistence.*;
import java.util.Date;


@Erupt(name = "用户管理", primaryKeyCol = "userId")
@Table(name = "user_info")
@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(generator = "CustomID")
    @GenericGenerator(name = "CustomID", strategy = "com.liu.forever.util.CustomIDGenerator")
    @Column(name = "user_id", updatable = false, nullable = false)
    @EruptField
    private String userId;


    @EruptField(
            views = @View(
                    title = "昵称"
            ),
            edit = @Edit(
                    title = "昵称",
                    type = EditType.INPUT, search = @Search, notNull = true,
                    inputType = @InputType
            )
    )
    private String nickname;

    @EruptField(
            views = @View(
                    title = "头像"
            ),
            edit = @Edit(
                    title = "头像",
                    type = EditType.ATTACHMENT, notNull = true,
                    attachmentType = @AttachmentType(type = AttachmentType.Type.IMAGE)
            )
    )
    private String profilePhoto;

    @EruptField(
            views = @View(
                    title = "创建时间", sortable = true
            ),
            edit = @Edit(
                    title = "创建时间",
                    type = EditType.DATE,
                    dateType = @DateType(type = DateType.Type.DATE_TIME)
            )
    )
    private Date creationTime;




}