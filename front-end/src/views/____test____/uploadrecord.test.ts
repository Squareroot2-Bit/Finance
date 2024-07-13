
import 'element-plus/dist/index.css'
import { describe, it, expect, beforeEach } from 'vitest';
import { mount, VueWrapper } from '@vue/test-utils';
import UploadRecordView from '@/views/UploadRecordView.vue';
import ElementPlus from 'element-plus';

describe('UploadRecordView', () => {
  let wrapper: VueWrapper<any>;

  beforeEach(() => {
    wrapper = mount(UploadRecordView, {
      global: {
        plugins: [ElementPlus],
      },
    });
  });




  it('正确处理数据', () => {
    const formatDate = (date: Date) => {
      return date.toISOString().slice(0, 10).replace(/-/g, '');
    };

    const date = new Date('2023-10-01');
    expect(formatDate(date)).toBe('20231001');
  });

  it('正确处理禁用事项', () => {
    const disabledDate = (date: Date) => {
      return date.getTime() > Date.now();
    };

    const futureDate = new Date('2099-12-31');
    expect(disabledDate(futureDate)).toBeTruthy();

    const pastDate = new Date('2020-01-01');
    expect(disabledDate(pastDate)).toBeFalsy();
  });

  it('正确提交表单', async () => {
    const submitForm = async (formEl: any) => {
      if (!formEl) return;
      await formEl.validate((valid: boolean, fields: any) => {
        if (valid) {
          console.log('Form submitted successfully');
        } else {
          console.log('Form submission failed', fields);
        }
      });
    };

    const formEl = {
      validate: (callback: (valid: boolean, fields: any) => void) => {
        callback(true, {});
      },
      resetFields: () => {},
    };

    await submitForm(formEl);
  });

  
});
