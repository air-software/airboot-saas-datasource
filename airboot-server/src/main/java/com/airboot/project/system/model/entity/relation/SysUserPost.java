package com.airboot.project.system.model.entity.relation;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户和岗位关联 sys_user_post
 *
 * @author airboot
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_post")
public class SysUserPost {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 岗位ID
     */
    private Long postId;
    
}
