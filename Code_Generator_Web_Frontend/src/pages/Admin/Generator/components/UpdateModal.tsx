import { updateGeneratorUsingPost } from '@/services/backend/generatorController';
import { ProColumns, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { message, Modal } from 'antd';
import React from 'react';

interface Props {
  oldData?: API.Generator;
  visible: boolean;
  columns: ProColumns<API.Generator>[];
  onSubmit: (values: API.GeneratorAddRequest) => void;
  onCancel: () => void;
}

/**
 * 更新节点，函数
 *
 * @param fields
 */
const handleUpdate = async (fields: API.GeneratorUpdateRequest) => {
  fields.fileConfig = JSON.parse((fields.fileConfig || '{}') as string);
  fields.modelConfig = JSON.parse((fields.modelConfig || '{}') as string);
  const hide = message.loading('正在更新');
  try {
    await updateGeneratorUsingPost(fields);
    hide();
    message.success('更新成功');
    return true;
  } catch (error: any) {
    hide();
    message.error('更新失败，' + error.message);
    return false;
  }
};

/**
 * 更新弹窗，页面
 * @param props
 * @constructor
 */
const UpdateModal: React.FC<Props> = (props) => {
  const { oldData, visible, columns, onSubmit, onCancel } = props;

  if (!oldData) {
    return <></>;
  }

  return (
    // 弹框
    <Modal
      destroyOnClose
      title={'更新代码生成器配置'}
      open={visible}
      footer={null}
      onCancel={() => {
        onCancel?.();
      }}
      style={{textAlign: 'center',fontFamily: '黑体' }}
    >
      {/* 这个组件的作用是用于展示表格数据，并提供一些表格操作和交互功能。 */}
       <ProTable
        style={{fontFamily: 'SimSun' }}
        type="form"
        columns={columns}
        form={{
          initialValues: {
            ...oldData,
            tags: JSON.parse(oldData.tags || '[]'),
          },
        }}
        onSubmit={async (values: API.GeneratorAddRequest) => {
          const success = await handleUpdate({
            ...values,
            id: oldData.id as any,
          });
          if (success) {
            onSubmit?.(values);
          }
        }}
      />
    </Modal>
  );
};
export default UpdateModal;
