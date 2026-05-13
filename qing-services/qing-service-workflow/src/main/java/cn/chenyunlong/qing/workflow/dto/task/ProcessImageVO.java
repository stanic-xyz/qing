package cn.chenyunlong.qing.workflow.dto.task;

import lombok.Data;

import java.util.List;

// 5. 流程图 VO
@Data
public class ProcessImageVO {
    private String xml;           // BPMN XML
    private List<String> activeNodeIds; // 高亮节点ID
    private List<String> completedFlowIds; // 高亮连线ID
}
