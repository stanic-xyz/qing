import { useCallback, useEffect } from 'react';
import {
  ReactFlow,
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
  MarkerType,
  BackgroundVariant
} from '@xyflow/react';
import '@xyflow/react/dist/style.css';
import axios from 'axios';

export default function FundsFlowChart() {
  const [nodes, setNodes, onNodesChange] = useNodesState<any>([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState<any>([]);

  useEffect(() => {
    // 模拟或真实获取后端渠道和账户数据，并组装为图节点
    const loadGraphData = async () => {
      try {
        const [channelsRes, accountsRes] = await Promise.all([
          axios.get('/api/finance/channels/effective'),
          axios.get('/api/finance/accounts')
        ]);

        const channels = channelsRes.data.data || [];
        const accounts = accountsRes.data.data || [];

        if (channels.length === 0 && accounts.length === 0) {
          // No data placeholder
          setNodes([{
            id: 'empty',
            data: { label: '暂无绑定的资金流动数据，请先前往“渠道关系”页面进行配置' },
            position: { x: 250, y: 250 },
            style: { background: '#f9fafb', border: '1px dashed #d1d5db', padding: '20px', color: '#6b7280' }
          }]);
          return;
        }

        const initialNodes: any[] = [];
        const initialEdges: any[] = [];

        // 渲染渠道节点 (中间层)
        channels.forEach((c: any, index: number) => {
          initialNodes.push({
            id: `channel-${c.id}`,
            data: { label: `渠道: ${c.name}` },
            position: { x: 300 + index * 150, y: 150 },
            style: { background: '#dbeafe', border: '1px solid #3b82f6', borderRadius: '8px', padding: '10px' }
          });
        });

        // 渲染账户节点 (下层)
        accounts.forEach((a: any, index: number) => {
          // 只渲染有关联渠道的账户或者所有账户（根据需要，这里渲染所有返回的有效账户）
          initialNodes.push({
            id: `account-${a.id}`,
            data: { label: `账户: ${a.accountName}\n(${a.accountType})` },
            position: { x: 100 + index * 150, y: 350 },
            style: { background: '#fce7f3', border: '1px solid #ec4899', borderRadius: '8px', padding: '10px' }
          });
        });

        // 获取绑定关系并渲染连线
        const edgesList: any[] = [];
        for (const channel of channels) {
           try {
             const relRes = await axios.get(`/api/finance/channels/${channel.id}/accounts`);
             const boundAccounts = relRes.data.data || [];
             boundAccounts.forEach((ba: any) => {
                edgesList.push({
                  id: `e-channel-${channel.id}-account-${ba.id}`,
                  source: `channel-${channel.id}`,
                  target: `account-${ba.id}`,
                  animated: true,
                  markerEnd: {
                    type: MarkerType.ArrowClosed,
                  },
                });
             });
           } catch (e) {
             console.error('Fetch rel error for channel ' + channel.id, e);
           }
        }
        initialEdges.push(...edgesList);

        setNodes(initialNodes);
        setEdges(initialEdges);
      } catch (e) {
        console.error("Failed to load graph data", e);
      }
    };

    loadGraphData();
  }, []);

  const onConnect = useCallback(
    (params: any) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );

  return (
    <div className="flex flex-col h-full relative">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">资金流动图</h1>
      </div>
      <div className="flex-1 bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <ReactFlow
          nodes={nodes}
          edges={edges}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          onConnect={onConnect}
          fitView
        >
          <Controls />
          <MiniMap />
          <Background variant={BackgroundVariant.Dots} gap={12} size={1} />
        </ReactFlow>
      </div>
    </div>
  );
}
