package com.liu.forever.pojo.dto;


import com.liu.forever.model.TaiYoAiRoleDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 刘子年
 * @date 2023/10/26
 */
@Data
@AllArgsConstructor

@Builder
public class BaseGptParameterDTO {
    public Double topP;
    public Double temperature;
    public Double presencePenalty;
    public Double frequencyPenalty;
    public Double maxTokens;


    private String model;
    /**
     * 提示词
     */
    private String prompt;


    public BaseGptParameterDTO(TaiYoAiRoleDO aiPromptDOInfo) {
        this.prompt = aiPromptDOInfo.getPrompt();
        this.topP = Double.valueOf(aiPromptDOInfo.getTopP());
        this.temperature = Double.valueOf(aiPromptDOInfo.getTemperature());
        this.presencePenalty = Double.valueOf(aiPromptDOInfo.getPresencePenalty());
        this.frequencyPenalty = Double.valueOf(aiPromptDOInfo.getFrequencyPenalty());
        this.maxTokens = Double.valueOf(aiPromptDOInfo.getMaxTokens());

        this.model = "gpt-4o-2024-05-13";


    }


}
