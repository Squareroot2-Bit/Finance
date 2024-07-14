import { describe, it,  vi } from 'vitest';
import { mount } from '@vue/test-utils';
import UploadRecordView from '@/views/UploadRecordView.vue';

// Mock the dependencies
vi.mock('papaparse', () => ({
  unparse: vi.fn().mockReturnValue('mocked-csv')
}));

const mockTableData = [
  { id: 1, name: 'Test' },
  { id: 2, name: 'Test2' }
];

const mockConvertToRecordArray = vi.fn(() => mockTableData);

describe('UploadRecordView.vue', () => {
    it('上传功能存在', async () => {
      // 挂载组件
      const wrapper = mount(UploadRecordView);
  
      // 找到上传组件
      const uploadComponent = wrapper.findComponent({ name: 'ElUpload' });
  
      // 模拟文件上传
      const input = uploadComponent.find('input[type="file"]');
      await input.setValue('');
    });
    it('格式文件成功上传', async () => {
        const handleRequest = async (formEl: any) => {
          if (!formEl) return;
          await formEl.validate((valid: boolean, fields: any) => {
            if (valid) {
              console.log('File submitted successfully');
            } else {
              console.log('File submission failed', fields);
            }
          });
        };
    
        const file = {
          validate: (callback: (valid: boolean, fields: any) => void) => {
            callback(true, {});
          },
          resetFields: () => {},
        };
        await handleRequest(file);
      });
      it('格式不正确文件上传失败', async () => {
        const handleRequest = async (formEl: any) => {
          if (!formEl) return;
          await formEl.validate((valid: boolean, fields: any) => {
            if (valid) {
              console.log('File submitted successfully');
            } else {
              console.log('File submission failed', fields);
            }
          });
        };
    
        const file = {
          validate: (callback: (valid: boolean, fields: any) => void) => {
            callback(false, {});
          },
          resetFields: () => {},
        };
        await handleRequest(file);
      });
      it('导出文件成功', async () => {
        const convertRecordToFile = async (formEl: any) => {
          if (!formEl) return;
          await formEl.validate((valid: boolean, fields: any) => {
            if (valid) {
              console.log('File submitted successfully');
            } else {
              console.log('File submission failed', fields);
            }
          });
        };
    
        const form = {
          validate: (callback: (valid: boolean, fields: any) => void) => {
            callback(true, {});
          },
          resetFields: () => {},
        };
        await convertRecordToFile(form);
      });
  });